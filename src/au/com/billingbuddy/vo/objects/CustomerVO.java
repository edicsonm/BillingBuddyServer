package au.com.billingbuddy.vo.objects;

import java.io.Serializable;


public class CustomerVO implements Serializable {
	
//	private String id;
//	private String email;
//	private String phone;
//	private String username;
//	private String password;
	
	private static final long serialVersionUID = 5929911480071411063L;
	private String id;
	private String createTime;
	private String email;
	private String phoneNumber;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
