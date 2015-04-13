package au.com.billingbuddy.exceptions.objects;

import au.com.billigbuddy.utils.ErrorManager;

public class ProcessorMDTRException extends BillingBuddyException {

	private static final long serialVersionUID = 2920911009582428078L;

	public ProcessorMDTRException(Exception e, String location, String error) {
		super(e);
		ErrorManager.manageErrorPaymentPage(location, error);
	}
	
	
	public ProcessorMDTRException(Exception e) {
		super(e);
	}

	public ProcessorMDTRException(String message) {
		super(message);
	}

	public ProcessorMDTRException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessorMDTRException(Throwable cause) {
		super(cause);
	}
	
	
}