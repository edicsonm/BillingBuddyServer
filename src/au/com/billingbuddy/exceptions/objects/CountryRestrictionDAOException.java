package au.com.billingbuddy.exceptions.objects;


public class CountryRestrictionDAOException extends BillingBuddyException{

	private static final long serialVersionUID = 2820200342299619923L;

	public CountryRestrictionDAOException(Exception e) {
		super(e);
	}

	public CountryRestrictionDAOException(String message) {
		super(message);
	}

	public CountryRestrictionDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public CountryRestrictionDAOException(Throwable cause) {
		super(cause);
	}
}
