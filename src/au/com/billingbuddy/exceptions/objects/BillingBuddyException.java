package au.com.billingbuddy.exceptions.objects;

import org.json.simple.JSONObject;

import au.com.billigbuddy.utils.ErrorManager;
import au.com.billingbuddy.dao.objects.ErrorLogDAO;
import au.com.billingbuddy.exceptions.interfaces.IException;
import au.com.billingbuddy.vo.objects.ErrorLogVO;

public class BillingBuddyException extends Exception implements IException {

	private static final long serialVersionUID = 886765645402562172L;
	private String errorCode;
	private String componentOrigen;
	private String errorMenssage;
	private String errorLevel;

	public BillingBuddyException(String message) {
		super(message);
	}

	public BillingBuddyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BillingBuddyException(Exception e, String ... attributes) {
		super(e);
		ErrorManager.manageErrorProcessor(attributes);
	}
	
	public BillingBuddyException(BillingBuddySQLException e) {
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
	
	public BillingBuddyException(BillingBuddySQLException e, String call) {
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

	public BillingBuddyException(Throwable cause) {
		super(cause);
//		System.out.println("1:" + this.getClass().getSuperclass().getSimpleName());
//		System.out.println("2:" + this.getClass().getSimpleName() + ".1");
//		System.out.println("3:" + this.getStackTrace()[0].getClassName());
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
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
