package au.com.billingbuddy.exceptions.objects;

public class MySQLConnectionException extends BillingBuddySQLException {
	
	private static final long serialVersionUID = 8518132010973914369L;

	public MySQLConnectionException(Exception e) {
		super(e);
	}

	public MySQLConnectionException(String message) {
		super(message);
	}

	public MySQLConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public MySQLConnectionException(Throwable cause) {
		super(cause);
	}
}
