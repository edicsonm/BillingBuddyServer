package au.com.billingbuddy.exceptions.objects;

public class FraudDetectionMDTRException extends BillingBuddyException {

	private static final long serialVersionUID = 2920911009582428078L;

	public FraudDetectionMDTRException(Exception e) {
		super(e);
	}

	public FraudDetectionMDTRException(String message) {
		super(message);
	}

	public FraudDetectionMDTRException(String message, Throwable cause) {
		super(message, cause);
	}

	public FraudDetectionMDTRException(Throwable cause) {
		super(cause);
	}
	
	
}