package au.com.billingbuddy.exceptions.objects;

import au.com.billigbuddy.utils.ErrorManager;

public class SaveInformationTransactionThreadException extends BillingBuddyException {

	private static final long serialVersionUID = 2920911009582428078L;

	public SaveInformationTransactionThreadException(Exception e, String ... attributes) {
		super(e);
		ErrorManager.manageErrorSaveInformationTransaction(attributes);
	}
	
	public SaveInformationTransactionThreadException(Exception e) {
		super(e);
	}

	public SaveInformationTransactionThreadException(String message) {
		super(message);
	}

	public SaveInformationTransactionThreadException(String message, Throwable cause) {
		super(message, cause);
	}

	public SaveInformationTransactionThreadException(Throwable cause) {
		super(cause);
	}
	
	
}