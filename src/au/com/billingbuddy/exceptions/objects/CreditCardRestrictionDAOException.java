package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;


public class CreditCardRestrictionDAOException extends BillingBuddySQLException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4108041198726162479L;

	public CreditCardRestrictionDAOException(SQLException e) {
		super(e);
	}

	public CreditCardRestrictionDAOException(String message) {
		super(message);
	}

	public CreditCardRestrictionDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public CreditCardRestrictionDAOException(Throwable cause) {
		super(cause);
	}
}
