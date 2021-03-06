package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class CountryBlockListVO extends VO implements Serializable {

	private static final long serialVersionUID = -3353703843150920798L;
	private String id;
	private String countryId;
	private String transaction;
	private String merchantServerLocation;
	private String merchantRegistrationLocation;
	private String creditCardIssueLocation;
	private String creditCardHolderLocation;
	
	private CountryVO countryVO;
	
	public CountryBlockListVO(){
	}
	
	public CountryBlockListVO(String numeric){
		this.setCountryVO(new CountryVO());
		this.getCountryVO().setNumeric(numeric);
	}
	
	public String getId() {
		return id;
	}
	public String getCountryId() {
		return countryId;
	}
	public String getTransaction() {
		return transaction;
	}
	public String getMerchantServerLocation() {
		return merchantServerLocation;
	}
	public String getMerchantRegistrationLocation() {
		return merchantRegistrationLocation;
	}
	public String getCreditCardIssueLocation() {
		return creditCardIssueLocation;
	}
	public String getCreditCardHolderLocation() {
		return creditCardHolderLocation;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}
	public void setMerchantServerLocation(String merchantServerLocation) {
		this.merchantServerLocation = merchantServerLocation;
	}
	public void setMerchantRegistrationLocation(String merchantRegistrationLocation) {
		this.merchantRegistrationLocation = merchantRegistrationLocation;
	}
	public void setCreditCardIssueLocation(String creditCardIssueLocation) {
		this.creditCardIssueLocation = creditCardIssueLocation;
	}
	public void setCreditCardHolderLocation(String creditCardHolderLocation) {
		this.creditCardHolderLocation = creditCardHolderLocation;
	}
	public CountryVO getCountryVO() {
		return countryVO;
	}
	public void setCountryVO(CountryVO countryVO) {
		this.countryVO = countryVO;
	}
	
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof CountryBlockListVO))
			return false;
		CountryBlockListVO objetoVO = (CountryBlockListVO) obj;
		return (this.getCountryVO().getNumeric().equalsIgnoreCase(objetoVO.getCountryVO().getNumeric()));
    }

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
