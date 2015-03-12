package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class IndustryVO extends VO implements Serializable {
	
	private static final long serialVersionUID = -2882942875814462644L;
	private String id;
	private String description;
	private String creationTime;
	private String status;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
