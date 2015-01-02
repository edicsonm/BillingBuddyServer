package au.com.billingbuddy.exceptions.objects;

public class ProcesorFacadeException extends BillingBuddyException {

	private static final long serialVersionUID = 2864947795549718531L;

	public ProcesorFacadeException(Exception e) {
		super(e);
	}

	public ProcesorFacadeException(String message) {
		super(message);
	}

	public ProcesorFacadeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcesorFacadeException(Throwable cause) {
		super(cause);
	}
}
