package au.com.billingbuddy.exceptions.objects;

public class SearchBinException extends BillingBuddyException {
	
	private static final long serialVersionUID = 7541728597704473495L;
	private String errorCode;
	private String errorMessage;

	
	public SearchBinException(BillingBuddyException e) {
		super(e);
	}

	public SearchBinException(String message) {
		super(message);
	}

	public SearchBinException(String message, Throwable cause) {
		super(message, cause);
	}

	public SearchBinException(Throwable cause) {
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
