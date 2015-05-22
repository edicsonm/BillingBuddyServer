package au.com.billingbuddy.exceptions.objects;

import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import au.com.billigbuddy.utils.ErrorManager;
import au.com.billingbuddy.common.objects.MySQLError;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.dao.objects.ErrorLogDAO;
import au.com.billingbuddy.exceptions.interfaces.IException;
import au.com.billingbuddy.vo.objects.ErrorLogVO;

public class BillingBuddySQLException extends SQLException implements IException {

	private static final long serialVersionUID = 886765645402562172L;
	private String componentOrigen;
	private String errorMenssage;
	private String errorLevel;
	private String value;
	private String sqlObjectName;
	
	private JSONObject errorDetails = new JSONObject();
	
	public BillingBuddySQLException(SQLException e) {
		super(e);
//		e.printStackTrace();
//		String tagG = this.getClass().getSuperclass().getSimpleName();
//		String tagE = this.getClass().getSimpleName();
//		System.out.println("tagG: " + tagG);
//		System.out.println("tagE: " + tagE);
//		
//		tagG = this.getStackTrace()[0].getClassName();
//		int sz = this.getStackTrace()[0].getClassName().split("\\.").length;
//		System.out.println("tagG: " + tagG);
//		try {
			
//			System.out.println("e.getMessage(): " + e.getMessage());
//			System.out.println("e.getErrorCode(): " + e.getErrorCode());
//			System.out.println("e.getSQLState(): " + e.getSQLState());
			
//			System.out.println("e.getCause().toString() 3 : "+this.getClass().getCanonicalName());
//			System.out.println("e.getCause().toString() 4 : "+this.getClass().getSimpleName());
//			System.out.println("e.getCause().toString() 5 : "+this.getClass().getSuperclass().getSimpleName());
			
			JSONArray arrayAttributes = new JSONArray();
			JSONObject attributeDetails = new JSONObject();
			StackTraceElement[] stackTraceElements = e.getStackTrace();
			for (int i = 0; i < stackTraceElements.length; i++) {
				StackTraceElement stackTraceElement = stackTraceElements[i];
				attributeDetails.put(i, stackTraceElement.toString());
			}
			
			errorDetails.put("Message", e.getMessage());
			errorDetails.put("ErrorCode", e.getErrorCode());
			errorDetails.put("SQLState", e.getSQLState());
			errorDetails.put("ClassName", this.getClass().getCanonicalName());
//			errorDetails.put("Detail", e.toString());
			errorDetails.put("Details", attributeDetails);
	
			JSONObject jSONObject = new JSONObject();
			jSONObject.put("ErrorDetails", errorDetails);
			
//			ErrorLogVO errorLogVO = new ErrorLogVO();
//			errorLogVO.setProcessName(this.getClass().getSimpleName());
//			errorLogVO.setInformation(jSONObject.toJSONString());
//			ErrorLogDAO errorLogDAO = new ErrorLogDAO();
//			if(errorLogDAO.insert(errorLogVO) == 0){
//				System.out.println("Genera archivo de error " + e.hashCode());
//				String fileName = this.getClass().getCanonicalName() +"_"+ e.hashCode();
//				ErrorManager.logDailySubscriptionErrorFile(fileName, jSONObject);
//			}else{
//				System.out.println("Guarda el error del DAO en la tabla de los errores: "  +  e.hashCode());
//			}
		
			MySQLError mySQLError = Utilities.extractSQLError(e.getMessage(), e.getErrorCode());
			if(mySQLError != null){
				setValue(mySQLError.getValue());
				setSqlObjectName(mySQLError.getSqlObjectName());
			}
//		} catch (ErrorLogDAOException e1) {
//			System.out.println("Sale por aca 1 " + e1.getMessage());
//		} catch (MySQLConnectionException e1) {
//			System.out.println("Sale por aca 2 " + e1.getMessage());
//		}
	}
		
	public BillingBuddySQLException(String message) {
		super(message);
		// gestorArchivosProp = GestorArchivosProp.obtenerInstancia();
		// ParamsConfigApp configuracion =
		// gestorArchivosProp.getParamsConfigAppInstance();
		// this.mapaExcepciones = configuracion.getExceptionIds();
	}

	public BillingBuddySQLException(String message, Throwable cause) {
		super(message, cause);
		// gestorArchivosProp = GestorArchivosProp.obtenerInstancia();
		// ParamsConfigApp configuracion =
		// gestorArchivosProp.getParamsConfigAppInstance();
		// this.mapaExcepciones = configuracion.getExceptionIds();
	}

	public BillingBuddySQLException(Throwable cause) {
		super(cause);
//		System.out.println("1:" + this.getClass().getSuperclass().getSimpleName());
//		System.out.println("2:" + this.getClass().getSimpleName() + ".1");
//		System.out.println("3:" + this.getStackTrace()[0].getClassName());
	}

