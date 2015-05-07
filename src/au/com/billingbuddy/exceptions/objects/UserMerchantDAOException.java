package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;

import au.com.billingbuddy.exceptions.interfaces.IException;

public class UserMerchantDAOException  extends Exception implements IException {

	private static final long serialVersionUID = -984024383200823680L;

	public UserMerchantDAOException(SQLException e) {
		super(e);
	}

	public UserMerchantDAOException(String message) {
		super(message);
	}

	public UserMerchantDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserMerchantDAOException(Throwable cause) {
		super(cause);
	}
}
