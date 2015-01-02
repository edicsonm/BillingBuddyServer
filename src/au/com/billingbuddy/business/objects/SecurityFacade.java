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

import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.common.objects.SecurityMethods;

public class SecurityFacade {
	
	private static SecurityFacade instance = null;
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();
	
	public static synchronized SecurityFacade getInstance() {
		if (instance == null) {
			instance = new SecurityFacade();
		}
		return instance;
	}
	
	private SecurityFacade() {}
	
	
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
}
