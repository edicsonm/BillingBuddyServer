package au.com.billingbuddy.exceptions.objects;

public class ReporteRejectedByDayException extends BillingBuddyException {

	private static final long serialVersionUID = -9041608688574753887L;

	public ReporteRejectedByDayException(Exception e) {
		super(e);
	}

	public ReporteRejectedByDayException(String message) {
		super(message);
	}

	public ReporteRejectedByDayException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReporteRejectedByDayException(Throwable cause) {
		super(cause);
	}
	
	
}