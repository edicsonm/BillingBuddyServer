package au.com.billingbuddy.exceptions.objects;

public class RefundDAOException extends Exception {
	
	private static final long serialVersionUID = -6415115271136281856L;

	public RefundDAOException(Exception e) {
		super(e);
	}

	public RefundDAOException(String message) {
		super(message);
	}

	public RefundDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public RefundDAOException(Throwable cause) {
		super(cause);
	}
}
