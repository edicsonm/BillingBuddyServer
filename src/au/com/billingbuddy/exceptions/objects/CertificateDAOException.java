package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;


public class CertificateDAOException extends BillingBuddySQLException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 5177943576691755556L;

	public CertificateDAOException(SQLException e) {
		super(e);
	}

	public CertificateDAOException(String message) {
		super(message);
	}

	public CertificateDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public CertificateDAOException(Throwable cause) {
		super(cause);
	}
}
