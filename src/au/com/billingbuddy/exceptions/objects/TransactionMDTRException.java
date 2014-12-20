package au.com.billingbuddy.exceptions.objects;

public class TransactionMDTRException extends BillingBuddyException {

	private static final long serialVersionUID = -7547774623502489923L;

	public TransactionMDTRException(Exception e) {
		super(e);
	}

	public TransactionMDTRException(String message) {
		super(message);
	}

	public TransactionMDTRException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionMDTRException(Throwable cause) {
		super(cause);
	}
	
	
}
