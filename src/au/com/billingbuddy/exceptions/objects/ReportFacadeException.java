package au.com.billingbuddy.exceptions.objects;

public class ReportFacadeException extends BillingBuddyException {

	private static final long serialVersionUID = 2864947795549718531L;

	public ReportFacadeException(Exception e) {
		super(e);
	}

	public ReportFacadeException(String message) {
		super(message);
	}

	public ReportFacadeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReportFacadeException(Throwable cause) {
		super(cause);
	}
}
