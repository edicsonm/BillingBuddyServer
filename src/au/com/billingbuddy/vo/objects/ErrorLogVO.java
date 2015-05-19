package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class ErrorLogVO extends VO implements Serializable {
	
	private static final long serialVersionUID = 2006202121309750081L;
	
	private String id;
	private String processName;
	private String type;
	private String information;
	private String creationTime;
	private String Status;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
}
