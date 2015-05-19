package au.com.billingbuddy.business.objects;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import au.com.billigbuddy.utils.ErrorManager;
import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.Currency;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.objects.ChargeDAO;
import au.com.billingbuddy.dao.objects.DailySubscriptionDAO;
import au.com.billingbuddy.dao.objects.ErrorLogDAO;
import au.com.billingbuddy.dao.objects.MerchantDocumentDAO;
import au.com.billingbuddy.dao.objects.RejectedChargeDAO;
import au.com.billingbuddy.dao.objects.TransactionDAO;
import au.com.billingbuddy.exceptions.objects.ChargeDAOException;
import au.com.billingbuddy.exceptions.objects.DailySubscriptionDAOException;
import au.com.billingbuddy.exceptions.objects.ErrorLogDAOException;
import au.com.billingbuddy.exceptions.objects.FraudDetectionMDTRException;
import au.com.billingbuddy.exceptions.objects.MerchantDocumentDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.MySQLTransactionException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.RejectedChargeDAOException;
import au.com.billingbuddy.exceptions.objects.SubscriptionsMDTRException;
import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.exceptions.objects.TransactionMDTRException;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.DailySubscriptionVO;
import au.com.billingbuddy.vo.objects.ErrorLogVO;
import au.com.billingbuddy.vo.objects.MerchantDocumentVO;
import au.com.billingbuddy.vo.objects.RejectedChargeVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;


public class SubscriptionsMDTR {
	
	private static SubscriptionsMDTR instance = null;
	private static ConfigurationSystem configurationSystem = ConfigurationSystem.getInstance();
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();
	
	private long initialTime;
	private long finalTime;
	
	public static synchronized SubscriptionsMDTR getInstance() {
		if (instance == null) {
			instance = new SubscriptionsMDTR();
		}
		return instance;
	}
	
	private SubscriptionsMDTR() {
		Stripe.apiKey = ConfigurationSystem.getKey("apiKey");
	}
	
