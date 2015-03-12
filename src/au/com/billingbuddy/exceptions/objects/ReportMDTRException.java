package au.com.billingbuddy.exceptions.objects;

public class ReportMDTRException extends BillingBuddyException {

	private static final long serialVersionUID = 2920911009582428078L;

	public ReportMDTRException(Exception e) {
		super(e);
	}

	public ReportMDTRException(String message) {
		super(message);
	}

	public ReportMDTRException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportMDTRException(Throwable cause) {
		super(cause);
	}
	
	
}