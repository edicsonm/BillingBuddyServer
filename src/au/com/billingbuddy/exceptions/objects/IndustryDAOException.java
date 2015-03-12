package au.com.billingbuddy.exceptions.objects;


public class IndustryDAOException extends BillingBuddySQLException{

	private static final long serialVersionUID = -4771073265614810444L;

	public IndustryDAOException(Exception e) {
		super(e);
	}

	public IndustryDAOException(String message) {
		super(message);
	}

	public IndustryDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public IndustryDAOException(Throwable cause) {
		super(cause);
	}
}
