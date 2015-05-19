package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;

public class TransactionDAOException extends BillingBuddySQLException {

	private static final long serialVersionUID = -6030108627553692457L;
	
	public TransactionDAOException(SQLException e) {
		super(e);
	}

	public TransactionDAOException(String message) {
		super(message);
	}

	public TransactionDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionDAOException(Throwable cause) {
		super(cause);
	}
}
