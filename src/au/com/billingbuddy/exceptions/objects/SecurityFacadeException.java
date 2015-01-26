package au.com.billingbuddy.exceptions.objects;

public class SecurityFacadeException extends BillingBuddyException {

	private static final long serialVersionUID = 17479554791894722L;
	
	public SecurityFacadeException(Exception e) {
		super(e);
	}

	public SecurityFacadeException(String message) {
		super(message);
	}

	public SecurityFacadeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityFacadeException(Throwable cause) {
		super(cause);
	}
}
