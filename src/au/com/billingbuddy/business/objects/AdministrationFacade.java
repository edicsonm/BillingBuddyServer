package au.com.billingbuddy.business.objects;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import au.com.billingbuddy.dao.objects.MerchantDAO;
import au.com.billingbuddy.exceptions.objects.AdministrationFacadeException;
import au.com.billingbuddy.exceptions.objects.AdministrationMDTRException;
import au.com.billingbuddy.exceptions.objects.MerchantDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.SubscriptionsMDTRException;
import au.com.billingbuddy.vo.objects.MerchantVO;
import au.com.billingbuddy.vo.objects.SubmittedProcessLogVO;

public class AdministrationFacade {
	
	private static AdministrationFacade instance = null;
	private AdministrationMDTR administrationMDTR = AdministrationMDTR.getInstance();
	private ProcessSubscriptionsMDTR processSubscriptionsMDTR = ProcessSubscriptionsMDTR.getInstance();
	
	public static synchronized AdministrationFacade getInstance() {
		if (instance == null) {
			instance = new AdministrationFacade();
		}
		return instance;
	}

	private AdministrationFacade() {}

	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
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
	
	public SubmittedProcessLogVO searchSubmittedProcessLog(SubmittedProcessLogVO submittedProcessLogVO) throws AdministrationFacadeException{
		try {
			return administrationMDTR.searchSubmittedProcessLog(submittedProcessLogVO);
		} catch (AdministrationMDTRException e) {
			AdministrationFacadeException administrationFacadeException = new AdministrationFacadeException(e);
			administrationFacadeException.setErrorCode(e.getErrorCode());
			throw administrationFacadeException;
		}
	}
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/	
	
	
	public JSONObject reprocessErrorFile(JSONObject jSONObjectParameters) throws AdministrationFacadeException{
		try {
			/*totalRegistries
			JSONObject jSONObjectParameters*/
//			jSONObjectParameters = subscriptionsMDTR.reprocessFile(jSONObjectParameters);
			return processSubscriptionsMDTR.reprocessFile(jSONObjectParameters);
		} catch (SubscriptionsMDTRException e) {
			AdministrationFacadeException administrationFacadeException = new AdministrationFacadeException(e);
			administrationFacadeException.setErrorCode(e.getErrorCode());
			throw administrationFacadeException;
		}
	}
}
