package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;

public class ErrorLogDAOException extends BillingBuddyErrorLogSQLException{

	private static final long serialVersionUID = -1108867121435198961L;

	public ErrorLogDAOException(SQLException e) {
		super(e);
	}
	
	public ErrorLogDAOException(String message) {
		super(message);
	}

	public ErrorLogDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public ErrorLogDAOException(Throwable cause) {
		super(cause);
	}
}
