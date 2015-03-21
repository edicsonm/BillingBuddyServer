package au.com.billingbuddy.vo.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class MerchantVO extends VO implements Serializable {

	private static final long serialVersionUID = -3463623290868877516L;

	private String id;
	
	private String businessTypeId;
	private String industryId;
	private String countryNumericMerchant;
	private String name;
	private String status;
	private String createTime;
	private String tradingName;
	private String legalPhysicalAddress;
	private String statementAddress;
	private String taxFileNumber;
	private String cityBusinessInformation;
	private String postCodeBusinessInformation;
	private String issuedBusinessID;
	private String issuedPersonalID;
	private String typeAccountApplication;
	private String estimatedAnnualSales;
	
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String faxNumber;
	private String emailAddress;
	private String alternateEmailAddress;
	private String cityPersonalInformation;
	private String postCodePersonalInformation;
	private String countryNumericPersonalInformation;
	
	private CountryVO countryVOBusiness;
	private CountryVO countryVOPersonalInformation;
	
	private BusinessTypeVO businessTypeVO;
	private IndustryVO industryVO;
	
	private MerchantConfigurationVO merchantConfigurationVO;
	private ArrayList<MerchantRestrictionVO> listMerchantRestrictionsVO;
	
	private String timeUnit;
	private String amountTransactions;
	private String numberTransactions;
	private String since;
	private String to;
	
	public MerchantVO() {
	}
	
	public MerchantVO(String userId) {
		setUserId(userId);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MerchantConfigurationVO getMerchantConfigurationVO() {
		return merchantConfigurationVO;
	}

	public ArrayList<MerchantRestrictionVO> getListMerchantRestrictionsVO() {
		return listMerchantRestrictionsVO;
	}

	public void setMerchantConfigurationVO(
			MerchantConfigurationVO merchantConfigurationVO) {
		this.merchantConfigurationVO = merchantConfigurationVO;
	}

	public void setListMerchantRestrictionsVO(
			ArrayList<MerchantRestrictionVO> listMerchantRestrictionsVO) {
		this.listMerchantRestrictionsVO = listMerchantRestrictionsVO;
	}

	public String getAmountTransactions() {
		return amountTransactions;
	}

	public String getNumberTransactions() {
		return numberTransactions;
	}

	public String getSince() {
		return since;
	}

	public String getTo() {
		return to;
	}

	public void setAmountTransactions(String amountTransactions) {
		this.amountTransactions = amountTransactions;
	}

	public void setNumberTransactions(String numberTransactions) {
		this.numberTransactions = numberTransactions;
	}

	public void setSince(String since) {
		this.since = since;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	public String getCountryNumericMerchant() {
		return countryNumericMerchant;
	}

	public void setCountryNumericMerchant(String countryNumericMerchant) {
		this.countryNumericMerchant = countryNumericMerchant;
	}

	public String getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(String businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getIndustryId() {
		return industryId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTradingName() {
		return tradingName;
	}

	public void setTradingName(String tradingName) {
		this.tradingName = tradingName;
	}

	public String getLegalPhysicalAddress() {
		return legalPhysicalAddress;
	}

	public void setLegalPhysicalAddress(String legalPhysicalAddress) {
		this.legalPhysicalAddress = legalPhysicalAddress;
	}

	public String getStatementAddress() {
		return statementAddress;
	}

	public void setStatementAddress(String statementAddress) {
		this.statementAddress = statementAddress;
	}

	public String getTaxFileNumber() {
		return taxFileNumber;
	}

	public void setTaxFileNumber(String taxFileNumber) {
		this.taxFileNumber = taxFileNumber;
	}

	public String getCityBusinessInformation() {
		return cityBusinessInformation;
	}

	public void setCityBusinessInformation(String cityBusinessInformation) {
		this.cityBusinessInformation = cityBusinessInformation;
	}

	public String getPostCodeBusinessInformation() {
		return postCodeBusinessInformation;
	}

	public void setPostCodeBusinessInformation(String postCodeBusinessInformation) {
		this.postCodeBusinessInformation = postCodeBusinessInformation;
	}

	public String getIssuedBusinessID() {
		return issuedBusinessID;
	}

	public void setIssuedBusinessID(String issuedBusinessID) {
		this.issuedBusinessID = issuedBusinessID;
	}

	public String getIssuedPersonalID() {
		return issuedPersonalID;
	}

	public void setIssuedPersonalID(String issuedPersonalID) {
		this.issuedPersonalID = issuedPersonalID;
	}

	public String getTypeAccountApplication() {
		return typeAccountApplication;
	}

	public void setTypeAccountApplication(String typeAccountApplication) {
		this.typeAccountApplication = typeAccountApplication;
	}

	public String getEstimatedAnnualSales() {
		return estimatedAnnualSales;
	}

	public void setEstimatedAnnualSales(String estimatedAnnualSales) {
		this.estimatedAnnualSales = estimatedAnnualSales;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getAlternateEmailAddress() {
		return alternateEmailAddress;
	}

	public void setAlternateEmailAddress(String alternateEmailAddress) {
		this.alternateEmailAddress = alternateEmailAddress;
	}

	public String getCityPersonalInformation() {
		return cityPersonalInformation;
	}

	public void setCityPersonalInformation(String cityPersonalInformation) {
		this.cityPersonalInformation = cityPersonalInformation;
	}

	public String getCountryNumericPersonalInformation() {
		return countryNumericPersonalInformation;
	}

	public void setCountryNumericPersonalInformation(
			String countryNumericPersonalInformation) {
		this.countryNumericPersonalInformation = countryNumericPersonalInformation;
	}

	public String getPostCodePersonalInformation() {
		return postCodePersonalInformation;
	}

	public void setPostCodePersonalInformation(String postCodePersonalInformation) {
		this.postCodePersonalInformation = postCodePersonalInformation;
	}

	public CountryVO getCountryVOBusiness() {
		return countryVOBusiness;
	}

	public void setCountryVOBusiness(CountryVO countryVOBusiness) {
		this.countryVOBusiness = countryVOBusiness;
	}

	public CountryVO getCountryVOPersonalInformation() {
		return countryVOPersonalInformation;
	}

	public void setCountryVOPersonalInformation(
			CountryVO countryVOPersonalInformation) {
		this.countryVOPersonalInformation = countryVOPersonalInformation;
	}

	public BusinessTypeVO getBusinessTypeVO() {
		return businessTypeVO;
	}

	public void setBusinessTypeVO(BusinessTypeVO businessTypeVO) {
		this.businessTypeVO = businessTypeVO;
	}

	public IndustryVO getIndustryVO() {
		return industryVO;
	}

	public void setIndustryVO(IndustryVO industryVO) {
		this.industryVO = industryVO;
	}


}
