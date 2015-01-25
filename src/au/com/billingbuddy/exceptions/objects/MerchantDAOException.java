package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;

public class MerchantDAOException extends BillingBuddySQLException{

	private static final long serialVersionUID = 121031465390765747L;

	public MerchantDAOException(SQLException e) {
		super(e);
	}

	public MerchantDAOException(String message) {
		super(message);
	}

	public MerchantDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public MerchantDAOException(Throwable cause) {
		super(cause);
	}
}
