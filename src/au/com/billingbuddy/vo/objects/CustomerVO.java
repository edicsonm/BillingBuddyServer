package au.com.billingbuddy.vo.objects;

import au.com.billingbuddy.common.objects.ValidateData;
import au.com.billingbuddy.exceptions.objects.DataSanitizeException;

public class CustomerVO {
	
	private String id;
	private String email;
	private String phone;
	private String username;
	private String password;

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) throws DataSanitizeException {
		ValidateData.validate(email, ValidateData.EMAIL);
		this.email = email;
	}

	public void setUsername(String username) throws DataSanitizeException {
		ValidateData.validate(username, ValidateData.NAME);
		this.username = username;
	}

	public void setPassword(String password) throws DataSanitizeException {
		ValidateData.validate(password, ValidateData.STRING);
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
