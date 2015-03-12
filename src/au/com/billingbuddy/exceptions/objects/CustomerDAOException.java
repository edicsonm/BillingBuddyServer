package au.com.billingbuddy.exceptions.objects;

public class CustomerDAOException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4895780632596093239L;
	
	
	public CustomerDAOException(Exception e) {
		super(e);
	}

	public CustomerDAOException(String message) {
		super(message);
	}

	public CustomerDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomerDAOException(Throwable cause) {
		super(cause);
	}
}
