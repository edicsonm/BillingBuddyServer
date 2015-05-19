package au.com.billingbuddy.exceptions.objects;

public class ChargeDAOException extends BillingBuddySQLException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ChargeDAOException(Exception e) {
		super(e);
	}

	public ChargeDAOException(String message) {
		super(message);
	}

	public ChargeDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public ChargeDAOException(Throwable cause) {
		super(cause);
	}
}
