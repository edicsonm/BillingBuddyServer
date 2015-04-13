package au.com.billingbuddy.vo.objects;

public class VO {
	
	private String userId;
	private String errorCode;
	private String status;
	private String creationTime;
	private String message;
	private String data;
	
	private String initialDateReport;
	private String finalDateReport;
	
	private String match;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getInitialDateReport() {
		return initialDateReport;
	}

	public void setInitialDateReport(String initialDateReport) {
		this.initialDateReport = initialDateReport;
	}

	public String getFinalDateReport() {
		return finalDateReport;
	}

	public void setFinalDateReport(String finalDateReport) {
		this.finalDateReport = finalDateReport;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
}
