package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;

public class RejectedChargeDAOException extends BillingBuddySQLException {

	private static final long serialVersionUID = -3755005386591594873L;

	public RejectedChargeDAOException(SQLException e) {
		super(e);
	}

	public RejectedChargeDAOException(String message) {
		super(message);
	}

	public RejectedChargeDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public RejectedChargeDAOException(Throwable cause) {
		super(cause);
	}
}
