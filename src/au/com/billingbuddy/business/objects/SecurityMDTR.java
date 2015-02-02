package au.com.billingbuddy.business.objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.SecurityMethods;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.objects.CertificateDAO;
import au.com.billingbuddy.dao.objects.MerchantConfigurationDAO;
import au.com.billingbuddy.exceptions.objects.CertificateDAOException;
import au.com.billingbuddy.exceptions.objects.MerchantConfigurationDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.MySQLTransactionException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.SecurityMDTRException;
import au.com.billingbuddy.vo.objects.CertificateVO;
import au.com.billingbuddy.vo.objects.MerchantConfigurationVO;

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
		HashMap<String , String> infoCertificates = new HashMap<String , String>();
		try {
			certificateVO.setAliasMerchant(ConfigurationSystem.getKey("aliasMerchant")+certificateVO.getMerchantId());
			ProcessBuilder pb = new ProcessBuilder(ConfigurationSystem.getKey("urlScriptCertificateGeneration"),
					(System.getProperty("java.home") + "/bin"),
					"cn="+certificateVO.getCommonName() +",ou= "+ certificateVO.getOrganizationUnit() + ",o= " + certificateVO.getOrganization() + ",c= " + certificateVO.getCountry(), 
					certificateVO.getAliasMerchant(),
					certificateVO.getPasswordKeyStore(),
					certificateVO.getPasswordkey(), 
					ConfigurationSystem.getKey("dnaBB"),
					(ConfigurationSystem.getKey("aliasBB")+certificateVO.getMerchantId()), 
					ConfigurationSystem.getKey("passwordBBKeyStore"),
					ConfigurationSystem.getKey("passwordBBKey"));
			Process p = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			certificateVO.setLog(new StringBuffer());
			int swMerchantCertificate = 0;
			int swBBCertificate = 0;
			StringBuffer infoCertificateMerchant = new StringBuffer();
			StringBuffer infoCertificateBB = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				if(line.contains("#"))Utilities.processingCertificateAnswer(line, infoCertificates);
				else if(line.equalsIgnoreCase("StartMerchantCertificateInformation")){
					swMerchantCertificate = 1;
				}else if(line.equalsIgnoreCase("StopMerchantCertificateInformation")){
					swMerchantCertificate = 0;
				}else if(swMerchantCertificate == 1){
					infoCertificateMerchant.append(line+"\n");
				}else if(line.equalsIgnoreCase("StartBillingBuddyCertificateInformation")){
					swBBCertificate = 1;
				}else if(line.equalsIgnoreCase("StopBillingBuddyCertificateInformation")){
					swBBCertificate = 0;
				}else if(swBBCertificate == 1){
					infoCertificateBB.append(line+"\n");
				}else{
					System.out.println("--> " + line);
				}
				certificateVO.getLog().append(line+"\n");
			}
			
			System.out.println("KEYSTOREBB: " +infoCertificates.get("FOLDER")+"/"+infoCertificates.get("KEYSTOREBB"));
			System.out.println("KEYSTOREMERCHANT: " + infoCertificates.get("FOLDER")+"/"+infoCertificates.get("KEYSTOREMERCHANT"));
			
			File fileKeyStoreBB = new File(infoCertificates.get("FOLDER")+"/"+infoCertificates.get("KEYSTOREBB"));
			certificateVO.setFileKeyStoreBB(fileKeyStoreBB);
			
			File fileKeyStoreMerchant = new File(infoCertificates.get("FOLDER")+"/"+infoCertificates.get("KEYSTOREMERCHANT"));
			certificateVO.setFileKeyStoreMerchant(fileKeyStoreMerchant);
			
			certificateVO.setInfoCertificateBB(infoCertificateBB.toString());
			certificateVO.setInfoCertificateMerchant(infoCertificateMerchant.toString());
			
			CertificateDAO certificateDAO= new CertificateDAO();
			certificateDAO.insert(certificateVO);
			if(certificateVO != null && certificateVO.getId() != null){
				certificateVO.setStatus(ConfigurationApplication.getKey("success"));
				certificateVO.setMessage("SecurityMDTR.certificateGeneration.success");
	        }else{
	        	certificateVO.setStatus(ConfigurationApplication.getKey("failure"));
	        	certificateVO.setMessage("SecurityMDTR.certificateGeneration.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar el Certificado .... ");
	        	System.out.println("#################################################################");
//	        	SecurityMDTRException securityMDTRException = new SecurityMDTRException("");
//				securityMDTRException.setErrorCode("SecurityMDTR.certificateGeneration.failure");
//				throw securityMDTRException;
	        }
		} catch (MySQLConnectionException e) {
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.certificateGeneration.MySQLConnectionException");
			throw securityMDTRException;
		}catch (CertificateDAOException e){
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.certificateGeneration.CertificateDAOException");
			throw securityMDTRException;
		} catch (IOException e) {
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.certificateGeneration.IOException");
			throw securityMDTRException;
		}
		return certificateVO;	
	}
	
	public ArrayList<CertificateVO> listCertificates() throws SecurityMDTRException {
		ArrayList<CertificateVO> listCertificates = null;
		try {
			CertificateDAO certificateDAO = new CertificateDAO();
			listCertificates = certificateDAO.search();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.certificateGeneration.MySQLConnectionException");
			throw securityMDTRException;
		} catch (CertificateDAOException e) {
			e.printStackTrace();
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.certificateGeneration.CertificateDAOException");
			throw securityMDTRException;
		}
		return listCertificates;
	}
	
	public CertificateVO updateStatusCertificate(CertificateVO certificateVO) throws SecurityMDTRException {
		MySQLTransaction mySQLTransaction = null;
		try {
			mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			CertificateDAO certificateDAO = new CertificateDAO(mySQLTransaction);
			if(certificateDAO.updateStatus(certificateVO) == 1) {
				if(certificateVO.getStatus().equalsIgnoreCase("0")) {//Copiar certificado al repositorio de los certificados activos
					certificateVO = certificateDAO.searchDetailBB(certificateVO);
					if(Utilities.configureCertificate(ConfigurationSystem.getKey("urlConfiguredCertificates"), certificateVO, (ConfigurationSystem.getKey("aliasBB")+certificateVO.getMerchantId()+".jks"))){
						certificateVO.setStatus(ConfigurationApplication.getKey("success"));
						certificateVO.setMessage("ProcessorMDTR.updateStatusCertificate.success");
						mySQLTransaction.commit();
					}else{
						certificateVO.setStatus(ConfigurationApplication.getKey("failure"));
			        	certificateVO.setMessage("ProcessorMDTR.updateStatusCertificate.failure");
						System.out.println("#################################################################");
			        	System.out.println("No fue posible actualizar status del certificado .... ");
			        	System.out.println("#################################################################");
						mySQLTransaction.rollback();
					}
				}else{//Remover el certificado del reositorio de los certificados activos
					if(Utilities.removeCertificate(ConfigurationSystem.getKey("urlConfiguredCertificates"), (ConfigurationSystem.getKey("aliasBB")+certificateVO.getMerchantId()+".jks"))){
						certificateVO.setStatus(ConfigurationApplication.getKey("success"));
						certificateVO.setMessage("ProcessorMDTR.updateStatusCertificate.success");
						mySQLTransaction.commit();
					}else{
						certificateVO.setStatus(ConfigurationApplication.getKey("failure"));
			        	certificateVO.setMessage("ProcessorMDTR.updateStatusCertificate.failure");
						System.out.println("#################################################################");
			        	System.out.println("No fue posible actualizar status del certificado .... ");
			        	System.out.println("#################################################################");
						mySQLTransaction.rollback();
					}
				}
	        }else{
	        	certificateVO.setStatus(ConfigurationApplication.getKey("failure"));
	        	certificateVO.setMessage("ProcessorMDTR.updateStatusCertificate.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible actualizar status del certificado .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.updateStatusCertificate.MySQLConnectionException");
			throw securityMDTRException;
		} catch (CertificateDAOException e) {
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.updateStatusCertificate.CertificateDAOException"+ (!Utilities.isNullOrEmpty(e.getSqlObjectName())? ("."+e.getSqlObjectName()):""));
			throw securityMDTRException;
		} catch (MySQLTransactionException e) {
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.updateStatusCertificate.MySQLTransactionException");
			throw securityMDTRException;
		}finally{
			try {
				if(mySQLTransaction != null){
					mySQLTransaction.close();
				}
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
			}
		}
		return certificateVO;
	}
	
	public CertificateVO downloadCertificate(CertificateVO certificateVO) throws SecurityMDTRException {
		try {
			CertificateDAO certificateDAO = new CertificateDAO();
			certificateVO = certificateDAO.searchDetailMerchant(certificateVO);
			if(certificateVO != null){
				certificateVO.setStatus(ConfigurationApplication.getKey("success"));
				certificateVO.setMessage("ProcessorMDTR.downloadCertificate.success");
	        }else{
	        	certificateVO.setStatus(ConfigurationApplication.getKey("failure"));
	        	certificateVO.setMessage("ProcessorMDTR.downloadCertificate.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible actualizar obtener el certificado.... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.downloadCertificate.MySQLConnectionException");
			throw securityMDTRException;
		} catch (CertificateDAOException e) {
			SecurityMDTRException securityMDTRException = new SecurityMDTRException(e);
			securityMDTRException.setErrorCode("SecurityMDTR.downloadCertificate.CertificateDAOException"+ (!Utilities.isNullOrEmpty(e.getSqlObjectName())? ("."+e.getSqlObjectName()):""));
			throw securityMDTRException;
		}
		return certificateVO;
	}
	
}
