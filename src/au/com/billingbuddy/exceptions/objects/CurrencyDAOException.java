package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;

import au.com.billingbuddy.exceptions.interfaces.IException;

public class CurrencyDAOException  extends Exception implements IException {

	private static final long serialVersionUID = 2419468108085998816L;

	public CurrencyDAOException(SQLException e) {
		super(e);
	}

	public CurrencyDAOException(String message) {
		super(message);
	}

	public CurrencyDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public CurrencyDAOException(Throwable cause) {
		super(cause);
	}
}
