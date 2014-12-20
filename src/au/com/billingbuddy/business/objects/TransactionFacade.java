package au.com.billingbuddy.business.objects;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.common.objects.Json;
import au.com.billingbuddy.common.objects.SecurityMethods;
import au.com.billingbuddy.exceptions.objects.JsonException;
import au.com.billingbuddy.exceptions.objects.TransactionMDTRException;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.CustomerVO;
import au.com.billingbuddy.vo.objects.ResponseVO;
import au.com.billingbuddy.vo.objects.ShippingAddressVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

public class TransactionFacade {
	
	TransactionMDTR transactionMDTR = TransactionMDTR.getInstance();
	FraudDetectionMDRT fraudDetectionMDRT = FraudDetectionMDRT.getInstance();
	
	private static TransactionFacade instance = null;
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();
	
	public static synchronized TransactionFacade getInstance() {
		if (instance == null) {
			instance = new TransactionFacade();
		}
		return instance;
	}
	
	private TransactionFacade() {}
	
	
	public boolean validateSignature(String originalMessage, String originalMessageSigned) {
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			char[] password = instanceConfigurationApplication.getKey("passwordKeyStore").toCharArray();//Key Store Password
			java.io.FileInputStream fis = new java.io.FileInputStream(instanceConfigurationApplication.getKey("privacyKeyStore"));
			ks.load(fis, password);
			fis.close();
			// 6. Validar la firma, extraer la clave pública de su certificado de remitentes
			X509Certificate sendercert = (X509Certificate)ks.getCertificate("testsender");
		    PublicKey pubKeySender = sendercert.getPublicKey();
		    
		    // 6.2 Verificar la Firma
		    Signature myVerifySign = Signature.getInstance("MD5withRSA");
		    myVerifySign.initVerify(pubKeySender);
		    myVerifySign.update(originalMessage.getBytes());
		    
		    boolean verifySign = myVerifySign.verify(SecurityMethods.hexaToBytes(originalMessageSigned));
		    if (verifySign == false) {
		    	System.out.println(" Error in validating Signature ");
		    	return false;
		    }else {
		    	System.out.println(" Successfully validated Signature ");
		    	return true;
		    }
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  false;
	}
	
	public String proccesMessage(String messageRequest){
		String messageResponse = null;
		try {
			TransactionVO transactionVO = new TransactionVO();
			CardVO cardVO = new CardVO();
			CustomerVO customerVO = new CustomerVO();
			ShippingAddressVO shippingAddressVO = new ShippingAddressVO();
			Json.decodeJSON(messageRequest, transactionVO, cardVO, customerVO, shippingAddressVO);
			ResponseVO responseVO = transactionMDTR.proccesSymplePaymentTransaction(transactionVO, cardVO, customerVO, shippingAddressVO);
			messageResponse = (Json.encodeJSON(responseVO.getStatus(), responseVO.getMessage(), responseVO.getData())).toJSONString();
		} catch (JsonException e) {
			messageResponse = Json.encodeJSON(
						instanceConfigurationApplication.getKey("failure"), 
						instanceConfigurationApplication.getKey(e.getClass().getSimpleName() + "."+ e.getErrorCode()), 
						"").toJSONString();
		} catch (TransactionMDTRException e) {
			messageResponse = Json.encodeJSON(
						instanceConfigurationApplication.getKey("failure"), 
						instanceConfigurationApplication.getKey(e.getClass().getSimpleName() + "."+ e.getErrorCode()), 
						"").toJSONString();
		}
		return messageResponse;
	}
	
	public TransactionVO proccesPayment(TransactionVO transactionVO){
		/*1.- Registrar la tarjeta.*/
		/*2.- Registrar la transaccion.*/
		/*3.- Registrar el Cargo.*/
		/*Realizar todo en una sola transacion*/
		
		try {
			transactionMDTR.proccesPayment(transactionVO);
			
//			transactionVO = fraudDetectionMDRT.CreditCardFraudDetection(transactionVO);
//			if(!transactionVO.isRiskScore()){
//				return transactionMDTR.proccesPayment(transactionVO);
//			}else{
//				return transactionVO;
//			}
		} catch (TransactionMDTRException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<TransactionVO> listTransaction(TransactionVO transactionVO){
		ArrayList<TransactionVO> listTransaction = transactionMDTR.listTransaction(transactionVO);
		return listTransaction;
	}
	
}
