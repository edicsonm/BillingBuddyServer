package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;

public class MerchantConfigurationDAOException extends BillingBuddySQLException{

	private static final long serialVersionUID = 3037404041975240551L;

	public MerchantConfigurationDAOException(SQLException e) {
		super(e);
	}

	public MerchantConfigurationDAOException(String message) {
		super(message);
	}

	public MerchantConfigurationDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public MerchantConfigurationDAOException(Throwable cause) {
		super(cause);
	}
}
