package au.com.billingbuddy.exceptions.objects;

public class SecurityMDTRException extends BillingBuddyException {

	private static final long serialVersionUID = -3178178199872012188L;

	public SecurityMDTRException(Exception e) {
		super(e);
	}

	public SecurityMDTRException(String message) {
		super(message);
	}

	public SecurityMDTRException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityMDTRException(Throwable cause) {
		super(cause);
	}
	
	
}