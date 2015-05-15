package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;


public class DailySubscriptionDAOException extends BillingBuddySQLException{

	private static final long serialVersionUID = -262305314292851754L;

	public DailySubscriptionDAOException(SQLException e) {
		super(e);
	}

	public DailySubscriptionDAOException(String message) {
		super(message);
	}

	public DailySubscriptionDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DailySubscriptionDAOException(Throwable cause) {
		super(cause);
	}
}
