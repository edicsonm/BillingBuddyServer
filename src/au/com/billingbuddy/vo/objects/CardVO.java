package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class CardVO extends VO implements Serializable{

	private static final long serialVersionUID = 587132337119064926L;
	private String id;
	private String customerId;
	private String merchantCustomerId;
	private String merchantCustomerCardId;
	private String bin;
	private String cardIdStripe;
	private String brand;
	private String expMonth;
	private String expYear;
	
	private String fingerPrint;
	private String funding;
	private String number;
	private String cvv;
	private String name;
	private String last4;
	private String addressCity;
	private String addressCountry;
	private String addresState;
	private String addressLine1;
	private String addressLine2;
	private String addressZip;
	private String country;
	
	private String blackListed;
	private String blackListedReason;
	private String blackListedCreateTime;
	private String registrationTime;
	
	private CustomerVO customerVO;
	private MerchantCustomerVO merchantCustomerVO;
	private MerchantCustomerCardVO merchantCustomerCardVO;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getCardIdStripe() {
		return cardIdStripe;
	}

	public void setCardIdStripe(String cardIdStripe) {
		this.cardIdStripe = cardIdStripe;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	public String getExpYear() {
		return expYear;
	}

	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}

	public String getFingerPrint() {
		return fingerPrint;
	}

	public void setFingerPrint(String fingerPrint) {
		this.fingerPrint = fingerPrint;
	}

	public String getFunding() {
		return funding;
	}

	public void setFunding(String funding) {
		this.funding = funding;
	}

	public String getLast4() {
		return last4;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddresState() {
		return addresState;
	}

	public void setAddresState(String addresState) {
		this.addresState = addresState;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressZip() {
		return addressZip;
	}

	public void setAddressZip(String addressZip) {
		this.addressZip = addressZip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getBlackListed() {
		return blackListed;
	}

	public void setBlackListed(String blackListed) {
		this.blackListed = blackListed;
	}

	public String getBlackListedReason() {
		return blackListedReason;
	}

	public void setBlackListedReason(String blackListedReason) {
		this.blackListedReason = blackListedReason;
	}

	public String getBlackListedCreateTime() {
		return blackListedCreateTime;
	}

	public void setBlackListedCreateTime(String blackListedCreateTime) {
		this.blackListedCreateTime = blackListedCreateTime;
	}

	public String getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(String registrationTime) {
		this.registrationTime = registrationTime;
	}

	public CustomerVO getCustomerVO() {
		return customerVO;
	}

	public void setCustomerVO(CustomerVO customerVO) {
		this.customerVO = customerVO;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddressCountry() {
		return addressCountry;
	}

	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}
	
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof CardVO))
			return false;
		CardVO objetoVO = (CardVO) obj;
		return (this.getId().equalsIgnoreCase(objetoVO.getId()));
    }

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public String getMerchantCustomerId() {
		return merchantCustomerId;
	}

	public void setMerchantCustomerId(String merchantCustomerId) {
		this.merchantCustomerId = merchantCustomerId;
	}

	public MerchantCustomerVO getMerchantCustomerVO() {
		return merchantCustomerVO;
	}

	public void setMerchantCustomerVO(MerchantCustomerVO merchantCustomerVO) {
		this.merchantCustomerVO = merchantCustomerVO;
	}

	public MerchantCustomerCardVO getMerchantCustomerCardVO() {
		return merchantCustomerCardVO;
	}

	public void setMerchantCustomerCardVO(
			MerchantCustomerCardVO merchantCustomerCardVO) {
		this.merchantCustomerCardVO = merchantCustomerCardVO;
	}

	public String getMerchantCustomerCardId() {
		return merchantCustomerCardId;
	}

	public void setMerchantCustomerCardId(String merchantCustomerCardId) {
		this.merchantCustomerCardId = merchantCustomerCardId;
	}
	

}
