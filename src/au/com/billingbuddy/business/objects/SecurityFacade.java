package au.com.billingbuddy.business.objects;

import java.sql.SQLException;
import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.SecurityFacadeException;
import au.com.billingbuddy.exceptions.objects.SecurityMDTRException;
import au.com.billingbuddy.vo.objects.CertificateVO;
import au.com.billingbuddy.vo.objects.MerchantConfigurationVO;

public class SecurityFacade {
	
	private static SecurityFacade instance = null;
	SecurityMDTR securityMDTR = SecurityMDTR.getInstance();
	
	public static synchronized SecurityFacade getInstance() {
		if (instance == null) {
			instance = new SecurityFacade();
		}
		return instance;
	}
	
	private SecurityFacade() {}
	
	
	public boolean validateSignature(CertificateVO certificateVO, String originalMessage, String originalMessageSigned) throws SecurityFacadeException {
		boolean answer = false;
		try {
			answer = securityMDTR.validateSignature(certificateVO, originalMessage, originalMessageSigned);
		} catch (SecurityMDTRException e) {
			SecurityFacadeException securityFacadeException = new SecurityFacadeException(e);
			securityFacadeException.setErrorCode(e.getErrorCode());
			throw securityFacadeException;
		}
		return answer;
	}
	
	public CertificateVO certificateGeneration(CertificateVO certificateVO) throws SecurityFacadeException {
		try {
			securityMDTR.certificateGeneration(certificateVO);
		} catch (SecurityMDTRException e) {
			SecurityFacadeException securityFacadeException = new SecurityFacadeException(e);
			securityFacadeException.setErrorCode(e.getErrorCode());
			throw securityFacadeException;
		}
		return certificateVO;	
	}
	
	public ArrayList<CertificateVO> listCertificates(CertificateVO certificateVO) throws SecurityFacadeException {
		ArrayList<CertificateVO> listCertificates = null;
		try {
			listCertificates = securityMDTR.listCertificates(certificateVO);
		} catch (SecurityMDTRException e) {
			SecurityFacadeException securityFacadeException = new SecurityFacadeException(e);
			securityFacadeException.setErrorCode(e.getErrorCode());
			throw securityFacadeException;
		}
	return listCertificates;	
	}
	
	public CertificateVO updateStatusCertificate(CertificateVO certificateVO) throws SecurityFacadeException {
		try {
			certificateVO = securityMDTR.updateStatusCertificate(certificateVO);
		} catch (SecurityMDTRException e) {
			SecurityFacadeException securityFacadeException = new SecurityFacadeException(e);
			securityFacadeException.setErrorCode(e.getErrorCode());
			throw securityFacadeException;
		}
		return certificateVO;
	}
	
	public CertificateVO downloadCertificate(CertificateVO certificateVO) throws SecurityFacadeException {
		try {
			certificateVO = securityMDTR.downloadCertificate(certificateVO);
		} catch (SecurityMDTRException e) {
			SecurityFacadeException securityFacadeException = new SecurityFacadeException(e);
			securityFacadeException.setErrorCode(e.getErrorCode());
			throw securityFacadeException;
		}
		return certificateVO;
	}
	
	
	
	
}
