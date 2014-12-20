package au.com.billingbuddy.exceptions.objects;

public class DataSanitizeException extends BillingBuddyException {

	private static final long serialVersionUID = 1432391113238669708L;

	public DataSanitizeException(Exception e) {
		super(e);
	}

	public DataSanitizeException(String message) {
		super(message);
	}

	public DataSanitizeException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public DataSanitizeException(Throwable cause) {
		super(cause);
	}
	
}
