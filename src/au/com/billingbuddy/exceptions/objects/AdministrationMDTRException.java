package au.com.billingbuddy.exceptions.objects;

public class AdministrationMDTRException extends BillingBuddyException {
	
	private static final long serialVersionUID = -557467664794321527L;

	public AdministrationMDTRException(Exception e, String ... attributes) {
		super(e, attributes);
//		ErrorManager.manageErrorProcessor(attributes);
	}
	
	public AdministrationMDTRException(BillingBuddySQLException e) {
		super(e);
//		try {
//			JSONObject jSONObject = new JSONObject();
//			jSONObject.put("ErrorDetails", e.getErrorDetails());
//			ErrorLogVO errorLogVO = new ErrorLogVO();
//			errorLogVO.setProcessName(this.getClass().getSimpleName());
//			errorLogVO.setInformation(jSONObject.toJSONString());
//			ErrorLogDAO errorLogDAO = new ErrorLogDAO();
//			if(errorLogDAO.insert(errorLogVO) == 0){
////				System.out.println("Genera archivo de error " + e.hashCode());
//				String fileName = this.getClass().getCanonicalName() +"_"+ e.hashCode();
//				ErrorManager.logDailySubscriptionErrorFile(fileName, jSONObject);
//			}else{
////				System.out.println("Guarda el error del DAO en la tabla de los errores: "  + e.hashCode());
//			}
//		} catch (ErrorLogDAOException e1) {
//			System.out.println("Sale por aca 1 " + e1.getMessage());
//		} catch (MySQLConnectionException e1) {
//			System.out.println("Sale por aca 2 " + e1.getMessage());
//		}
	}
	
	public AdministrationMDTRException(BillingBuddySQLException e, String call) {
		super(e, call);
//		try {
//			e.getErrorDetails().put("CALL", call);
//			JSONObject jSONObject = new JSONObject();
//			jSONObject.put("ErrorDetails", e.getErrorDetails());
//			ErrorLogVO errorLogVO = new ErrorLogVO();
//			errorLogVO.setProcessName(this.getClass().getSimpleName());
//			errorLogVO.setInformation(jSONObject.toJSONString());
//			ErrorLogDAO errorLogDAO = new ErrorLogDAO();
//			if(errorLogDAO.insert(errorLogVO) == 0){
////				System.out.println("Genera archivo de error " + e.hashCode());
//				String fileName = this.getClass().getCanonicalName() +"_"+ e.hashCode();
//				ErrorManager.logDailySubscriptionErrorFile(fileName, jSONObject);
//			}else{
////				System.out.println("Guarda el error del DAO en la tabla de los errores: "  + e.hashCode());
//			}
//		} catch (ErrorLogDAOException e1) {
//			System.out.println("Sale por aca 1 " + e1.getMessage());
//		} catch (MySQLConnectionException e1) {
//			System.out.println("Sale por aca 2 " + e1.getMessage());
//		}
	}

	public AdministrationMDTRException(String message) {
		super(message);
	}

	public AdministrationMDTRException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdministrationMDTRException(Throwable cause) {
		super(cause);
	}
}
