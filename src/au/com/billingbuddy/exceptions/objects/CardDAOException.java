package au.com.billingbuddy.exceptions.objects;


public class CardDAOException extends BillingBuddyException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1019344580544262578L;

	public CardDAOException(Exception e) {
		super(e);
	}

	public CardDAOException(String message) {
		super(message);
	}

	public CardDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public CardDAOException(Throwable cause) {
		super(cause);
	}
}
