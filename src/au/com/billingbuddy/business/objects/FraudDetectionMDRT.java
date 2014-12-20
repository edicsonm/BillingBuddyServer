package au.com.billingbuddy.business.objects;

import java.util.Calendar;
import java.util.HashMap;

import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.vo.objects.TransactionVO;

import com.maxmind.ws.CreditCardFraudDetection;

public class FraudDetectionMDRT {
	
	final boolean isSecure = true;
	private HashMap<String, String> hashMap = new HashMap<String, String>();
	private static FraudDetectionMDRT instance = null;	
	private static ConfigurationSystem configurationSystem = ConfigurationSystem.getInstance();
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();

	private boolean debugMode;
	private boolean printTimes;
	private long initialTime;
	private long finalTime;
	
	public static synchronized FraudDetectionMDRT getInstance() {
		if (instance == null) {
			instance = new FraudDetectionMDRT();
		}
		return instance;
	}
	
	private FraudDetectionMDRT() {}
	
	public TransactionVO CreditCardFraudDetection(TransactionVO transactionVO){
		
		CreditCardFraudDetection creditCardFraudDetection = new CreditCardFraudDetection(isSecure);
		creditCardFraudDetection.debug = isDebugMode();
		creditCardFraudDetection.setTimeout(10);
		
		hashMap.put("i", transactionVO.getIp());
		hashMap.put("license_key", configurationSystem.getKey("license_key"));
		hashMap.put("bin", transactionVO.getCardVO().getBin());
		
//		// Required Fields - Billing Address
//        hashMap.put("city", transactionVO.getBillingAddressCity());
//        hashMap.put("region", transactionVO.getBillingAddressRegion());
//        hashMap.put("postal", transactionVO.getBillingAddressPostal());
//        hashMap.put("country", transactionVO.getBillingAddressCountry());
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
        
        transactionVO.setRiskScore(isHighRiskScore(hashMap.get("riskScore")));
        if(transactionVO.isRiskScore()){
        	transactionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
			transactionVO.setMessage(instanceConfigurationApplication.getKey("FraudDetectionMDRT.1"));
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
	
}
