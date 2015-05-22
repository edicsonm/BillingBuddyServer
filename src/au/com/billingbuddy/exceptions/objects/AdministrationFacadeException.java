package au.com.billingbuddy.exceptions.objects;

public class AdministrationFacadeException extends BillingBuddyException {
	
	private static final long serialVersionUID = 6294372259532667999L;

	public AdministrationFacadeException(Exception e) {
		super(e);
	}

	public AdministrationFacadeException(String message) {
		super(message);
	}

	public AdministrationFacadeException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdministrationFacadeException(Throwable cause) {
		super(cause);
	}
}
