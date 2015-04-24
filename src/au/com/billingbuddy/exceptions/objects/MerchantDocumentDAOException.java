package au.com.billingbuddy.exceptions.objects;


public class MerchantDocumentDAOException extends BillingBuddyException{

	private static final long serialVersionUID = -5017968317888918244L;

	public MerchantDocumentDAOException(Exception e) {
		super(e);
	}

	public MerchantDocumentDAOException(String message) {
		super(message);
	}

	public MerchantDocumentDAOException(String message, Throwable cause) {
		super(message, cause);
	}

	public MerchantDocumentDAOException(Throwable cause) {
		super(cause);
	}
}
