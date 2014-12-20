package au.com.billingbuddy.business.objects;


import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.common.objects.Json;
import au.com.billingbuddy.common.objects.SearchBin;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.objects.BinDAO;
import au.com.billingbuddy.dao.objects.CardDAO;
import au.com.billingbuddy.dao.objects.CustomerDAO;
import au.com.billingbuddy.dao.objects.TransactionDAO;
import au.com.billingbuddy.exceptions.objects.BinDAOException;
import au.com.billingbuddy.exceptions.objects.CardDAOException;
import au.com.billingbuddy.exceptions.objects.CustomerDAOException;
import au.com.billingbuddy.exceptions.objects.DataSanitizeException;
import au.com.billingbuddy.exceptions.objects.JsonException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.MySQLTransactionException;
import au.com.billingbuddy.exceptions.objects.SearchBinException;
import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.exceptions.objects.TransactionMDTRException;
import au.com.billingbuddy.vo.objects.BinVO;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.CustomerVO;
import au.com.billingbuddy.vo.objects.ResponseVO;
import au.com.billingbuddy.vo.objects.ShippingAddressVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

public class TransactionMDTR {
	
	private static TransactionMDTR instance = null;
	private FraudDetectionMDRT fraudDetectionMDRT = FraudDetectionMDRT.getInstance();
	private ProcessorMDTR processorMDTR = ProcessorMDTR.getInstance();
	
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();
	
	public static synchronized TransactionMDTR getInstance() {
		if (instance == null) {
			instance = new TransactionMDTR();
		}
		return instance;
	}
	
	private TransactionMDTR() {}
	
	public ResponseVO proccesSymplePaymentTransaction(TransactionVO transactionVO, CardVO cardVO, CustomerVO customerVO, ShippingAddressVO shippingAddressVO) throws TransactionMDTRException {
		ResponseVO responseVO = new ResponseVO();
		try {
			MySQLTransaction mySQLTransaction = new MySQLTransaction();
			BinDAO binDAO = new BinDAO(mySQLTransaction);
			BinVO binVO = new BinVO();
			binVO.setBin(cardVO.getBin());
			binDAO.searchBin(binVO);
			
			if(binVO != null && binVO.getId() == null) {
				binVO = Json.decodeJSONBinInformation(SearchBin.getBinInformation(cardVO.getBin()));
				binDAO.insert(binVO);
				mySQLTransaction.end();
			}
			
			if(binVO != null && binVO.getId() != null) {
				CustomerDAO customerDAO = new CustomerDAO();
				customerDAO.insert(customerVO);
				if(customerVO != null && customerVO.getId() != null){
					CardDAO cardDAO = new CardDAO();
					cardVO.setBin(binVO.getBin());
					cardVO.setCustomerId(customerVO.getId());
					cardDAO.insert(cardVO);
					if(cardVO != null && cardVO.getId() != null){
						
						transactionVO.setCustomerId(customerVO.getId());
						transactionVO.setCardId(cardVO.getId());
						
						TransactionDAO transactionDAO = new TransactionDAO();
						transactionDAO.insert(transactionVO);
						if(transactionVO != null && transactionVO.getId() != null) {
							responseVO.setStatus(instanceConfigurationApplication.getKey("success"));
							responseVO.setMessage(instanceConfigurationApplication.getKey("TransactionDAO.0"));
							responseVO.setData(transactionVO.getId());
						} else {
							responseVO.setStatus(instanceConfigurationApplication.getKey("failure"));
							responseVO.setMessage(instanceConfigurationApplication.getKey("transactionDAO.1"));
						}
					} else {
						responseVO.setStatus(instanceConfigurationApplication.getKey("failure"));
						responseVO.setMessage(instanceConfigurationApplication.getKey("CardDAO.1"));
					}
				}else{
					responseVO.setStatus(instanceConfigurationApplication.getKey("failure"));
					responseVO.setMessage(instanceConfigurationApplication.getKey("CustomerDAO.1"));
				}
			}else{
				responseVO.setStatus(instanceConfigurationApplication.getKey("failure"));
				responseVO.setMessage(instanceConfigurationApplication.getKey("BinDAO.1")); 
			}
			return responseVO;
		} catch (CardDAOException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("0");
			throw transactionMDTRException;
		} catch (CustomerDAOException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("1");
			throw transactionMDTRException;
		} catch (MySQLConnectionException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("2");
			throw transactionMDTRException;
		} catch (TransactionDAOException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("3");
			throw transactionMDTRException;
		} catch (BinDAOException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("4");
			throw transactionMDTRException;
		} catch (MySQLTransactionException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("5");
			throw transactionMDTRException;
		} catch (SearchBinException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("6");
			throw transactionMDTRException;
		} catch (JsonException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("7");
			throw transactionMDTRException;
		} catch (DataSanitizeException e) {
			TransactionMDTRException transactionMDTRException = new TransactionMDTRException(e);
			transactionMDTRException.setErrorCode("8");
			throw transactionMDTRException;
		}
	}
	
	public TransactionVO proccesPayment(TransactionVO transactionVO) throws TransactionMDTRException {
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
					fraudDetectionMDRT.CreditCardFraudDetection(transactionVO);
					if(!transactionVO.isRiskScore()){
						/*Deberia guardarse toda la informacion entregada por MaxMind*/
						/*Se llama al procesador para que procese la transaccion.*/
						processorMDTR.chargeTransaction(transactionVO);
					}else{
						System.out.println("Transaccion supera los limites de riesgo");
					}
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
