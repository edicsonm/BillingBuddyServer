package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;

public class MerchantCustomerDAOException extends BillingBuddySQLException{

	private static final long serialVersionUID = 3204471282431020317L;

	public MerchantCustomerDAOException(SQLException e) {
		super(e);
	}

	public MerchantCustomerDAOException(String message) {
		super(message);
	}

	public MerchantCustomerDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public MerchantCustomerDAOException(Throwable cause) {
		super(cause);
	}
}