	public boolean proccesDailySubscriptions() throws SubscriptionsMDTRException {
		boolean answer = false;
		Map<String, Object> hashMapCharge = new HashMap<String, Object>();
		String logFileName = "ProccesDailySubscriptions - "+ Calendar.getInstance().getTime().toString();
		System.out.println("File name: " + logFileName);
		MySQLTransaction mySQLTransaction = null;
		try {
			mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			DailySubscriptionDAO dailySubscriptionDAO = new DailySubscriptionDAO(mySQLTransaction);
			ArrayList<DailySubscriptionVO> listDailySubscriptions = dailySubscriptionDAO.search();
			
			if(listDailySubscriptions != null) {
				for (Iterator<DailySubscriptionVO> iterator = listDailySubscriptions.iterator(); iterator .hasNext();) {
					DailySubscriptionVO dailySubscriptionVO = iterator.next();
					
					hashMapCharge.put("amount", Utilities.currencyToStripe(dailySubscriptionVO.getAmount(), dailySubscriptionVO.getCurrency()));
					hashMapCharge.put("currency", dailySubscriptionVO.getCurrency());
					hashMapCharge.put("description", "Charge for test@example.com");
			
					Map<String, Object> hashMapCard = new HashMap<String, Object>();
					hashMapCard.put("number", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getNumber());
					hashMapCard.put("exp_month", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpMonth());
					hashMapCard.put("exp_year", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpYear());
					hashMapCard.put("cvc", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getCvv());
					hashMapCard.put("name", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getName());
					hashMapCharge.put("card", hashMapCard);
					try {
						Charge charge = Charge.create(hashMapCharge);
						System.out.println("Se optiene autorizacion numero : " + charge.getId());
						dailySubscriptionVO.setStatus("Charged");
						dailySubscriptionVO.setAuthorizerCode(charge.getId());
					} catch (CardException e) {
						System.out.println("Error: " + e.getMessage());
						dailySubscriptionVO.setStatus("Unpaid");
						dailySubscriptionVO.setAuthorizerReason(e.getMessage());
					}
					try{
						if(dailySubscriptionDAO.update(dailySubscriptionVO)== 0) {
							//No fue posible actualizar el status del pago de la subscripcion. Se genera log con los errores.
							JSONObject attributeDetails = new JSONObject();
							attributeDetails.put("Dasu_ID", dailySubscriptionVO.getId());
							attributeDetails.put("Subs_ID", dailySubscriptionVO.getSubscriptionId());
							attributeDetails.put("AutorizationID", dailySubscriptionVO.getAuthorizerCode());
							attributeDetails.put("CALL_DailySubscriptionDAO", dailySubscriptionDAO.getCallString());
													
							JSONObject jSONObject = new JSONObject();
							jSONObject.put("ErrorDetails", attributeDetails);
							manageError(jSONObject,"DailySubscriptions", (dailySubscriptionVO.getId() +"-"+ dailySubscriptionVO.getSubscriptionId()));
						}
					} catch (DailySubscriptionDAOException e) {
						JSONObject errorDetails = new JSONObject();
						errorDetails.put("Dasu_ID", dailySubscriptionVO.getId());
						errorDetails.put("Subs_ID", dailySubscriptionVO.getSubscriptionId());
						errorDetails.put("AutorizationID", dailySubscriptionVO.getAuthorizerCode());
						errorDetails.put("CALL_DailySubscriptionDAO", dailySubscriptionDAO.getCallString());
						System.out.println(errorDetails.toJSONString());
						writeError(logFileName,errorDetails);
						new SubscriptionsMDTRException(e, dailySubscriptionDAO.getCallString());
					}
				}
			}
		} catch (MySQLConnectionException e) {
			throw new SubscriptionsMDTRException(e);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (DailySubscriptionDAOException e) {
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.DailySubscriptionDAOException");
			throw subscriptionsMDTRException;
		} catch (MySQLTransactionException e) {
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.MySQLTransactionException");
			throw subscriptionsMDTRException;
		} finally{
			try {
				if(mySQLTransaction != null){
					mySQLTransaction.close();
				}
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
			}
		}
		return answer;
	}

	public boolean proccesDailySubscriptionsBK() throws SubscriptionsMDTRException {
		boolean answer = false;
		ChargeVO chargeVO = null;
		TransactionVO transactionVO = new TransactionVO();
		Charge charge = new Charge();
		MySQLTransaction mySQLTransaction = null;
		Map<String, Object> hashMapCharge = new HashMap<String, Object>();
		String logFileName = "ProccesDailySubscriptions - "+ Calendar.getInstance().getTime().toString();
		try {
			System.out.println("InetAddress.getLocalHost().toString(): " + InetAddress.getLocalHost().getHostAddress());
			transactionVO.setIp(InetAddress.getLocalHost().getHostAddress());
			transactionVO.setMaxmindId("00000000");
			transactionVO.setUserAgent("LocalUserAgent");
			
			mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			TransactionDAO transactionDAO = new TransactionDAO(mySQLTransaction);
			DailySubscriptionDAO dailySubscriptionDAO = new DailySubscriptionDAO(mySQLTransaction);
			ChargeDAO chargeDAO = new ChargeDAO(mySQLTransaction);
			RejectedChargeDAO rejectedChargeDAO = new RejectedChargeDAO(mySQLTransaction);
			ArrayList<DailySubscriptionVO> listDailySubscriptions = listDailySubscriptions();
			if(listDailySubscriptions != null) {
				for (Iterator<DailySubscriptionVO> iterator = listDailySubscriptions.iterator(); iterator .hasNext();) {
					DailySubscriptionVO dailySubscriptionVO = iterator.next();
					transactionVO.setMerchantId(dailySubscriptionVO.getMerchantId());
					transactionVO.setOrderAmount(dailySubscriptionVO.getAmount());
					transactionVO.setOrderCurrency(dailySubscriptionVO.getCurrency());
					
					try {
						if(transactionDAO.insertTransactionSubscription(transactionVO) != 0) {//Registra la transaccion
							/*Aca se incluye todo el proceso para enviar la TRX al procesador(Stripe)*/
							hashMapCharge.put("amount", Utilities.currencyToStripe(dailySubscriptionVO.getAmount(), dailySubscriptionVO.getCurrency()));
							hashMapCharge.put("currency", dailySubscriptionVO.getCurrency());
							hashMapCharge.put("description", "Charge for test@example.com");
					
							Map<String, Object> hashMapCard = new HashMap<String, Object>();
							hashMapCard.put("number", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getNumber());
							hashMapCard.put("exp_month", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpMonth());
							hashMapCard.put("exp_year", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpYear());
							hashMapCard.put("cvc", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getCvv());
							hashMapCard.put("name", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getName());
							
							hashMapCharge.put("card", hashMapCard);
							initialTime = Calendar.getInstance().getTimeInMillis();
							
							try {
								charge = Charge.create(hashMapCharge);
//								printValues(charge);
		//						printTimes(initialTime, finalTime);		
								finalTime = Calendar.getInstance().getTimeInMillis();
								chargeVO = new ChargeVO();
								chargeVO.setCardVO(dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO());
								chargeVO.setTransactionId(transactionVO.getId());
								chargeVO.setProcessTime((finalTime-initialTime) + " ms.");
								
								long initialTime = Calendar.getInstance().getTimeInMillis();
								Utilities.copyChargeToChargeVO(chargeVO,charge);
								long finalTime = Calendar.getInstance().getTimeInMillis();
		//						System.out.println("Tiempo total para copiar los elementos de Stripe: " + (finalTime-initialTime) + " ms.");
								
		//						transactionVO.setCardVO(chargeVO.getCardVO());
		//						transactionVO.setChargeVO(chargeVO);
								
		//						printValues(charge);
		//				        printTimes(initialTime, finalTime);
						        
						        /** Hasta este punto a transaccion fue procesada en el Procesador (STRIPE), se debe proceder con el proceso de registro de la 
						        informacion asociada a la transaccion en la base de datos de BillingBuddy, si alguno de los procesos siguientes falla la informacion 
						        debe ser registrada en un log por seguridad para permitir un reprocesamiento de la informacion que falló.
						        **/
						        initialTime = Calendar.getInstance().getTimeInMillis();
						        
						        System.out.println("Se optiene autorizacion numero : " + chargeVO.getStripeId());
						        
								if(chargeDAO.insert(chargeVO) != 0) {
									finalTime = Calendar.getInstance().getTimeInMillis();
		//							System.out.println("Tiempo total para copiar registrar un Charge: " + (finalTime-initialTime) + " ms.");
									dailySubscriptionVO.setStatus("Charged");
									dailySubscriptionVO.setAuthorizerCode(chargeVO.getStripeId());
									if(dailySubscriptionDAO.update(dailySubscriptionVO) == 0) {
										System.out.println(dailySubscriptionDAO.getCallString());
										JSONObject attributeDetail = new JSONObject();
										attributeDetail.put("CALL", dailySubscriptionDAO.getCallString());
										JSONObject jSONObject = new JSONObject();
										jSONObject.put("ErrorDetails", attributeDetail);
										
										ErrorLogVO errorLogVO = new ErrorLogVO();
										errorLogVO.setProcessName("DailySubscriptions");
										errorLogVO.setInformation(jSONObject.toJSONString());
										
										ErrorLogDAO errorLogDAO = new ErrorLogDAO();
										if(errorLogDAO.insert(errorLogVO) == 0){
											//Generar archivo de error
											String fileName = dailySubscriptionVO.getId() +"-"+ dailySubscriptionVO.getSubscriptionId();
											ErrorManager.logDailySubscriptionErrorFile(fileName, jSONObject);
										}
									}
									
								} else {//El cargo no pudo ser registrado pero la transaccion ya fue autorizada, 
									  //se debe registrar la informacion para reprocesar la data.
									
									JSONObject attributeDetail = new JSONObject();
									attributeDetail.put("CALL", chargeDAO.getCallString());
									attributeDetail.put("ProcesorAnswer", charge.toString());
									JSONObject jSONObject = new JSONObject();
									jSONObject.put("ErrorDescription", "Unable to registred Charge");
									jSONObject.put("ErrorDetails", attributeDetail);
									String onErrorfileName = dailySubscriptionVO.getId() +"-"+ dailySubscriptionVO.getSubscriptionId();
									manageError(jSONObject,"DailySubscriptions", onErrorfileName);
									
//									ErrorLogVO errorLogVO = new ErrorLogVO();
//									errorLogVO.setProcessName("DailySubscriptions");
//									errorLogVO.setInformation(jSONObject.toJSONString());
//									
//									ErrorLogDAO errorLogDAO = new ErrorLogDAO();
//									if(errorLogDAO.insert(errorLogVO) == 0){
//										ErrorManager.logDailySubscriptionErrorFile((dailySubscriptionVO.getId() +"-"+ dailySubscriptionVO.getSubscriptionId()), jSONObject);
//									}
								}
							} catch (CardException e) {
								//En este momento se genera una transaccion rechazada
								try {
									System.out.println("Genera una transaccion rechazada");
									RejectedChargeVO rejectedChargeVO = new RejectedChargeVO();
									//rejectedChargeVO.setTransactionId(transactionVO.getId());
									rejectedChargeVO.setAmount(Utilities.currencyToStripe(dailySubscriptionVO.getAmount(), dailySubscriptionVO.getCurrency()));
									rejectedChargeVO.setCurrency(dailySubscriptionVO.getCurrency());
									rejectedChargeVO.setCardNumber(dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getNumber());
									rejectedChargeVO.setExpYear(dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpYear());
									rejectedChargeVO.setExpMonth(dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpMonth());
									rejectedChargeVO.setCardHolderName(dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getName());
									rejectedChargeVO.setFailureCode(e.getCode());
									rejectedChargeVO.setFailureMessage(e.getMessage());
									rejectedChargeDAO.insert(rejectedChargeVO);
								}catch (RejectedChargeDAOException ex) {
									new SubscriptionsMDTRException(ex, rejectedChargeDAO.getCallString());
									break;
								}
								//En este momento debe actualizar el status del registro en la tabla DailySubscription
								dailySubscriptionVO.setStatus("Unpaid");
								dailySubscriptionVO.setAuthorizerReason(e.getMessage());
								dailySubscriptionDAO.update(dailySubscriptionVO);
							}
						} else {
							//No fue posible registrar la transaccion en las tablas, el pago nunca fue enviado.
							//Se debe actualizar el status del pago de la subscripcion como no procesado por error en Proceso en BB.
							dailySubscriptionVO.setStatus("BBError");
							dailySubscriptionDAO.update(dailySubscriptionVO);
//							if(dailySubscriptionDAO.update(dailySubscriptionVO) == 0) {
//								//No fue posible actualizar el status del pago de la subscripcion. Se genera log con los errores.
//								JSONObject attributeDetails = new JSONObject();
//								attributeDetails.put("Dasu_ID", dailySubscriptionVO.getId());
//								attributeDetails.put("Subs_ID", dailySubscriptionVO.getSubscriptionId());
//								attributeDetails.put("CALL_DailySubscriptionDAO", dailySubscriptionDAO.getCallString());
//								attributeDetails.put("CALL_transactionDAO", transactionDAO.getCallString());
//														
//								JSONObject jSONObject = new JSONObject();
//								jSONObject.put("ErrorDetails", attributeDetails);
//								
//								manageError(jSONObject,"DailySubscriptions", (dailySubscriptionVO.getId() +"-"+ dailySubscriptionVO.getSubscriptionId()));
//							}
						}
					} catch (TransactionDAOException e) {
						new SubscriptionsMDTRException(e, transactionDAO.getCallString());
						break;
					} catch (DailySubscriptionDAOException e) {
						new SubscriptionsMDTRException(e, dailySubscriptionDAO.getCallString());
						break;
					} catch (ChargeDAOException e) {
						new SubscriptionsMDTRException(e, chargeDAO.getCallString());
						break;
					} catch (AuthenticationException e) {
						SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e, transactionVO.getId(), "SubscriptionsMDTR.proccesDailySubscriptions.AuthenticationException", charge.toString());
						subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.AuthenticationException");
					} catch (InvalidRequestException e) {
						SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e, transactionVO.getId(), "SubscriptionsMDTR.proccesDailySubscriptions.InvalidRequestException", charge.toString());
						subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.InvalidRequestException");
					} catch (APIConnectionException e) {
						SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e, transactionVO.getId(), "SubscriptionsMDTR.proccesDailySubscriptions.APIConnectionException", charge.toString());
						subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.APIConnectionException");
					} catch (APIException e) {
						SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e, transactionVO.getId(), "ProcessorMDTR.chargePayment.APIException", charge.toString());
						subscriptionsMDTRException.setErrorCode("TransactionMDTR.chargePayment.APIException");
					} catch (ErrorLogDAOException e) {
						SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
						subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.ErrorLogDAOException");
					} 
				}
			}
