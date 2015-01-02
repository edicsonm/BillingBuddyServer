package au.com.billingbuddy.business.objects;

import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.exceptions.objects.ProcesorFacadeException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.vo.objects.ChargeVO;

public class ProcesorFacade {
	
	private static ProcesorFacade instance = null;
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();
	ProcessorMDTR processorMDTR = ProcessorMDTR.getInstance();
	
	public static synchronized ProcesorFacade getInstance() {
		if (instance == null) {
			instance = new ProcesorFacade();
		}
		return instance;
	}
	
	private ProcesorFacade() {}

	public ChargeVO processRefund(ChargeVO chargeVO) throws ProcesorFacadeException{
		try {
			processorMDTR.processRefund(chargeVO);
		} catch (ProcessorMDTRException e) {
			e.printStackTrace();
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			procesorFacadeException.setErrorMenssage(instanceConfigurationApplication.getKey(e.getErrorCode()));
			throw procesorFacadeException;
		}
		return chargeVO;
	}

}
