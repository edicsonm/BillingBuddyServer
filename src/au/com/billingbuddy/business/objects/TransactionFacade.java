package au.com.billingbuddy.business.objects;

import java.util.ArrayList;
import java.util.Calendar;

import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.exceptions.objects.FraudDetectionMDTRException;
import au.com.billingbuddy.exceptions.objects.ProcesorFacadeException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.TransactionFacadeException;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

public class TransactionFacade {
	
	ProcessorMDTR processorMDTR = ProcessorMDTR.getInstance();
	FraudDetectionMDTR fraudDetectionMDTR = FraudDetectionMDTR.getInstance();
	
	private static TransactionFacade instance = null;
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();
	
	public static synchronized TransactionFacade getInstance() {
		if (instance == null) {
			instance = new TransactionFacade();
		}
		return instance;
	}
	
	private TransactionFacade() {}
	
	
//	public String proccesMessage(String messageRequest){
//		String messageResponse = null;
//		try {
//			TransactionVO transactionVO = new TransactionVO();
//			CardVO cardVO = new CardVO();
//			CustomerVO customerVO = new CustomerVO();
//			ShippingAddressVO shippingAddressVO = new ShippingAddressVO();
//			Json.decodeJSON(messageRequest, transactionVO, cardVO, customerVO, shippingAddressVO);
//			ResponseVO responseVO = transactionMDTR.proccesSymplePaymentTransaction(transactionVO, cardVO, customerVO, shippingAddressVO);
//			messageResponse = (Json.encodeJSON(responseVO.getStatus(), responseVO.getMessage(), responseVO.getData())).toJSONString();
//		} catch (JsonException e) {
//			messageResponse = Json.encodeJSON(
//						instanceConfigurationApplication.getKey("failure"), 
//						instanceConfigurationApplication.getKey(e.getClass().getSimpleName() + "."+ e.getErrorCode()), 
//						"").toJSONString();
//		} catch (TransactionMDTRException e) {
//			messageResponse = Json.encodeJSON(
//						instanceConfigurationApplication.getKey("failure"), 
//						instanceConfigurationApplication.getKey(e.getClass().getSimpleName() + "."+ e.getErrorCode()), 
//						"").toJSONString();
//		}
//		return messageResponse;
//	}
	
	public TransactionVO proccesPayment(TransactionVO transactionVO) throws TransactionFacadeException {
		try {/*1.- Registrar la tarjeta.*/
			/*2.- Registrar la transaccion.*/
			/*3.- Registrar el Cargo.*/
			/*Realizar todo en una sola transacion*/
			long initialTime = Calendar.getInstance().getTimeInMillis();
			transactionVO = fraudDetectionMDTR.creditCardFraudDetection(transactionVO);
			long finalTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("Tiempo total de procesamiento para MaxMind: " + (finalTime-initialTime) + " ms.");
			
//			transactionVO.setId("17");
//			transactionVO.setHighRiskScore(false);
			
			if(!transactionVO.isHighRiskScore()) {
				initialTime = Calendar.getInstance().getTimeInMillis();	
				processorMDTR.chargePayment(transactionVO);
				finalTime = Calendar.getInstance().getTimeInMillis();
				System.out.println("Tiempo total de procesamiento para Stripe: " + (finalTime-initialTime) + " ms.");
				if(transactionVO != null && transactionVO.getChargeVO() != null && transactionVO.getChargeVO().getId() != null){
					transactionVO.setStatus(instanceConfigurationApplication.getKey("success"));
					transactionVO.setMessage(instanceConfigurationApplication.getKey("TransactionFacade.0"));
		        }else{
		        	if(transactionVO != null && transactionVO.getChargeVO()!= null && transactionVO.getChargeVO().getStripeId() != null){
		        		
		        	}else{
		        		
		        	}
		        	transactionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
					transactionVO.setMessage(instanceConfigurationApplication.getKey("TransactionFacade.1"));
		        }
			} else {
				transactionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
				transactionVO.setMessage(instanceConfigurationApplication.getKey("TransactionFacade.2"));
			}
//		try {
//			transactionMDTR.proccesPayment(transactionVO);
//			transactionVO = fraudDetectionMDRT.CreditCardFraudDetection(transactionVO);
////			if(!transactionVO.isRiskScore()){
////				return transactionMDTR.proccesPayment(transactionVO);
////			}else{
////				return transactionVO;
////			}
//		} catch (TransactionMDTRException e) {
//			e.printStackTrace();
//		}
		} catch (ProcessorMDTRException e) {
//			e.printStackTrace();
//			TransactionFacadeException transactionFacadeException = new TransactionFacadeException(e);
//			transactionFacadeException.setErrorCode("TransactionFacade.proccesPayment.ProcessorMDTRException");
//			throw transactionFacadeException;
			
			TransactionFacadeException transactionFacadeException = new TransactionFacadeException(e);
			transactionFacadeException.setErrorCode(e.getErrorCode());
			throw transactionFacadeException;
			
		} catch (FraudDetectionMDTRException e) {
			e.printStackTrace();
			TransactionFacadeException transactionFacadeException = new TransactionFacadeException(e);
			transactionFacadeException.setErrorCode(e.getErrorCode());
			throw transactionFacadeException;
		}
		return transactionVO;
	}
	
//	public ArrayList<TransactionVO> listTransaction(TransactionVO transactionVO){
//		ArrayList<TransactionVO> listTransaction = transactionMDTR.listTransaction(transactionVO);
//		return listTransaction;
//	}
	
}
