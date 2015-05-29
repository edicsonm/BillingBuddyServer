package au.com.billingbuddy.business.objects;

import java.util.HashMap;

import au.com.billingbuddy.business.objects.ProcessSubscriptionsMDTR.ProcessSubscription;
import au.com.billingbuddy.exceptions.objects.SubscriptionsMDTRException;

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
		try {
			return processSubscriptionsMDTR.executeSubscriptionsToProcess();
		} catch (SubscriptionsMDTRException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public HashMap<String,ProcessSubscription> getHashThreadsProcessSubscription() {
		return processSubscriptionsMDTR.getHashThreadsProcessSubscription();
	}
	
	public void destroyThread(String name) {
		processSubscriptionsMDTR.destroyThread(name);
	}
	
	public void printThreads() {
		processSubscriptionsMDTR.printThreads();
	}
}
