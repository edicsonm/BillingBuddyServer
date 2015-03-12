package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;


public class CountryRestrictionDAOException extends BillingBuddySQLException{

	private static final long serialVersionUID = 2820200342299619923L;

	public CountryRestrictionDAOException(SQLException e) {
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
