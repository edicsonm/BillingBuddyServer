package au.com.billingbuddy.exceptions.objects;

public class ReporteAmountByDayException extends BillingBuddyException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8786813833021528543L;

	public ReporteAmountByDayException(Exception e) {
		super(e);
	}

	public ReporteAmountByDayException(String message) {
		super(message);
	}

	public ReporteAmountByDayException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReporteAmountByDayException(Throwable cause) {
		super(cause);
	}
	
	
}