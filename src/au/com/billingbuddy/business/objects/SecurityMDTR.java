package au.com.billingbuddy.business.objects;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.SecurityMethods;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.SecurityMDTRException;
import au.com.billingbuddy.vo.objects.CertificateVO;

public class SecurityMDTR {
	
	private static SecurityMDTR instance = null;
//	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();
	private static ConfigurationSystem instanceConfigurationSystem = ConfigurationSystem.getInstance();
	
	public static synchronized SecurityMDTR getInstance() {
		if (instance == null) {
			instance = new SecurityMDTR();
		}
		return instance;
	}
	
	private SecurityMDTR() {}
	
	
	public boolean validateSignature(String originalMessage, String originalMessageSigned) throws SecurityMDTRException {
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
			char[] password = instanceConfigurationSystem.getKey("passwordKeyStore").toCharArray();//Key Store Password
			java.io.FileInputStream fis = new java.io.FileInputStream(instanceConfigurationSystem.getKey("privacyKeyStore"));
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
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.validateSignature.KeyStoreException");
			throw securityMDTRException;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.validateSignature.NoSuchAlgorithmException");
			throw securityMDTRException;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.validateSignature.InvalidKeyException");
			throw securityMDTRException;
		} catch (SignatureException e) {
			e.printStackTrace();
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.validateSignature.SignatureException");
			throw securityMDTRException;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.validateSignature.FileNotFoundException");
			throw securityMDTRException;
		} catch (CertificateException e) {
			e.printStackTrace();
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.validateSignature.CertificateException");
			throw securityMDTRException;
		} catch (IOException e) {
			e.printStackTrace();
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.validateSignature.IOException");
			throw securityMDTRException;
		}
	}
	
	public CertificateVO certificateGeneration(CertificateVO certificateVO) throws SecurityMDTRException {
		try {
			certificateVO.setAliasMerchant(ConfigurationSystem.getKey("aliasMerchant")+certificateVO.getMerchantId());
			System.out.println(certificateVO.getCommonName() +", "+ certificateVO.getOrganizationUnit() + ", " + certificateVO.getOrganization() + ", " + certificateVO.getCountry());
			System.out.println(certificateVO.getAliasMerchant());
			System.out.println(certificateVO.getPasswordKeyStore());
			System.out.println(ConfigurationSystem.getKey("dnaBB"));
			System.out.println(ConfigurationSystem.getKey("aliasBB")+certificateVO.getMerchantId());
			System.out.println(ConfigurationSystem.getKey("passwordBB"));
			System.out.println(ConfigurationSystem.getKey("validDaysCertificate"));
			ProcessBuilder pb = new ProcessBuilder(ConfigurationSystem.getKey("urlScriptCertificateGeneration"),  
					(System.getProperty("java.home") + "/bin"), "cn="+certificateVO.getCommonName() +",ou= "+ certificateVO.getOrganizationUnit() + ",o= " + certificateVO.getOrganization() + ",c= " + certificateVO.getCountry(), 
					certificateVO.getAliasMerchant(), certificateVO.getPasswordKeyStore(), ConfigurationSystem.getKey("dnaBB"),
					(ConfigurationSystem.getKey("aliasBB")+certificateVO.getMerchantId()), ConfigurationSystem.getKey("passwordBB"), ConfigurationSystem.getKey("validDaysCertificate"));
			Process p = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			certificateVO.setLog(new StringBuffer());
			while ((line = reader.readLine()) != null) {
				System.out.println("--> " + line);
				certificateVO.getLog().append(line);
			}
		}catch (Exception e){
			e.printStackTrace();
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.certificateGeneration.Exception");
			throw securityMDTRException;
		}
		return certificateVO;	
	}
		
	
}
