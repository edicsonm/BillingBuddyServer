package au.com.billingbuddy.business.objects;

import java.util.HashMap;

public class ProcessSubscriptionFacade {
	
	private static ProcessSubscriptionFacade instance = null;
	private ProcessSubscriptionsMDTR processSubscriptionsMDTR = ProcessSubscriptionsMDTR.getInstance();
	
	public static synchronized ProcessSubscriptionFacade getInstance() {
		if (instance == null) {
			instance = new ProcessSubscriptionFacade();
		}
		return instance;
	}

	private ProcessSubscriptionFacade() {}

	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
	public HashMap<String,String> executeSubscriptionsToProcess() {
		return processSubscriptionsMDTR.executeSubscriptionsToProcess();
	}
}
