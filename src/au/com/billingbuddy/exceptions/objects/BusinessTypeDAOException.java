package au.com.billingbuddy.exceptions.objects;


public class BusinessTypeDAOException extends BillingBuddySQLException{

	private static final long serialVersionUID = 1178673561238088276L;

	public BusinessTypeDAOException(Exception e) {
		super(e);
	}

	public BusinessTypeDAOException(String message) {
		super(message);
	}

	public BusinessTypeDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessTypeDAOException(Throwable cause) {
		super(cause);
	}
}
