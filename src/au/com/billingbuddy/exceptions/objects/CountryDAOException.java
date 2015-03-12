package au.com.billingbuddy.exceptions.objects;


public class CountryDAOException extends BillingBuddyException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6219298382988238207L;

	public CountryDAOException(Exception e) {
		super(e);
	}

	public CountryDAOException(String message) {
		super(message);
	}

	public CountryDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public CountryDAOException(Throwable cause) {
		super(cause);
	}
}
