package au.com.billingbuddy.exceptions.objects;

public class TransactionFacadeException extends BillingBuddyException {

	private static final long serialVersionUID = 17479554791894722L;
	
	public TransactionFacadeException(Exception e) {
		super(e);
	}

	public TransactionFacadeException(String message) {
		super(message);
	}

	public TransactionFacadeException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionFacadeException(Throwable cause) {
		super(cause);
	}
}
