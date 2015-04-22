package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class TransactionVO  extends VO implements Serializable {
	
	private static final long serialVersionUID = 1242926442168105003L;
	private String id;
	private String ip;
	private String sessionId;
	private String maxmindId;
	private String orderNumber;	
	private String billingAddressCity;
	private String billingAddressRegion;
	private String billingAddressPostal;
	private String billingAddressCountry;
	private String shippingAddress;
	private String shippingAddressCity;
	private String shippingAddressRegion;
	private String shippingAddressPostal;
	private String shippingAddressCountry;
	private String domain;
	private String customerPhone;
	private String emailMD5;
	private String usernameMD5;
	private String bin;
	private String binName;
	private String binPhone;
	private String userAgent;
	private String acceptLanguage;
	private String orderAmount;
	private String orderCurrency;
	private String merchantId;
	private String txnType;
	private String cVVResult;
	private String requestedType;
	private String riskScore;
	private boolean highRiskScore;
	private String countryMatch;
	private String highRiskCountry;
	private String distance;
	private String ipAccuracyRadius;
	private String ipCity;
	private String ipRegion;
	private String ipRegionName;
	private String ipPostalCode;
	private String ipMetroCode;
	private String ipAreaCode;
	private String countryCode;
	private String ipCountryName;
	private String ipContinentCode;
	private String ipLatitude;
	private String ipLongitude;
	private String ipTimeZone;
	private String ipAsnum;
	private String ipUserType;
	private String ipNetSpeedCell;
	private String ipDomain;
	private String ipIsp;
	private String ipOrg;
	private String ipCityConf;
	private String ipRegionConf;
	private String ipPostalConf;
	private String ipCountryConf;
	private String anonymousProxy;
	private String proxyScore;
	private String ipCorporateProxy;
	private String freeMail;
	private String carderEmail;
	private String binMatch;
	private String binCountry;
	private String binNameMatch;
	private String binPhoneMatch;
	private String prepaid;
	private String custPhoneInBillingLoc;
	private String shipForward;
	private String cityPostalMatch;
	private String shipCityPostalMatch;
	private String minfraudVersion;
	private String serviceLevel;
	private String error;
	private String cardId;
	private String processTime;
	private String creationTime;
	
	private String dateReport;
//	private String initialDateReport;
//	private String finalDateReport;
	private String amountDateReport;
	private String totalDateReport;
	
	private ChargeVO chargeVO;
	private CardVO cardVO;
	private MerchantVO merchantVO;
	private RejectedChargeVO rejectedChargeVO;
	
	public TransactionVO(){}
	
	public TransactionVO(String userId) {
		setUserId(userId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getMaxmindId() {
		return maxmindId;
	}

	public void setMaxmindId(String maxmindId) {
		this.maxmindId = maxmindId;
	}

	public String getBillingAddressCity() {
		return billingAddressCity;
	}

	public void setBillingAddressCity(String billingAddressCity) {
		this.billingAddressCity = billingAddressCity;
	}

	public String getBillingAddressRegion() {
		return billingAddressRegion;
	}

	public void setBillingAddressRegion(String billingAddressRegion) {
		this.billingAddressRegion = billingAddressRegion;
	}

	public String getBillingAddressPostal() {
		return billingAddressPostal;
	}

	public void setBillingAddressPostal(String billingAddressPostal) {
		this.billingAddressPostal = billingAddressPostal;
	}

	public String getBillingAddressCountry() {
		return billingAddressCountry;
	}

	public void setBillingAddressCountry(String billingAddressCountry) {
		this.billingAddressCountry = billingAddressCountry;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getShippingAddressCity() {
		return shippingAddressCity;
	}

	public void setShippingAddressCity(String shippingAddressCity) {
		this.shippingAddressCity = shippingAddressCity;
	}

	public String getShippingAddressRegion() {
		return shippingAddressRegion;
	}

	public void setShippingAddressRegion(String shippingAddressRegion) {
		this.shippingAddressRegion = shippingAddressRegion;
	}

	public String getShippingAddressPostal() {
		return shippingAddressPostal;
	}

	public void setShippingAddressPostal(String shippingAddressPostal) {
		this.shippingAddressPostal = shippingAddressPostal;
	}

	public String getShippingAddressCountry() {
		return shippingAddressCountry;
	}

	public void setShippingAddressCountry(String shippingAddressCountry) {
		this.shippingAddressCountry = shippingAddressCountry;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getEmailMD5() {
		return emailMD5;
	}

	public void setEmailMD5(String emailMD5) {
		this.emailMD5 = emailMD5;
	}

	public String getUsernameMD5() {
		return usernameMD5;
	}

	public void setUsernameMD5(String usernameMD5) {
		this.usernameMD5 = usernameMD5;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getBinName() {
		return binName;
	}

	public void setBinName(String binName) {
		this.binName = binName;
	}

	public String getBinPhone() {
		return binPhone;
	}

	public void setBinPhone(String binPhone) {
		this.binPhone = binPhone;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderCurrency() {
		return orderCurrency;
	}

	public void setOrderCurrency(String orderCurrency) {
		this.orderCurrency = orderCurrency;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getcVVResult() {
		return cVVResult;
	}

	public void setcVVResult(String cVVResult) {
		this.cVVResult = cVVResult;
	}

	public String getRequestedType() {
		return requestedType;
	}

	public void setRequestedType(String requestedType) {
		this.requestedType = requestedType;
	}

	public String getCountryMatch() {
		return countryMatch;
	}

	public void setCountryMatch(String countryMatch) {
		this.countryMatch = countryMatch;
	}

	public String getHighRiskCountry() {
		return highRiskCountry;
	}

	public void setHighRiskCountry(String highRiskCountry) {
		this.highRiskCountry = highRiskCountry;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getIpAccuracyRadius() {
		return ipAccuracyRadius;
	}

	public void setIpAccuracyRadius(String ipAccuracyRadius) {
		this.ipAccuracyRadius = ipAccuracyRadius;
	}

	public String getIpCity() {
		return ipCity;
	}

	public void setIpCity(String ipCity) {
		this.ipCity = ipCity;
	}

	public String getIpRegion() {
		return ipRegion;
	}

	public void setIpRegion(String ipRegion) {
		this.ipRegion = ipRegion;
	}

	public String getIpRegionName() {
		return ipRegionName;
	}

	public void setIpRegionName(String ipRegionName) {
		this.ipRegionName = ipRegionName;
	}

	public String getIpPostalCode() {
		return ipPostalCode;
	}

	public void setIpPostalCode(String ipPostalCode) {
		this.ipPostalCode = ipPostalCode;
	}

	public String getIpMetroCode() {
		return ipMetroCode;
	}

	public void setIpMetroCode(String ipMetroCode) {
		this.ipMetroCode = ipMetroCode;
	}

	public String getIpAreaCode() {
		return ipAreaCode;
	}

	public void setIpAreaCode(String ipAreaCode) {
		this.ipAreaCode = ipAreaCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getIpCountryName() {
		return ipCountryName;
	}

	public void setIpCountryName(String ipCountryName) {
		this.ipCountryName = ipCountryName;
	}

	public String getIpContinentCode() {
		return ipContinentCode;
	}

	public void setIpContinentCode(String ipContinentCode) {
		this.ipContinentCode = ipContinentCode;
	}

	public String getIpLatitude() {
		return ipLatitude;
	}

	public void setIpLatitude(String ipLatitude) {
		this.ipLatitude = ipLatitude;
	}

	public String getIpLongitude() {
		return ipLongitude;
	}

	public void setIpLongitude(String ipLongitude) {
		this.ipLongitude = ipLongitude;
	}

	public String getIpTimeZone() {
		return ipTimeZone;
	}

	public void setIpTimeZone(String ipTimeZone) {
		this.ipTimeZone = ipTimeZone;
	}

	public String getIpUserType() {
		return ipUserType;
	}

	public void setIpUserType(String ipUserType) {
		this.ipUserType = ipUserType;
	}

	public String getIpNetSpeedCell() {
		return ipNetSpeedCell;
	}

	public void setIpNetSpeedCell(String ipNetSpeedCell) {
		this.ipNetSpeedCell = ipNetSpeedCell;
	}

	public String getIpDomain() {
		return ipDomain;
	}

	public void setIpDomain(String ipDomain) {
		this.ipDomain = ipDomain;
	}

	public String getIpIsp() {
		return ipIsp;
	}

	public void setIpIsp(String ipIsp) {
		this.ipIsp = ipIsp;
	}

	public String getIpOrg() {
		return ipOrg;
	}

	public void setIpOrg(String ipOrg) {
		this.ipOrg = ipOrg;
	}

	public String getIpCityConf() {
		return ipCityConf;
	}

	public void setIpCityConf(String ipCityConf) {
		this.ipCityConf = ipCityConf;
	}

	public String getIpRegionConf() {
		return ipRegionConf;
	}

	public void setIpRegionConf(String ipRegionConf) {
		this.ipRegionConf = ipRegionConf;
	}

	public String getIpPostalConf() {
		return ipPostalConf;
	}

	public void setIpPostalConf(String ipPostalConf) {
		this.ipPostalConf = ipPostalConf;
	}

	public String getIpCountryConf() {
		return ipCountryConf;
	}

	public void setIpCountryConf(String ipCountryConf) {
		this.ipCountryConf = ipCountryConf;
	}

	public String getAnonymousProxy() {
		return anonymousProxy;
	}

	public void setAnonymousProxy(String anonymousProxy) {
		this.anonymousProxy = anonymousProxy;
	}

	public String getProxyScore() {
		return proxyScore;
	}

	public void setProxyScore(String proxyScore) {
		this.proxyScore = proxyScore;
	}

	public String getIpCorporateProxy() {
		return ipCorporateProxy;
	}

	public void setIpCorporateProxy(String ipCorporateProxy) {
		this.ipCorporateProxy = ipCorporateProxy;
	}

	public String getFreeMail() {
		return freeMail;
	}

	public void setFreeMail(String freeMail) {
		this.freeMail = freeMail;
	}

	public String getCarderEmail() {
		return carderEmail;
	}

	public void setCarderEmail(String carderEmail) {
		this.carderEmail = carderEmail;
	}

	public String getBinMatch() {
		return binMatch;
	}

	public void setBinMatch(String binMatch) {
		this.binMatch = binMatch;
	}

	public String getBinCountry() {
		return binCountry;
	}

	public void setBinCountry(String binCountry) {
		this.binCountry = binCountry;
	}

	public String getBinNameMatch() {
		return binNameMatch;
	}

	public void setBinNameMatch(String binNameMatch) {
		this.binNameMatch = binNameMatch;
	}

	public String getBinPhoneMatch() {
		return binPhoneMatch;
	}

	public void setBinPhoneMatch(String binPhoneMatch) {
		this.binPhoneMatch = binPhoneMatch;
	}

	public String getPrepaid() {
		return prepaid;
	}

	public void setPrepaid(String prepaid) {
		this.prepaid = prepaid;
	}

	public String getCustPhoneInBillingLoc() {
		return custPhoneInBillingLoc;
	}

	public void setCustPhoneInBillingLoc(String custPhoneInBillingLoc) {
		this.custPhoneInBillingLoc = custPhoneInBillingLoc;
	}

	public String getShipForward() {
		return shipForward;
	}

	public void setShipForward(String shipForward) {
		this.shipForward = shipForward;
	}

	public String getCityPostalMatch() {
		return cityPostalMatch;
	}

	public void setCityPostalMatch(String cityPostalMatch) {
		this.cityPostalMatch = cityPostalMatch;
	}

	public String getShipCityPostalMatch() {
		return shipCityPostalMatch;
	}

	public void setShipCityPostalMatch(String shipCityPostalMatch) {
		this.shipCityPostalMatch = shipCityPostalMatch;
	}

	public String getMinfraudVersion() {
		return minfraudVersion;
	}

	public void setMinfraudVersion(String minfraudVersion) {
		this.minfraudVersion = minfraudVersion;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public CardVO getCardVO() {
		return cardVO;
	}

	public void setCardVO(CardVO cardVO) {
		this.cardVO = cardVO;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public ChargeVO getChargeVO() {
		return chargeVO;
	}

	public void setChargeVO(ChargeVO chargeVO) {
		this.chargeVO = chargeVO;
	}

	public String getIpAsnum() {
		return ipAsnum;
	}

	public void setIpAsnum(String ipAsnum) {
		this.ipAsnum = ipAsnum;
	}

	public String getProcessTime() {
		return processTime;
	}

	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}

	public String getRiskScore() {
		return riskScore;
	}

	public void setRiskScore(String riskScore) {
		this.riskScore = riskScore;
	}

	public boolean isHighRiskScore() {
		return highRiskScore;
	}

	public void setHighRiskScore(boolean highRiskScore) {
		this.highRiskScore = highRiskScore;
	}

	public MerchantVO getMerchantVO() {
		return merchantVO;
	}

	public void setMerchantVO(MerchantVO merchantVO) {
		this.merchantVO = merchantVO;
	}

	public String getDateReport() {
		return dateReport;
	}

	public String getAmountDateReport() {
		return amountDateReport;
	}

	public String getTotalDateReport() {
		return totalDateReport;
	}

	public RejectedChargeVO getRejectedChargeVO() {
		return rejectedChargeVO;
	}

	public void setDateReport(String dateReport) {
		this.dateReport = dateReport;
	}

	public void setAmountDateReport(String amountDateReport) {
		this.amountDateReport = amountDateReport;
	}

	public void setTotalDateReport(String totalDateReport) {
		this.totalDateReport = totalDateReport;
	}

	public void setRejectedChargeVO(RejectedChargeVO rejectedChargeVO) {
		this.rejectedChargeVO = rejectedChargeVO;
	}

//	public String getInitialDateReport() {
//		return initialDateReport;
//	}
//
//	public String getFinalDateReport() {
//		return finalDateReport;
//	}
//
//	public void setInitialDateReport(String initialDateReport) {
//		this.initialDateReport = initialDateReport;
//	}
//
//	public void setFinalDateReport(String finalDateReport) {
//		this.finalDateReport = finalDateReport;
//	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}


	

}
