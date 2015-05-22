package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class SubmittedProcessLogVO extends VO implements Serializable {

	private static final long serialVersionUID = 4366909939764115417L;
	
	private String id;
	private String processName;
	private String startTime;
	private String endTime;
	private String statusProcess;
	private String information;
	private String file;

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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStatusProcess() {
		return statusProcess;
	}

	public void setStatusProcess(String statusProcess) {
		this.statusProcess = statusProcess;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof SubmittedProcessLogVO))
			return false;
		SubmittedProcessLogVO objetoVO = (SubmittedProcessLogVO) obj;
		return (this.getId().equalsIgnoreCase(objetoVO.getId()));
    }

	@Override
	public int hashCode() {
		return super.hashCode();
	}


}
