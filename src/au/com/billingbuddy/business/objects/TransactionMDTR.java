package au.com.billingbuddy.business.objects;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import au.com.billigbuddy.utils.ErrorManager;
import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.common.objects.Currency;
import au.com.billingbuddy.common.objects.SaveInformationTransactionThread;
import au.com.billingbuddy.common.objects.TransactionManager;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.objects.CardDAO;
import au.com.billingbuddy.dao.objects.ChargeDAO;
import au.com.billingbuddy.dao.objects.RejectedChargeDAO;
import au.com.billingbuddy.dao.objects.TransactionDAO;
import au.com.billingbuddy.exceptions.objects.CardDAOException;
import au.com.billingbuddy.exceptions.objects.ChargeDAOException;
import au.com.billingbuddy.exceptions.objects.FraudDetectionMDTRException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.MySQLTransactionException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.RejectedChargeDAOException;
import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.exceptions.objects.TransactionFacadeException;
import au.com.billingbuddy.exceptions.objects.TransactionMDTRException;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.RejectedChargeVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

public class TransactionMDTR {
	
	private static TransactionMDTR instance = null;
	private static FraudDetectionMDTR fraudDetectionMDTR = FraudDetectionMDTR.getInstance();
	private static TransactionManager instanceTransactionManager = TransactionManager.getInstance();
	
	private long initialTime;
	private long finalTime;
	
	public static synchronized TransactionMDTR getInstance() {
		if (instance == null) {
			instance = new TransactionMDTR();
		}
		return instance;
	}
	
	private TransactionMDTR() {}
	
