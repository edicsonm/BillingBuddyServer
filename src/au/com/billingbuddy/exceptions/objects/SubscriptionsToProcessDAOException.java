package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;


public class SubscriptionsToProcessDAOException extends BillingBuddySQLException {

	private static final long serialVersionUID = -262305314292851754L;

	public SubscriptionsToProcessDAOException(SQLException e) {
		super(e);
	}

	public SubscriptionsToProcessDAOException(String message) {
		super(message);
	}

	public SubscriptionsToProcessDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public SubscriptionsToProcessDAOException(Throwable cause) {
		super(cause);
	}
}
