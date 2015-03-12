package au.com.billingbuddy.exceptions.objects;

import au.com.billingbuddy.exceptions.interfaces.IException;

public class ThreadListenerException extends Exception implements IException {
	
	private static final long serialVersionUID = 6750537621712081258L;

	public ThreadListenerException(Exception e) {
		super(e);
	}

	public ThreadListenerException(String message) {
		super(message);
	}

	public ThreadListenerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ThreadListenerException(Throwable cause) {
		super(cause);
	}
}
