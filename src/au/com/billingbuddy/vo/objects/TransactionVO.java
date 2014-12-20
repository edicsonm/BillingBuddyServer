package au.com.billingbuddy.vo.objects;

import au.com.billingbuddy.common.objects.ValidateData;
import au.com.billingbuddy.exceptions.objects.DataSanitizeException;

public class TransactionVO  extends VO {

	private String id;
	private String orderNumber;
	private String orderCurrency;
	private String merchantId;
	private String txnType;
	private String userAgent;
	private String acceptLanguage;
	private String customerId;
	private String cardId;
	private String billingAddressCity;
	private String billingAddressRegion;
	private String billingAddressPostal;
	private String billingAddressCountry;
	private String shippingAddressId;
	private String product;
	private String quantity;
	private String rate;
	private String orderAmount;
	private String companyName;
	private String ip;
	private String sessionId;
	private String domain;
	
	private String riskScoreValue;
	private boolean riskScore;
	
	private CardVO cardVO;
	private CustomerVO customerVO;
	private ShippingAddressVO shippingAddressVO;
	
	public TransactionVO(){}
	
	public String getId() {
		return id;
	}

	public String getOrderCurrency() {
		return orderCurrency;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public String getTxnType() {
		return txnType;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getCardId() {
		return cardId;
	}

	public String getBillingAddressCity() {
		return billingAddressCity;
	}

	public String getBillingAddressRegion() {
		return billingAddressRegion;
	}

	public String getBillingAddressPostal() {
		return billingAddressPostal;
	}

	public String getBillingAddressCountry() {
		return billingAddressCountry;
	}

	public String getShippingAddressId() {
		return shippingAddressId;
	}

	public String getProduct() {
		return product;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getRate() {
		return rate;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getIp() {
		return ip;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getDomain() {
		return domain;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOrderCurrency(String orderCurrency) {
		this.orderCurrency = orderCurrency;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public void setBillingAddressCity(String billingAddressCity) throws DataSanitizeException {
		ValidateData.validate(billingAddressCity, ValidateData.NAME);
		this.billingAddressCity = billingAddressCity;
	}

	public void setBillingAddressRegion(String billingAddressRegion) throws DataSanitizeException {
		ValidateData.validate(billingAddressRegion, ValidateData.NAME);
		this.billingAddressRegion = billingAddressRegion;
	}

	public void setBillingAddressPostal(String billingAddressPostal) throws DataSanitizeException {
		ValidateData.validate(billingAddressPostal, ValidateData.DECIMALNUMERIC);
		this.billingAddressPostal = billingAddressPostal;
	}

	public void setBillingAddressCountry(String billingAddressCountry) throws DataSanitizeException {
		ValidateData.validate(billingAddressCountry, ValidateData.COUNTRY);
		this.billingAddressCountry = billingAddressCountry;
	}

	public void setShippingAddressId(String shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public void setQuantity(String quantity) throws DataSanitizeException {
		ValidateData.validate(quantity, ValidateData.DECIMALNUMERIC);
		this.quantity = quantity;
	}

	public void setRate(String rate) throws DataSanitizeException {
		ValidateData.validate(rate, ValidateData.DECIMALNUMERIC);
		this.rate = rate;
	}

	public void setOrderAmount(String orderAmount) throws DataSanitizeException {
		ValidateData.validate(orderAmount, ValidateData.DECIMALNUMERIC);
		this.orderAmount = orderAmount;
	}

	public void setCompanyName(String companyName) throws DataSanitizeException {
		ValidateData.validate(companyName, ValidateData.STRING);
		this.companyName = companyName;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setDomain(String domain) throws DataSanitizeException {
		ValidateData.validate(domain, ValidateData.DOMAIN);
		this.domain = domain;
	}

	public CardVO getCardVO() {
		return cardVO;
	}

	public void setCardVO(CardVO cardVO) {
		this.cardVO = cardVO;
	}

	public CustomerVO getCustomerVO() {
		return customerVO;
	}

	public void setCustomerVO(CustomerVO customerVO) {
		this.customerVO = customerVO;
	}

	public ShippingAddressVO getShippingAddressVO() {
		return shippingAddressVO;
	}

	public void setShippingAddressVO(ShippingAddressVO shippingAddressVO) {
		this.shippingAddressVO = shippingAddressVO;
	}

	public String getRiskScoreValue() {
		return riskScoreValue;
	}

	public void setRiskScoreValue(String riskScoreValue) {
		this.riskScoreValue = riskScoreValue;
	}

	public boolean isRiskScore() {
		return riskScore;
	}

	public void setRiskScore(boolean riskScore) {
		this.riskScore = riskScore;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

}
