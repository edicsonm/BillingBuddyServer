package au.com.billingbuddy.business.objects;

import java.util.Calendar;
import java.util.HashMap;

import org.json.simple.JSONObject;

import au.com.billigbuddy.utils.ErrorManager;
import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.dao.objects.TransactionDAO;
import au.com.billingbuddy.exceptions.objects.FraudDetectionMDTRException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.vo.objects.TransactionVO;

import com.maxmind.ws.CreditCardFraudDetection;

public class FraudDetectionMDTR {
	
	final boolean isSecure = true;
	private static FraudDetectionMDTR instance = null;	
	private static ConfigurationSystem configurationSystem = ConfigurationSystem.getInstance();
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();

	private boolean debugMode;
	private boolean printTimes;
	private long initialTime;
	private long finalTime;
	
	public static synchronized FraudDetectionMDTR getInstance() {
		if (instance == null) {
			instance = new FraudDetectionMDTR();
		}
		return instance;
	}
	
	private FraudDetectionMDTR() {}
	
	public TransactionVO creditCardFraudDetection(TransactionVO transactionVO) throws FraudDetectionMDTRException{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {
			CreditCardFraudDetection creditCardFraudDetection = new CreditCardFraudDetection(isSecure);
			creditCardFraudDetection.debug = isDebugMode();
			creditCardFraudDetection.setTimeout(10);
			
//			System.out.println("ip: " + transactionVO.getIp());
//			System.out.println("license_key: " + configurationSystem.getKey("license_key"));
//			System.out.println("bin: " + transactionVO.getCardVO().getBin());
			
			hashMap.put("i", transactionVO.getIp());
			hashMap.put("license_key", configurationSystem.getKey("license_key"));
			hashMap.put("bin", transactionVO.getCardVO().getBin());
			
	//		// Required Fields - Billing Address
	        hashMap.put("city", transactionVO.getBillingAddressCity());
	        hashMap.put("region", transactionVO.getBillingAddressRegion());
	        hashMap.put("postal", transactionVO.getBillingAddressPostal());
	        hashMap.put("country", transactionVO.getBillingAddressCountry());
	//		
	//        //Required Fields - Shipping Address
	//		hashMap.put("shipAddr", transactionVO.getShippingAddressVO().getAddress());
	//		hashMap.put("shipCity", transactionVO.getShippingAddressVO().getCity());
	//		hashMap.put("shipRegion",transactionVO.getShippingAddressVO().getRegion());
	//		hashMap.put("shipPostal", transactionVO.getShippingAddressVO().getPostal());
	//		hashMap.put("shipCountry", transactionVO.getShippingAddressVO().getCountry());
	
			
			//Required Fields - User Data
			/*Some of these fields are MD5 hash fields. Such fields should be passed as an MD5 hash in hex form. 
			 * Always lower-case the input before hashing. Do not use any salt when hashing these inputs.*/
	//		hashMap.put("domain","");
	//		hashMap.put("custPhone","");
	//		hashMap.put("emailMD5","");
	//		hashMap.put("usernameMD5","");
	
			//Required Fields - BIN-related
			/*These fields are used to verify that the customer is in possession of the credit card.*/
	//		hashMap.put("bin","");
	//		hashMap.put("binName","");
	//		hashMap.put("binPhone","");
			
			//Required Fields - Transaction Linking
			/*These fields are used to link together fraudulent orders from the same browser across multiple proxies or credit card numbers.*/
	//		hashMap.put("user_agent","");
	//		hashMap.put("accept_language","");
			
			//Required Fields - Transaction Information
			/*These fields provide additional information about the transaction.*/
	//		hashMap.put("txnID","");
	//		hashMap.put("order_amount","");
	//		hashMap.put("order_currency","");
	//		hashMap.put("shopID","");
	//		hashMap.put("txn_type","");
			
			initialTime = Calendar.getInstance().getTimeInMillis();
			creditCardFraudDetection.input(hashMap);
	        creditCardFraudDetection.query();
	        hashMap = creditCardFraudDetection.output();
	        finalTime = Calendar.getInstance().getTimeInMillis();
	        
	        printValues(hashMap);
	        printTimes(initialTime, finalTime);
	        
	        transactionVO.setHighRiskScore(isHighRiskScore(hashMap.get("riskScore")));
	        transactionVO = parseElements(transactionVO, hashMap);
	        transactionVO.setProcessTime((finalTime-initialTime) + " ms.");
        
			TransactionDAO transactionDAO = new TransactionDAO();
			
			if(transactionDAO.insert(transactionVO) != 0){
				ErrorManager.manageErrorPaymentPage("ProcessorMDTR.creditCardFraudDetection.SaveTransaction", new JSONObject(hashMap).toString());
				System.out.println("OJO CAMBIAR EL IF PARA EVITAR EL REGISTRO DE TODAS LAS TRANSACCIONES");
			}
	        
	        System.out.println("transactionVO.isRiskScore(): " + transactionVO.isHighRiskScore());
	        if(transactionVO.isHighRiskScore()){
	        	transactionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
				transactionVO.setMessage(instanceConfigurationApplication.getKey("FraudDetectionMDRT.1"));
	        }else{
	        	transactionVO.setStatus(instanceConfigurationApplication.getKey("success"));
				transactionVO.setMessage(instanceConfigurationApplication.getKey("FraudDetectionMDRT.0"));
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			FraudDetectionMDTRException fraudDetectionMDTRException = new FraudDetectionMDTRException(e, "ProcessorMDTR.creditCardFraudDetection.MySQLConnectionException", new JSONObject(hashMap).toString());
			fraudDetectionMDTRException.setErrorCode("ProcessorMDTR.creditCardFraudDetection.MySQLConnectionException");
			throw fraudDetectionMDTRException;
		} catch (TransactionDAOException e) {
			FraudDetectionMDTRException fraudDetectionMDTRException = new FraudDetectionMDTRException(e, "ProcessorMDTR.creditCardFraudDetection.TransactionDAOException", new JSONObject(hashMap).toString());
			fraudDetectionMDTRException.setErrorCode("ProcessorMDTR.creditCardFraudDetection.TransactionDAOException");
			throw fraudDetectionMDTRException;
		} catch (Exception e) {
			FraudDetectionMDTRException fraudDetectionMDTRException = new FraudDetectionMDTRException(e, "ProcessorMDTR.creditCardFraudDetection.Exception", new JSONObject(hashMap).toString());
			fraudDetectionMDTRException.setErrorCode("ProcessorMDTR.creditCardFraudDetection.Exception");
			throw fraudDetectionMDTRException;
		}
        return transactionVO;
	}
	
	private void printTimes(long initialTime, long finalTime) {
		System.out.println("Time to obtain answer from maxmind: " + (finalTime-initialTime) + " ms.");
	}

	public boolean isHighRiskScore(String riskScore){
		 Float score = Float.parseFloat(riskScore);
		 if(score > Integer.parseInt(configurationSystem.getKey("isHighRiskScore"))){
			 return true;
		 }else{
			 return false;
		 }
	 }
	
	public void printValues(HashMap<String, String> hashMap){
		for (final String string : hashMap.keySet()) {
            final String key = string;
            final String value = hashMap.get(key);
            System.out.println("("+key +","+value+")");
        }
	}

	public boolean isDebugMode() {
		if(configurationSystem.getKey("maxmindDebugMode").equalsIgnoreCase("true"))return true;
		return false;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	public boolean isPrintTimes() {
		return printTimes;
	}

	public void setPrintTimes(boolean printTimes) {
		this.printTimes = printTimes;
	}
	
	private TransactionVO parseElements(TransactionVO transactionVO, HashMap<String, String> hashMap){
		
		if (hashMap.containsKey("cityPostalMatch")) transactionVO.setCityPostalMatch((String)hashMap.get("cityPostalMatch")) ;
		if (hashMap.containsKey("binCountry")) transactionVO.setBinCountry((String)hashMap.get("binCountry"));
		if (hashMap.containsKey("binNameMatch")) transactionVO.setBinNameMatch((String)hashMap.get("binNameMatch"));
		if (hashMap.containsKey("anonymousProxy")) transactionVO.setAnonymousProxy((String)hashMap.get("anonymousProxy"));
		if (hashMap.containsKey("ip_city")) transactionVO.setIpCity((String)hashMap.get("ip_city"));
		if (hashMap.containsKey("countryMatch")) transactionVO.setCountryMatch((String)hashMap.get("countryMatch"));
		if (hashMap.containsKey("ip_areaCode")) transactionVO.setIpAreaCode((String)hashMap.get("ip_areaCode"));
		if (hashMap.containsKey("binPhoneMatch")) transactionVO.setBinPhoneMatch((String)hashMap.get("binPhoneMatch"));
		if (hashMap.containsKey("ip_regionName")) transactionVO.setIpRegionName((String)hashMap.get("ip_regionName"));
		if (hashMap.containsKey("ip_isp")) transactionVO.setIpIsp((String)hashMap.get("ip_isp"));
		if (hashMap.containsKey("ip_cityConf")) transactionVO.setIpCityConf((String)hashMap.get("ip_cityConf"));
		if (hashMap.containsKey("binMatch")) transactionVO.setBinMatch((String)hashMap.get("binMatch"));
		if (hashMap.containsKey("freeMail")) transactionVO.setFreeMail((String)hashMap.get("freeMail"));
		if (hashMap.containsKey("distance")) transactionVO.setDistance((String)hashMap.get("distance"));
		if (hashMap.containsKey("custPhoneInBillingLoc")) transactionVO.setCustPhoneInBillingLoc((String)hashMap.get("custPhoneInBillingLoc"));
		if (hashMap.containsKey("shipCityPostalMatch")) transactionVO.setShipCityPostalMatch((String)hashMap.get("shipCityPostalMatch"));
		if (hashMap.containsKey("binPhone")) transactionVO.setBinPhone((String)hashMap.get("binPhone"));
		if (hashMap.containsKey("ip_postalCode")) transactionVO.setIpPostalCode((String)hashMap.get("ip_postalCode"));
		if (hashMap.containsKey("ip_continentCode")) transactionVO.setIpContinentCode((String)hashMap.get("ip_continentCode"));
		if (hashMap.containsKey("ip_latitude")) transactionVO.setIpLatitude((String)hashMap.get("ip_latitude"));
		if (hashMap.containsKey("maxmindID")) transactionVO.setMaxmindId((String)hashMap.get("maxmindID"));
		if (hashMap.containsKey("prepaid")) transactionVO.setPrepaid((String)hashMap.get("prepaid"));
		if (hashMap.containsKey("ip_longitude")) transactionVO.setIpLatitude((String)hashMap.get("ip_longitude"));
		if (hashMap.containsKey("ip_netSpeedCell")) transactionVO.setIpNetSpeedCell((String)hashMap.get("ip_netSpeedCell"));
		if (hashMap.containsKey("ip_regionConf")) transactionVO.setIpRegionConf((String)hashMap.get("ip_regionConf"));
		if (hashMap.containsKey("ip_asnum")) transactionVO.setIpAsnum((String)hashMap.get("ip_asnum"));
		if (hashMap.containsKey("ip_domain")) transactionVO.setIpDomain((String)hashMap.get("ip_domain"));
//		if (hashMap.containsKey("queriesRemaining")) transactionVO.setDistance((String)hashMap.get("queriesRemaining"));
		if (hashMap.containsKey("countryCode")) transactionVO.setCountryCode((String)hashMap.get("countryCode"));
		if (hashMap.containsKey("riskScore")) transactionVO.setRiskScore((String)hashMap.get("riskScore"));
		if (hashMap.containsKey("ip_region")) transactionVO.setIpRegion((String)hashMap.get("ip_region"));
		if (hashMap.containsKey("service_level")) transactionVO.setServiceLevel((String)hashMap.get("service_level"));
		if (hashMap.containsKey("ip_countryConf")) transactionVO.setIpCountryConf((String)hashMap.get("ip_countryConf"));
		if (hashMap.containsKey("proxyScore")) transactionVO.setProxyScore((String)hashMap.get("proxyScore"));
		if (hashMap.containsKey("ip_countryName")) transactionVO.setIpCountryName((String)hashMap.get("ip_countryName"));
		if (hashMap.containsKey("highRiskCountry")) transactionVO.setHighRiskCountry((String)hashMap.get("highRiskCountry"));
		if (hashMap.containsKey("ip_postalConf")) transactionVO.setIpPostalCode((String)hashMap.get("ip_postalConf"));
		if (hashMap.containsKey("ip_userType")) transactionVO.setIpUserType((String)hashMap.get("ip_userType"));
		if (hashMap.containsKey("binName")) transactionVO.setBinName((String)hashMap.get("binName"));
		if (hashMap.containsKey("ip_org")) transactionVO.setIpOrg((String)hashMap.get("ip_org"));
		if (hashMap.containsKey("ip_metroCode")) transactionVO.setIpMetroCode((String)hashMap.get("ip_metroCode"));
		if (hashMap.containsKey("ip_corporateProxy")) transactionVO.setIpCorporateProxy((String)hashMap.get("ip_corporateProxy"));
		if (hashMap.containsKey("err")) transactionVO.setError((String)hashMap.get("err"));
		if (hashMap.containsKey("ip_timeZone")) transactionVO.setIpTimeZone((String)hashMap.get("ip_timeZone"));
		if (hashMap.containsKey("minfraud_version")) transactionVO.setMinfraudVersion((String)hashMap.get("minfraud_version"));
		if (hashMap.containsKey("ip_accuracyRadius")) transactionVO.setIpAccuracyRadius((String)hashMap.get("ip_accuracyRadius"));
		
		return transactionVO;
	}
	
}
