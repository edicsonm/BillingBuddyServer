package au.com.billingbuddy.exceptions.objects;

import au.com.billingbuddy.exceptions.interfaces.IException;

public class PaymentServerException extends Exception implements IException {
	
	private static final long serialVersionUID = 8518132010973914369L;

	public PaymentServerException(Exception e) {
		super(e);
	}

	public PaymentServerException(String message) {
		super(message);
	}

	public PaymentServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public PaymentServerException(Throwable cause) {
		super(cause);
	}
}
