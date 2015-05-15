package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;


public class YearlySubscriptionDAOException extends BillingBuddySQLException{

	private static final long serialVersionUID = 5212529979107790765L;

	public YearlySubscriptionDAOException(SQLException e) {
		super(e);
	}

	public YearlySubscriptionDAOException(String message) {
		super(message);
	}

	public YearlySubscriptionDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public YearlySubscriptionDAOException(Throwable cause) {
		super(cause);
	}
}
