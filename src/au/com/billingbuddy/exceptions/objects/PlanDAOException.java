package au.com.billingbuddy.exceptions.objects;


public class PlanDAOException extends BillingBuddyException{

	private static final long serialVersionUID = 5242544431278102157L;

	public PlanDAOException(Exception e) {
		super(e);
	}

	public PlanDAOException(String message) {
		super(message);
	}

	public PlanDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public PlanDAOException(Throwable cause) {
		super(cause);
	}
}
