package au.com.billingbuddy.exceptions.objects;

public class ReporteChargesByDayException extends BillingBuddyException {

	private static final long serialVersionUID = 3451947450877544622L;

	public ReporteChargesByDayException(Exception e) {
		super(e);
	}

	public ReporteChargesByDayException(String message) {
		super(message);
	}

	public ReporteChargesByDayException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReporteChargesByDayException(Throwable cause) {
		super(cause);
	}
	
	
}