	public String getComponentOrigen() {
		return componentOrigen;
	}

	public void setComponentOrigen(String componentOrigen) {
		this.componentOrigen = componentOrigen;
	}

	public String getErrorMenssage() {
		return errorMenssage;
	}

	public void setErrorMenssage(String errorMenssage) {
		this.errorMenssage = errorMenssage;
	}

	public String getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(String errorLevel) {
		this.errorLevel = errorLevel;
	}

	public String getValue() {
		return value;
	}

	public String getSqlObjectName() {
		return sqlObjectName;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setSqlObjectName(String sqlObjectName) {
		this.sqlObjectName = sqlObjectName;
	}

	public JSONObject getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(JSONObject errorDetails) {
		this.errorDetails = errorDetails;
	}

	// private GestorArchivosProp gestorArchivosProp;
	// protected HashMap<String, String> mapaExcepciones;
	// private static final long serialVersionUID = -656908117024131416L;
	// private String errorCode;
	// private String errorMessage;
	// private String className;
	// private String classMethod;
	// private String classLine;
	//
	// public BillingBuddyException(Exception e) {
	// super(e);
	// // System.out.println("1:" +
	// this.getClass().getSuperclass().getSimpleName());
	// // System.out.println("2:" + this.getClass().getSimpleName() + ".1");
	// // System.out.println("3:" + this.getStackTrace()[0].getClassName());
	// this.setErrorMessage(this.getErrorCode() != null ?
	// ConfigurationApplication.getKey(this.getClass().getSimpleName() +
	// this.getErrorCode()) : "N/E");
	// // if(this.getErrorCode() != null){
	// // System.out.println("Valor a cargar: " +
	// (this.getClass().getSimpleName() + this.getErrorCode()));
	// //
	// this.setErrorMessage(ConfigurationApplication.getKey(this.getClass().getSimpleName()
	// + this.getErrorCode()));
	// // }
	// // this.setErrorMessage(this.getErrorCode() != null ?
	// ConfigurationApplication.getKey(this.getClass().getSimpleName() +
	// this.getErrorCode()) : "N/E");
	// // String tagG = this.getClass().getSuperclass().getSimpleName();
	// // String tagE = this.getClass().getSimpleName() + ".1";
	// // tagG = tagG + ".1";
	// // tagG = this.getStackTrace()[0].getClassName();
	// // int sz = this.getStackTrace()[0].getClassName().split("\\.").length;
	// // System.out.println("class:\t" + tagG.split("\\.")[sz - 1] + "\n"
	// // + "method:\t" + this.getStackTrace()[0].getMethodName() + "\n"
	// // + "line:\t" + this.getStackTrace()[0].getLineNumber());
	// //
	// this.setErrorMessage(ConfigurationApplication.getKey(this.getClass().getSimpleName()
	// + this.getErrorCode()));
	// // this.setClassName(this.getStackTrace()[0].getClassName());
	// // this.setClassMethod(this.getStackTrace()[0].getMethodName());
	// //
	// this.setClassLine(String.valueOf(this.getStackTrace()[0].getLineNumber()));
	// }
	//
	// public BillingBuddyException(String message) {
	// super(message);
	// System.out.println("1:" +
	// this.getClass().getSuperclass().getSimpleName());
	// System.out.println("2:" + this.getClass().getSimpleName() + ".1");
	// System.out.println("3:" + this.getStackTrace()[0].getClassName());
	// }
	//
	// public BillingBuddyException(String message, Throwable cause) {
	// super(message, cause);
	// System.out.println("1:" +
	// this.getClass().getSuperclass().getSimpleName());
	// System.out.println("2:" + this.getClass().getSimpleName() + ".1");
	// System.out.println("3:" + this.getStackTrace()[0].getClassName());
	// }
	//
	// public BillingBuddyException(Throwable cause) {
	// super(cause);
	// System.out.println("1:" +
	// this.getClass().getSuperclass().getSimpleName());
	// System.out.println("2:" + this.getClass().getSimpleName() + ".1");
	// System.out.println("3:" + this.getStackTrace()[0].getClassName());
	// }
	//
	// public String getErrorCode() {
	// return errorCode;
	// }
	//
	// public void setErrorCode(String errorCode) {
	// this.errorCode = errorCode;
	// }
	//
	// public String getErrorMessage() {
	// return errorMessage;
	// }
	//
	// public void setErrorMessage(String errorMessage) {
	// this.errorMessage = errorMessage;
	// }
	//
	// public String getClassName() {
	// return className;
	// }
	//
	// public void setClassName(String className) {
	// this.className = className;
	// }
	//
	// public String getClassMethod() {
	// return classMethod;
	// }
	//
	// public void setClassMethod(String classMethod) {
	// this.classMethod = classMethod;
	// }
	//
	// public String getClassLine() {
	// return classLine;
	// }
	//
	// public void setClassLine(String classLine) {
	// this.classLine = classLine;
	// }
}
