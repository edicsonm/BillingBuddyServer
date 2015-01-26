package au.com.billingbuddy.business.objects;

import au.com.billingbuddy.exceptions.objects.ProcesorFacadeException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.SecurityFacadeException;
import au.com.billingbuddy.exceptions.objects.SecurityMDTRException;
import au.com.billingbuddy.vo.objects.CertificateVO;

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
	
	
	public boolean validateSignature(String originalMessage, String originalMessageSigned) throws SecurityFacadeException {
		boolean answer = false;
		try {
			answer = securityMDTR.validateSignature(originalMessage, originalMessageSigned);
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
	
}