	public TransactionVO proccesPaymentFinal(TransactionVO transactionVO) throws TransactionMDTRException {
		// 1.- Contactar a MaxMind
		// 2.- Contactar al Procesador
		// 3.- Salvar toda la informacion Administrativa 
		try {
			
			//Se asume por defecto que toda transaccion sera fraudulenta a menos que se demuestre lo contrario.
			transactionVO.setStatus(ConfigurationApplication.getKey("failure"));
			transactionVO.setMessage(ConfigurationApplication.getKey("TransactionFacade.2"));
			transactionVO.setErrorCode("TransactionFacade.2");
			
			long initialTime = Calendar.getInstance().getTimeInMillis();
			transactionVO = fraudDetectionMDTR.creditCardFraudDetectionFinal(transactionVO);
			long finalTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("Tiempo total de procesamiento para MaxMind: " + (finalTime-initialTime) + " ms.");

			if(!transactionVO.isHighRiskScore()) {
				initialTime = Calendar.getInstance().getTimeInMillis();
				transactionVO = chargePaymentFinal(transactionVO);
				finalTime = Calendar.getInstance().getTimeInMillis();
				System.out.println("Tiempo total de procesamiento para Stripe: " + (finalTime-initialTime) + " ms.");
			}
			
		} catch (FraudDetectionMDTRException e) {
			e.printStackTrace();
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode(e.getErrorCode());
			throw transactionMDTRException;
		}
		return transactionVO;
	}
	
	
	public TransactionVO chargePaymentFinal(TransactionVO transactionVO) throws TransactionMDTRException {
		ChargeVO chargeVO = null;
		Charge charge = new Charge();
		try {
				Map<String, Object> hashMapCharge = new HashMap<String, Object>();
				hashMapCharge.put("amount", Utilities.currencyToStripe(transactionVO.getOrderAmount(), Currency.USD));
				hashMapCharge.put("currency", transactionVO.getOrderCurrency());
				hashMapCharge.put("description", "Charge for test@example.com");
		
				Map<String, Object> hashMapCard = new HashMap<String, Object>();
				hashMapCard.put("number", transactionVO.getCardVO().getNumber());
				hashMapCard.put("exp_month", transactionVO.getCardVO().getExpMonth());
				hashMapCard.put("exp_year", transactionVO.getCardVO().getExpYear());
				hashMapCard.put("cvc", transactionVO.getCardVO().getCvv());
				hashMapCard.put("name", transactionVO.getCardVO().getName());
				
				hashMapCharge.put("card", hashMapCard);
				initialTime = Calendar.getInstance().getTimeInMillis();
				
				charge = Charge.create(hashMapCharge);
				printValues(charge);
		        
				printTimes(initialTime, finalTime);		
				finalTime = Calendar.getInstance().getTimeInMillis();
				chargeVO = new ChargeVO();
				chargeVO.setCardVO(transactionVO.getCardVO());
				chargeVO.setTransactionId(transactionVO.getId());
				chargeVO.setProcessTime((finalTime-initialTime) + " ms.");
				
				long initialTime = Calendar.getInstance().getTimeInMillis();
				Utilities.copyChargeToChargeVO(chargeVO,charge);
				long finalTime = Calendar.getInstance().getTimeInMillis();
				System.out.println("Tiempo total para copiar los elementos de Stripe: " + (finalTime-initialTime) + " ms.");
				
				transactionVO.setCardVO(chargeVO.getCardVO());
				transactionVO.setChargeVO(chargeVO);
				
				printValues(charge);
		        printTimes(initialTime, finalTime);
		        
		        /** Hasta este punto a transaccion fue procesada en el Procesador (STRIPE), se debe proceder con el proceso de registro de la 
		        informacion asociada a la transaccion en la base de datos de BillingBuddy, si alguno de los procesos siguientes falla la informacion 
		        debe ser registrada en un log por seguridad para permitir un reprocesamiento de la informacion que falló.
		        **/
		        
		        instanceTransactionManager.saveInformationTransaction(transactionVO.getId(), transactionVO, charge);
		        
		        transactionVO.setStatus(ConfigurationApplication.getKey("success"));
				transactionVO.setMessage(ConfigurationApplication.getKey("TransactionFacade.0"));
				
			} catch (AuthenticationException e) {
				e.printStackTrace();
				TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e, transactionVO.getId(), "ProcessorMDTR.chargePaymentFinal.AuthenticationException", charge.toString());
				transactionMDTRException.setErrorCode("TransactionMDTR.chargePaymentFinal.AuthenticationException");
				throw transactionMDTRException;
			} catch (InvalidRequestException e) {
				e.printStackTrace();
				TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e, transactionVO.getId(), "ProcessorMDTR.chargePaymentFinal.InvalidRequestException", charge.toString());
				transactionMDTRException.setErrorCode("TransactionMDTR.chargePaymentFinal.InvalidRequestException");
				throw transactionMDTRException;
			} catch (APIConnectionException e) {
				e.printStackTrace();
				TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e, transactionVO.getId(), "ProcessorMDTR.chargePaymentFinal.APIConnectionException", charge.toString());
				transactionMDTRException.setErrorCode("TransactionMDTR.chargePaymentFinal.APIConnectionException");
				throw transactionMDTRException;
			} catch (CardException e) {
				e.printStackTrace();
				try {
					RejectedChargeVO rejectedChargeVO = new RejectedChargeVO();
					rejectedChargeVO.setTransactionId(transactionVO.getId());
					rejectedChargeVO.setAmount(Utilities.currencyToStripe(transactionVO.getOrderAmount(), Currency.USD));
					rejectedChargeVO.setCurrency(transactionVO.getOrderCurrency());
					rejectedChargeVO.setCardNumber(transactionVO.getCardVO().getNumber());
					rejectedChargeVO.setExpYear(transactionVO.getCardVO().getExpYear());
					rejectedChargeVO.setExpMonth(transactionVO.getCardVO().getExpMonth());
					rejectedChargeVO.setCardHolderName(transactionVO.getCardVO().getName());
					rejectedChargeVO.setFailureCode(e.getCode());
					rejectedChargeVO.setFailureMessage(e.getMessage());
					RejectedChargeDAO rejectedChargeDAO = new RejectedChargeDAO();
					rejectedChargeDAO.insert(rejectedChargeVO);
				} catch (MySQLConnectionException ex) {
					TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e, transactionVO.getId(), "ProcessorMDTR.chargePaymentFinal.MySQLConnectionException", charge.toString());
					transactionMDTRException.setErrorCode("TransactionMDTR.chargePaymentFinal.MySQLConnectionException");
					throw transactionMDTRException;
				} catch (RejectedChargeDAOException ex) {
					ex.printStackTrace();
					TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e, transactionVO.getId(), "ProcessorMDTR.chargePaymentFinal.RejectedChargesDAOException", charge.toString());
					transactionMDTRException.setErrorCode("TransactionMDTR.chargePaymentFinal.RejectedChargesDAOException");
					throw transactionMDTRException;
				}
				TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e, transactionVO.getId(),"ProcessorMDTR.chargePaymentFinal.CardException", charge.toString());
				transactionMDTRException.setErrorCode("TransactionMDTR.chargePaymentFinal.CardException." + e.getCode());
				throw transactionMDTRException;
			} catch (APIException e) {
				e.printStackTrace();
				TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e, transactionVO.getId(), "ProcessorMDTR.chargePayment.APIException", charge.toString());
				transactionMDTRException.setErrorCode("TransactionMDTR.chargePayment.APIException");
				throw transactionMDTRException;
			} 
			return transactionVO;
	}
	
	public TransactionVO saveInformationTransaction(TransactionVO transactionVO, ChargeVO chargeVO, Charge charge) throws TransactionMDTRException {
		CardVO cardVOAUX = chargeVO.getCardVO();
        CardVO cardVO = chargeVO.getCardVO();
        MySQLTransaction mySQLTransaction = null;
		try {
			mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			CardDAO cardDAO = new CardDAO(mySQLTransaction);
	        if(cardDAO.searchCardByNumber(cardVOAUX) == null) {//El cliente NO existe, se debe registrar todo
	        	System.out.println("El cliente NO existe, se debe registrar todo");
	        	
	        }else{
	        	System.out.println("El cliente EXISTE");
	        	cardVO.setId(cardVOAUX.getId());
	        	chargeVO.setCardId(cardVO.getId());
	        	chargeVO.setTransactionId(transactionVO.getId());
	        	ChargeDAO chargeDAO = new ChargeDAO(mySQLTransaction);
				initialTime = Calendar.getInstance().getTimeInMillis();
				
				System.out.println("chargeVO.getTransactionId(): " + chargeVO.getTransactionId());
				System.out.println("chargeVO.getStripeId(): " + chargeVO.getStripeId());
				System.out.println("chargeVO.getInvoiceId(): " + chargeVO.getInvoiceId());
				System.out.println("chargeVO.getAddressId(): " + chargeVO.getAddressId());
				
				if(chargeDAO.insert(chargeVO) != 0) {
					ErrorManager.manageErrorPaymentPage("TransactionMDTR.saveInformationTransaction.ChargeDAOException.SaveCharge", charge.toString());
				}
				
				finalTime = Calendar.getInstance().getTimeInMillis();
				System.out.println("Tiempo total para copiar registrar un Charge: " + (finalTime-initialTime) + " ms.");
				System.out.println("chargeVO.getId(): " + chargeVO.getId());
				mySQLTransaction.commit();
	        	
	        }
		} catch (MySQLTransactionException e) {
			e.printStackTrace();
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e, "TransactionMDTR.saveInformationTransaction.MySQLTransactionException", charge.toString());
			transactionMDTRException.setErrorCode("TransactionMDTR.saveInformationTransaction.MySQLTransactionException");
			throw transactionMDTRException;
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
		} catch (CardDAOException e) {
			e.printStackTrace();
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e, "TransactionMDTR.saveInformationTransaction.CardDAOException", charge.toString());
			transactionMDTRException.setErrorCode("TransactionMDTR.saveInformationTransaction.CardDAOException");
			throw transactionMDTRException;
		} catch (ChargeDAOException e) {
			e.printStackTrace();
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e, "TransactionMDTR.saveInformationTransaction.ChargeDAOException", charge.toString());
			transactionMDTRException.setErrorCode("TransactionMDTR.saveInformationTransaction.ChargeDAOException");
			throw transactionMDTRException;
		}finally{
			transactionVO.setChargeVO(chargeVO);
			try {
				if(mySQLTransaction != null){
					mySQLTransaction.close();
				}
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
			}
		}
		return transactionVO;
	}
	
	private void printTimes(long initialTime, long finalTime) {
		System.out.println("Time to obtain answer from maxmind: " + (finalTime-initialTime) + " ms.");
	}
	
	public void printValues(Charge charge){
		if(charge != null){
			System.out.println("charge.getAmount(): " + charge.getAmount());
			System.out.println("charge.getAmountRefunded(): " + charge.getAmountRefunded());
			System.out.println("charge.getBalanceTransaction(): " + charge.getBalanceTransaction());
			System.out.println("charge.getCaptured(): " + charge.getCaptured());
			System.out.println("charge.getCard(): " + charge.getCard());
			System.out.println("charge.getCreated(): " + charge.getCreated());
			System.out.println("charge.getCurrency(): " + charge.getCurrency());
			System.out.println("charge.getCustomer(): " + charge.getCustomer());
			System.out.println("charge.getDescription(): " + charge.getDescription());
			System.out.println("charge.getDispute(): " + charge.getDispute());
			System.out.println("charge.getDisputed(): " + charge.getDisputed());
			System.out.println("charge.getFailureCode(): " + charge.getFailureCode());
			System.out.println("charge.getFailureMessage(): " + charge.getFailureMessage());
			System.out.println("charge.getId(): " + charge.getId());
			System.out.println("charge.getInvoice(): " + charge.getInvoice());
			System.out.println("charge.getLivemode(): " + charge.getLivemode());
			System.out.println("charge.getMetadata(): " + charge.getMetadata());
			System.out.println("charge.getPaid(): " + charge.getPaid());
			System.out.println("charge.getRefunded(): " + charge.getRefunded());
			System.out.println("charge.getRefunds(): " + charge.getRefunds());
			System.out.println("charge.getStatementDescription(): " + charge.getStatementDescription());
			System.out.println("charge.toString(): " + charge.toString());
		}else{
			System.out.println(charge);
		}
	}
	
	public TransactionVO chargePayment_ELimiar_ETODO(TransactionVO transactionVO) throws TransactionMDTRException {
		try {
			/*1.- Registrar la tarjeta.*/
			/*2.- Registrar la transaccion.*/
			/*3.- Registrar el Cargo.*/
			/*Realizar todo en una sola transacion*/
			MySQLTransaction mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			CardVO cardVO = transactionVO.getCardVO();
			CardDAO cardDAO = new CardDAO(mySQLTransaction);
			cardDAO.insert(cardVO);
			if(cardVO!= null && cardVO.getId() != null) {//Tarjeta ya existe o fue registrada satisfactoriamente.
				TransactionDAO transactionDAO = new TransactionDAO(mySQLTransaction);
				transactionDAO.insert(transactionVO);
				if(transactionVO != null && transactionVO.getId() != null) {//Transaccion registrada satisfactoriamente.
					mySQLTransaction.end();//Guarda la informacion en la Base de Datos.(Informacion de la tarjeta y de la transaccion)
					/*Inicia proceso de llamado MaxMind*/
//					fraudDetectionMDRT.CreditCardFraudDetection(transactionVO);
//					if(!transactionVO.isHighRiskScore()){
//						/*Deberia guardarse toda la informacion entregada por MaxMind*/
//						/*Se llama al procesador para que procese la transaccion.*/
//						processorMDTR.chargeTransaction(transactionVO);
//					}else{
//						System.out.println("Transaccion supera los limites de riesgo");
//					}
				}else{
					mySQLTransaction.undo();
					System.out.println("Se genero un error al tratar de registrar la transaccion ...");
				}
			}else{
				mySQLTransaction.undo();
				System.out.println("Se genero un error al tratar de registrar la tarjeta ...");
			}
			
//			CustomerVO customerVO = transactionVO.getCustomerVO();
//			BinVO binVO = new BinVO();
//			BinDAO binDAO = new BinDAO(mySQLTransaction);
//			binVO.setBin(cardVO.getBin());
//			binDAO.searchBin(binVO);
//			
//			if(binVO != null && binVO.getId() == null) {
//				binVO = Json.decodeJSONBinInformation(SearchBin.getBinInformation(cardVO.getBin()));
//				binDAO.insert(binVO);
//				mySQLTransaction.end();
//			}
//			
//			if(binVO != null && binVO.getId() != null) {
//				CustomerDAO customerDAO = new CustomerDAO();
//				customerDAO.insert(customerVO);
//				if(customerVO != null && customerVO.getId() != null){
//					CardDAO cardDAO = new CardDAO();
//					cardVO.setBin(binVO.getBin());
//					cardVO.setCustomerId(customerVO.getId());
//					cardDAO.insert(cardVO);
//					if(cardVO != null && cardVO.getId() != null){
//						
//						transactionVO.setCustomerId(customerVO.getId());
//						transactionVO.setCardId(cardVO.getId());
//						
//						if(transactionVO != null && transactionVO.getId() != null) {
//							transactionVO.setStatus(instanceConfigurationApplication.getKey("success"));
//							transactionVO.setMessage(instanceConfigurationApplication.getKey("TransactionDAO.0"));
//							System.out.println("transactionVO.getId(): " + transactionVO.getId());
//							transactionVO.setData(transactionVO.getId());
//						} else {
//							transactionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
//							transactionVO.setMessage(instanceConfigurationApplication.getKey("transactionDAO.1"));
//						}
//					} else {
//						transactionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
//						transactionVO.setMessage(instanceConfigurationApplication.getKey("CardDAO.1"));
//					}
//				}else{
//					transactionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
//					transactionVO.setMessage(instanceConfigurationApplication.getKey("CustomerDAO.1"));
//				}
//			}else{
//				transactionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
//				transactionVO.setMessage(instanceConfigurationApplication.getKey("BinDAO.1")); 
//			}
			return transactionVO;
		} catch (CardDAOException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("0");
			throw transactionMDTRException;
//		} catch (CustomerDAOException e) {
//			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
//			transactionMDTRException.setErrorCode("1");
//			throw transactionMDTRException;
		} catch (MySQLConnectionException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("2");
			throw transactionMDTRException;
		} catch (TransactionDAOException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("3");
			throw transactionMDTRException;
//		} catch (BinDAOException e) {
//			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
//			transactionMDTRException.setErrorCode("4");
//			throw transactionMDTRException;
		} catch (MySQLTransactionException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("5");
			throw transactionMDTRException;
//		} catch (SearchBinException e) {
//			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
//			transactionMDTRException.setErrorCode("6");
//			throw transactionMDTRException;
//		} catch (JsonException e) {
//			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
//			transactionMDTRException.setErrorCode("7");
//			throw transactionMDTRException;
//		} catch (DataSanitizeException e) {
//			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
//			transactionMDTRException.setErrorCode("8");
//			throw transactionMDTRException;
		}
	}
	
	public ArrayList<TransactionVO> listTransaction(TransactionVO transactionVO){
		try {
			TransactionDAO transactionDAO = new TransactionDAO();
			ArrayList<TransactionVO> listTransaction = transactionDAO.search(transactionVO);
			return listTransaction;
		} catch (TransactionDAOException e) {
			e.printStackTrace();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
