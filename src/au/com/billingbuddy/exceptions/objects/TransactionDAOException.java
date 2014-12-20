package au.com.billingbuddy.exceptions.objects;

import au.com.billingbuddy.exceptions.interfaces.IException;

public class TransactionDAOException extends Exception implements IException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6030108627553692457L;
	public TransactionDAOException(Exception e) {
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
