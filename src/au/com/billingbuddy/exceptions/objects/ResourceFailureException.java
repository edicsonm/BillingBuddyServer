package au.com.billingbuddy.exceptions.objects;

public class ResourceFailureException extends BillingBuddyException {
	
	private static final long serialVersionUID = -4419111830906761725L;

	private String codigoError;
	private String mensajeError;

	public ResourceFailureException(BillingBuddyException e) {
		super(e);
		String tagG = this.getClass().getSuperclass().getSimpleName();
		String tagE = this.getClass().getSimpleName() + ".1";
		System.out.println("tagG: " + tagG);
		System.out.println("tagE: " + tagE);
		
	}

	public ResourceFailureException(String message) {
		super(message);
	}

	public ResourceFailureException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceFailureException(Throwable cause) {
		super(cause);
	}

	public String getCodigoError() {
		return codigoError;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	
}
