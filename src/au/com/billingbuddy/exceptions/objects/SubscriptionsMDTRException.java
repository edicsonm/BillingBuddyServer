package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import au.com.billigbuddy.utils.ErrorManager;
import au.com.billingbuddy.dao.objects.ErrorLogDAO;
import au.com.billingbuddy.vo.objects.ErrorLogVO;

public class SubscriptionsMDTRException extends BillingBuddyException {

	private static final long serialVersionUID = 2920911009582428078L;

	public SubscriptionsMDTRException(Exception e, String ... attributes) {
		super(e);
		ErrorManager.manageErrorProcessor(attributes);
	}
	
	public SubscriptionsMDTRException(BillingBuddySQLException e) {
		super(e);
		try {
			JSONObject jSONObject = new JSONObject();
			jSONObject.put("ErrorDetails", e.getErrorDetails());
			ErrorLogVO errorLogVO = new ErrorLogVO();
			errorLogVO.setProcessName(this.getClass().getSimpleName());
			errorLogVO.setInformation(jSONObject.toJSONString());
			ErrorLogDAO errorLogDAO = new ErrorLogDAO();
			if(errorLogDAO.insert(errorLogVO) == 0){
//				System.out.println("Genera archivo de error " + e.hashCode());
				String fileName = this.getClass().getCanonicalName() +"_"+ e.hashCode();
				ErrorManager.logDailySubscriptionErrorFile(fileName, jSONObject);
			}else{
//				System.out.println("Guarda el error del DAO en la tabla de los errores: "  + e.hashCode());
			}
		} catch (ErrorLogDAOException e1) {
			System.out.println("Sale por aca 1 " + e1.getMessage());
		} catch (MySQLConnectionException e1) {
			System.out.println("Sale por aca 2 " + e1.getMessage());
		}
	}
	
	public SubscriptionsMDTRException(BillingBuddySQLException e, String call) {
		super(e);
		try {
			e.getErrorDetails().put("CALL", call);
			JSONObject jSONObject = new JSONObject();
			jSONObject.put("ErrorDetails", e.getErrorDetails());
			ErrorLogVO errorLogVO = new ErrorLogVO();
			errorLogVO.setProcessName(this.getClass().getSimpleName());
			errorLogVO.setInformation(jSONObject.toJSONString());
			ErrorLogDAO errorLogDAO = new ErrorLogDAO();
			if(errorLogDAO.insert(errorLogVO) == 0){
//				System.out.println("Genera archivo de error " + e.hashCode());
				String fileName = this.getClass().getCanonicalName() +"_"+ e.hashCode();
				ErrorManager.logDailySubscriptionErrorFile(fileName, jSONObject);
			}else{
//				System.out.println("Guarda el error del DAO en la tabla de los errores: "  + e.hashCode());
			}
		} catch (ErrorLogDAOException e1) {
			System.out.println("Sale por aca 1 " + e1.getMessage());
		} catch (MySQLConnectionException e1) {
			System.out.println("Sale por aca 2 " + e1.getMessage());
		}
	}

	public SubscriptionsMDTRException(String message) {
		super(message);
	}

	public SubscriptionsMDTRException(String message, Throwable cause) {
		super(message, cause);
	}

	public SubscriptionsMDTRException(Throwable cause) {
		super(cause);
	}
	
	
}