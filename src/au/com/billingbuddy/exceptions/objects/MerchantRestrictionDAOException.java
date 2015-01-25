package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;


public class MerchantRestrictionDAOException extends BillingBuddySQLException{

	private static final long serialVersionUID = -1390122858484512396L;

	public MerchantRestrictionDAOException(SQLException e) {
		super(e);
	}

	public MerchantRestrictionDAOException(String message) {
		super(message);
	}

	public MerchantRestrictionDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public MerchantRestrictionDAOException(Throwable cause) {
		super(cause);
	}
}
