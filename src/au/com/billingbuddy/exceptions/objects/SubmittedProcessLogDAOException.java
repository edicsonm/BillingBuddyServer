package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;


public class SubmittedProcessLogDAOException extends BillingBuddySQLException {

	private static final long serialVersionUID = -262305314292851754L;

	public SubmittedProcessLogDAOException(SQLException e) {
		super(e);
	}

	public SubmittedProcessLogDAOException(String message) {
		super(message);
	}

	public SubmittedProcessLogDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public SubmittedProcessLogDAOException(Throwable cause) {
		super(cause);
	}
}
