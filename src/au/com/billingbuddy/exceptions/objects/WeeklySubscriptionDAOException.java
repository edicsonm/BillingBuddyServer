package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;


public class WeeklySubscriptionDAOException extends BillingBuddySQLException{

	private static final long serialVersionUID = -660181508891400287L;

	public WeeklySubscriptionDAOException(SQLException e) {
		super(e);
	}

	public WeeklySubscriptionDAOException(String message) {
		super(message);
	}

	public WeeklySubscriptionDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public WeeklySubscriptionDAOException(Throwable cause) {
		super(cause);
	}
}