//			transactionVO.setStatus(ConfigurationApplication.getKey("failure"));
//			transactionVO.setMessage(ConfigurationApplication.getKey("TransactionFacade.2"));
//			transactionVO.setErrorCode("TransactionFacade.2");
//			initialTime = Calendar.getInstance().getTimeInMillis();
//			answer =processDailySubscriptions();
//			finalTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("Tiempo definitivo en procesar todas las subscripciones: " + finalTime);
			mySQLTransaction.commit();
			answer = true;
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
		} catch (MySQLTransactionException e) {
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.MySQLTransactionException");
			throw subscriptionsMDTRException;
		} catch (UnknownHostException e) {
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.UnknownHostException");
			throw subscriptionsMDTRException;
		} finally{
			try {
				if(mySQLTransaction != null){
					mySQLTransaction.close();
				}
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
			}
		}
		return answer;
	}
	
	
	public ArrayList<DailySubscriptionVO> listDailySubscriptions() throws SubscriptionsMDTRException{
		ArrayList<DailySubscriptionVO> listDailySubscriptions = null;
		try {
			DailySubscriptionDAO dailySubscriptionDAO = new DailySubscriptionDAO();
			listDailySubscriptions = dailySubscriptionDAO.search();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.processDailySubscriptions.MySQLConnectionException");
			throw subscriptionsMDTRException;
		} catch (DailySubscriptionDAOException e) {
			e.printStackTrace();
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.processDailySubscriptions.DailySubscriptionDAOException");
			throw subscriptionsMDTRException;
		}
		return listDailySubscriptions;
	}
	
	public void writeError(String fileName, JSONObject errorDetails){
		try{
			File file = new File(ConfigurationSystem.getKey("urlSaveErrorFilesSaveInformationSubscriptions") + fileName);
    		System.out.println("Archivo: " + file.getAbsolutePath());
			if(!file.exists()){
    			file.createNewFile();
    		}
    		FileWriter fileWritter = new FileWriter(file.getName(),true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write(errorDetails.toJSONString()+"\n");
	        bufferWritter.close();
	        System.out.println("Done");
    	}catch(IOException e){
    		e.printStackTrace();
    	}
		
		
//			File file = new File(ConfigurationSystem.getKey("urlSaveErrorFilesSaveInformationSubscriptions") + fileName);
//			PrintWriter printWriter = null;
//			try {
//		        System.out.println(file.exists());
//				if (!file.exists()) file.createNewFile();
//		        printWriter = new PrintWriter(new FileOutputStream(fileName, true));
//		        printWriter.append(errorDetails.toJSONString());
//		        printWriter.close();
//		    } catch (IOException ioex) {
//		        ioex.printStackTrace();
////		    } finally {
////		        if (printWriter != null) {
////		            printWriter.flush();
////		            printWriter.close();
////		        }
//		    }
//			
////		try {	
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//			
//			PrintWriter writer = new PrintWriter(file, "UTF-8");
//			writer.append(errorDetails.toJSONString());
//			writer.close();

//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public void manageError(JSONObject jSONObject,String processName, String fileName) {
		try {
			ErrorLogDAO errorLogDAO = new ErrorLogDAO();
			ErrorLogVO errorLogVO = new ErrorLogVO();
			errorLogVO.setProcessName(processName);
			errorLogVO.setInformation(jSONObject.toJSONString());
			if(errorLogDAO.insert(errorLogVO) == 0){
				ErrorManager.logDailySubscriptionErrorFile(fileName, jSONObject);
			}
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
		} catch (ErrorLogDAOException e) {
			e.printStackTrace();
		}
	}
	
	public void printValues(Charge charge){
		if(charge != null) {
//			System.out.println("charge.getAmount(): " + charge.getAmount());
//			System.out.println("charge.getAmountRefunded(): " + charge.getAmountRefunded());
//			System.out.println("charge.getBalanceTransaction(): " + charge.getBalanceTransaction());
//			System.out.println("charge.getCaptured(): " + charge.getCaptured());
//			System.out.println("charge.getCard(): " + charge.getCard());
//			System.out.println("charge.getCreated(): " + charge.getCreated());
//			System.out.println("charge.getCurrency(): " + charge.getCurrency());
//			System.out.println("charge.getCustomer(): " + charge.getCustomer());
//			System.out.println("charge.getDescription(): " + charge.getDescription());
//			System.out.println("charge.getDispute(): " + charge.getDispute());
//			System.out.println("charge.getDisputed(): " + charge.getDisputed());
//			System.out.println("charge.getFailureCode(): " + charge.getFailureCode());
//			System.out.println("charge.getFailureMessage(): " + charge.getFailureMessage());
//			System.out.println("charge.getId(): " + charge.getId());
//			System.out.println("charge.getInvoice(): " + charge.getInvoice());
//			System.out.println("charge.getLivemode(): " + charge.getLivemode());
//			System.out.println("charge.getMetadata(): " + charge.getMetadata());
//			System.out.println("charge.getPaid(): " + charge.getPaid());
//			System.out.println("charge.getRefunded(): " + charge.getRefunded());
//			System.out.println("charge.getRefunds(): " + charge.getRefunds());
//			System.out.println("charge.getStatementDescription(): " + charge.getStatementDescription());
			System.out.println("charge.toString(): " + charge.toString());
		}else{
			System.out.println(charge);
		}
	}
	
	private void printTimes(long initialTime, long finalTime) {
		System.out.println("Time to obtain answer from maxmind: " + (finalTime-initialTime) + " ms.");
	}
	
}
