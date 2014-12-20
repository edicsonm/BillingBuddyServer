package au.com.billingbuddy.exceptions.objects;

import au.com.billingbuddy.exceptions.interfaces.IException;

public class SecurityMethodsException extends Exception implements IException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SecurityMethodsException(Exception e) {
		super(e);
	}

	public SecurityMethodsException(String message) {
		super(message);
	}

	public SecurityMethodsException(String message, Throwable cause) {
		super(message, cause);
	}

	public SecurityMethodsException(Throwable cause) {
		super(cause);
	}
	
}
