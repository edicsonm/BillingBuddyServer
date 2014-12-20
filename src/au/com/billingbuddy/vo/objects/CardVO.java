package au.com.billingbuddy.vo.objects;

import au.com.billingbuddy.common.objects.ValidateData;
import au.com.billingbuddy.exceptions.objects.DataSanitizeException;

public class CardVO {

	private String id;
	private String name;
	private String bin;
	private String cardNumber;
	private String cvv;
	private String expYear;
	private String expMonth;
	private String address;
	private String blackListed;
	private String blackListedReason;
	private String blackListedCreateTime;
	private String registrationTime;
	private String customerId;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getBin() {
		return bin;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getExpYear() {
		return expYear;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public String getAddress() {
		return address;
	}

	public String getBlackListed() {
		return blackListed;
	}

	public String getBlackListedReason() {
		return blackListedReason;
	}

	public String getBlackListedCreateTime() {
		return blackListedCreateTime;
	}

	public String getRegistrationTime() {
		return registrationTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) throws DataSanitizeException {
		ValidateData.validate(name, ValidateData.NAME);
		this.name = name;
	}

	public void setBin(String bin) throws DataSanitizeException {
		ValidateData.validate(bin, ValidateData.BIN);
		this.bin = bin;
	}

	public void setCardNumber(String cardNumber) throws DataSanitizeException {
		ValidateData.validate(cardNumber, ValidateData.CARDNUMBER);
		this.cardNumber = cardNumber;
	}

	public void setExpYear(String expYear) throws DataSanitizeException {
		ValidateData.validate(expYear, ValidateData.YEAR);
		this.expYear = expYear;
	}

	public void setExpMonth(String expMonth) throws DataSanitizeException {
		ValidateData.validate(expMonth, ValidateData.MONTH);
		this.expMonth = expMonth;
	}
	
	public void setCvv(String cvv)  throws DataSanitizeException {
		ValidateData.validate(cvv, ValidateData.CVC);
		this.cvv = cvv;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public void setBlackListed(String blackListed) {
		this.blackListed = blackListed;
	}

	public void setBlackListedReason(String blackListedReason) {
		this.blackListedReason = blackListedReason;
	}

	public void setBlackListedCreateTime(String blackListedCreateTime) {
		this.blackListedCreateTime = blackListedCreateTime;
	}

	public void setRegistrationTime(String registrationTime) {
		this.registrationTime = registrationTime;
	}

	public String getCvv() {
		return cvv;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
