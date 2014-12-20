package au.com.billingbuddy.exceptions.objects;

public class StripeChargeDAOException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public StripeChargeDAOException(Exception e) {
		super(e);
	}

	public StripeChargeDAOException(String message) {
		super(message);
	}

	public StripeChargeDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public StripeChargeDAOException(Throwable cause) {
		super(cause);
	}
}
