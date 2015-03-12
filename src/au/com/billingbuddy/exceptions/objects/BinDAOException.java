package au.com.billingbuddy.exceptions.objects;

public class BinDAOException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BinDAOException(Exception e) {
		super(e);
	}

	public BinDAOException(String message) {
		super(message);
	}

	public BinDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public BinDAOException(Throwable cause) {
		super(cause);
	}
}
