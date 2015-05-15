package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;


public class MonthlySubscriptionDAOException extends BillingBuddySQLException{

	private static final long serialVersionUID = 8163134047276745654L;

	public MonthlySubscriptionDAOException(SQLException e) {
		super(e);
	}

	public MonthlySubscriptionDAOException(String message) {
		super(message);
	}

	public MonthlySubscriptionDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public MonthlySubscriptionDAOException(Throwable cause) {
		super(cause);
	}
}
