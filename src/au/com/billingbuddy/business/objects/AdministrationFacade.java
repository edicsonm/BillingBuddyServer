package au.com.billingbuddy.business.objects;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import au.com.billingbuddy.exceptions.objects.AdministrationFacadeException;
import au.com.billingbuddy.exceptions.objects.AdministrationMDTRException;
import au.com.billingbuddy.exceptions.objects.SubscriptionsMDTRException;
import au.com.billingbuddy.vo.objects.SubmittedProcessLogVO;

public class AdministrationFacade {
	
	private static AdministrationFacade instance = null;
	private AdministrationMDTR administrationMDTR = AdministrationMDTR.getInstance();
	private SubscriptionsMDTR subscriptionsMDTR = SubscriptionsMDTR.getInstance();
	
	public static synchronized AdministrationFacade getInstance() {
		if (instance == null) {
			instance = new AdministrationFacade();
		}
		return instance;
	}

	private AdministrationFacade() {}
	
	public ArrayList<SubmittedProcessLogVO> listSubmittedProcessLogs(SubmittedProcessLogVO submittedProcessLogVO) throws AdministrationFacadeException{
		ArrayList<SubmittedProcessLogVO> listSubmittedProcessLogs = null;
		try {
			listSubmittedProcessLogs = administrationMDTR.listSubmittedProcessLogs(submittedProcessLogVO);
		} catch (AdministrationMDTRException e) {
			AdministrationFacadeException administrationFacadeException = new AdministrationFacadeException(e);
			administrationFacadeException.setErrorCode(e.getErrorCode());
			throw administrationFacadeException;
		}
		return listSubmittedProcessLogs;
	}
	
	public JSONObject reprocessErrorFile(JSONObject jSONObjectParameters) throws AdministrationFacadeException{
		try {
			/*totalRegistries
			JSONObject jSONObjectParameters*/
//			jSONObjectParameters = subscriptionsMDTR.reprocessFile(jSONObjectParameters);
			return subscriptionsMDTR.reprocessFile(jSONObjectParameters);
		} catch (SubscriptionsMDTRException e) {
			AdministrationFacadeException administrationFacadeException = new AdministrationFacadeException(e);
			administrationFacadeException.setErrorCode(e.getErrorCode());
			throw administrationFacadeException;
		}
	}
}
