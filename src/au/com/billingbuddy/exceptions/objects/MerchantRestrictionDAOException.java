package au.com.billingbuddy.exceptions.objects;


public class MerchantRestrictionDAOException extends BillingBuddyException{

	private static final long serialVersionUID = -1390122858484512396L;

	public MerchantRestrictionDAOException(Exception e) {
		super(e);
	}

	public MerchantRestrictionDAOException(String message) {
		super(message);
	}

	public MerchantRestrictionDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public MerchantRestrictionDAOException(Throwable cause) {
		super(cause);
	}
}
