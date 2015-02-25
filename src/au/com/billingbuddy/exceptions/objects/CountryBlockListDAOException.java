package au.com.billingbuddy.exceptions.objects;


public class CountryBlockListDAOException extends BillingBuddyException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6219298382988238207L;

	public CountryBlockListDAOException(Exception e) {
		super(e);
	}

	public CountryBlockListDAOException(String message) {
		super(message);
	}

	public CountryBlockListDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public CountryBlockListDAOException(Throwable cause) {
		super(cause);
	}
}
