package au.com.billingbuddy.exceptions.objects;


public class MerchantDAOException extends BillingBuddyException{

	private static final long serialVersionUID = 121031465390765747L;

	public MerchantDAOException(Exception e) {
		super(e);
	}

	public MerchantDAOException(String message) {
		super(message);
	}

	public MerchantDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public MerchantDAOException(Throwable cause) {
		super(cause);
	}
}
