package au.com.billingbuddy.exceptions.objects;

public class JsonException extends BillingBuddyException {

	private static final long serialVersionUID = 7260220756238225766L;
	
	private String errorCode;
	private String errorMessage;

	
	public JsonException(BillingBuddyException e) {
		super(e);
	}

	public JsonException(String message) {
		super(message);
	}

	public JsonException(String message, Throwable cause) {
		super(message, cause);
	}

	public JsonException(Throwable cause) {
		super(cause);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
