package au.com.billingbuddy.exceptions.objects;

import au.com.billingbuddy.exceptions.interfaces.IException;

public class MySQLTransactionException extends Exception implements IException {
	
	private static final long serialVersionUID = 6498857002647088261L;

	public MySQLTransactionException(Exception e) {
		super(e);
	}

	public MySQLTransactionException(String message) {
		super(message);
	}

	public MySQLTransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public MySQLTransactionException(Throwable cause) {
		super(cause);
	}
}
