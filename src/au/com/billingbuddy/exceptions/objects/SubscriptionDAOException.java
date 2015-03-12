package au.com.billingbuddy.exceptions.objects;


public class SubscriptionDAOException extends BillingBuddyException{

	private static final long serialVersionUID = 2820200342299619923L;

	public SubscriptionDAOException(Exception e) {
		super(e);
	}

	public SubscriptionDAOException(String message) {
		super(message);
	}

	public SubscriptionDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public SubscriptionDAOException(Throwable cause) {
		super(cause);
	}
}
