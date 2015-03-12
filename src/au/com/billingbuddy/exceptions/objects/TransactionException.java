package au.com.billingbuddy.exceptions.objects;

import au.com.billingbuddy.exceptions.interfaces.IException;

public class TransactionException extends Exception implements IException {

	private static final long serialVersionUID = 3452974227284554696L;
	
	public TransactionException(Exception e) {
		super(e);
	}

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionException(Throwable cause) {
		super(cause);
	}
}
