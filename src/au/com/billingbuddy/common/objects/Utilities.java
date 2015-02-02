package au.com.billingbuddy.common.objects;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stripe.model.Charge;
import com.stripe.model.Refund;

import au.com.billingbuddy.vo.objects.CertificateVO;
import au.com.billingbuddy.vo.objects.ChargeVO;

public class Utilities {
	
	Currency currency;
	public static ConfigurationApplication configurationApplication = ConfigurationApplication.getInstance();
	
//	public static boolean isNullOrEmpty(String valor) {
//		if (valor == null)
//			return true;
//		if (valor.length() == 0)
//			return true;
//		else
//			return false;
//	}
	
	public static String currencyToStripe(String amount, String currency) {
		int decimalValue = Integer.parseInt(configurationApplication.getKey("currency."+currency));
		return new DecimalFormat("#0").format(Double.parseDouble(amount) * decimalValue);
	}
	
	public static String stripeToCurrency(String amount, String currency) {
		try {
			int decimalValue = Integer.parseInt(configurationApplication.getKey("currency."+currency));
			return String.valueOf(Double.parseDouble(amount) / decimalValue);
		} catch (MissingResourceException e) {
			e.printStackTrace();
		} 
		return configurationApplication.getKey("currency.imposibleFormatAmount");
	}
	
	public static String currencyToStripe(String amount, Currency currency){
		int decimalValue = Integer.parseInt(configurationApplication.getKey("currency."+currency));
		return new DecimalFormat("#0").format(Double.parseDouble(amount) * decimalValue);
//		switch (currency) {
//         case USD:
//        	 System.out.println("Currency USD." + amount);
//        	 return new DecimalFormat("#0").format(Double.parseDouble(amount) * decimalValue);
//         case AUD:
//        	 System.out.println("Currency AUD.");
//        	 return new DecimalFormat("#0").format(Double.parseDouble(amount) * decimalValue);
//         default:
//        	 System.out.println("Currency USD. Default value");
//        	 return new DecimalFormat("#0").format(Double.parseDouble(amount) * decimalValue);
//		 }
	}
	
	public static String stripeToCurrency(String amount, Currency currency){
		int decimalValue = Integer.parseInt(configurationApplication.getKey("currency."+currency));
		return String.valueOf(Double.parseDouble(amount) / decimalValue);
	}
	
	public static void copyChargeToChargeVO(ChargeVO chargeVO, Charge charge){
		
		chargeVO.setAmount(String.valueOf(charge.getAmount()));
		chargeVO.setAmountRefunded(String.valueOf(charge.getAmountRefunded()));
		chargeVO.setBalanceTransaction(charge.getBalanceTransaction());
		chargeVO.setCaptured(booleanToString(charge.getCaptured()));
		chargeVO.setCurrency(charge.getCurrency());
		
		chargeVO.setFailureCode(charge.getFailureCode());
		chargeVO.setFailureMessage(charge.getFailureMessage());
		chargeVO.setStripeId(charge.getId());
		chargeVO.setPaid(booleanToString(charge.getPaid()));
		chargeVO.setRefunded(booleanToString(charge.getRefunded()));
		
		chargeVO.setDescription(charge.getDescription());
		chargeVO.setStatementDescription(charge.getStatementDescription());
		chargeVO.setObject("");
		chargeVO.setCardId(charge.getCard().getId());
		chargeVO.setInvoice(charge.getInvoice());
		chargeVO.setLiveMode(booleanToString(charge.getLivemode()));
		chargeVO.getCardVO().setCardIdStripe(charge.getCard().getId());
		chargeVO.getCardVO().setLast4(charge.getCard().getLast4());
		chargeVO.getCardVO().setCountry(charge.getCard().getCountry());
		chargeVO.getCardVO().setBrand(charge.getCard().getBrand());
		chargeVO.getCardVO().setFunding(charge.getCard().getFunding());
		chargeVO.getCardVO().setFingerPrint(charge.getCard().getFingerprint());
//		charge.getCard();
//		charge.getCreated();
//		charge.getCustomer();
//		charge.getDispute();
//		charge.getDisputed();
//		charge.getMetadata()
//		charge.getRefunds();
		
	}
	
	public static String booleanToString(boolean val){
		if(val)return "0";
		return "1";
	}

	public static void copyRefundToChargeVO(ChargeVO chargeVO, Refund refund) {
		chargeVO.getRefundVO().setId(refund.getId());
		chargeVO.getRefundVO().setAmount(String.valueOf(refund.getAmount()));
		chargeVO.getRefundVO().setCurrency(refund.getCurrency());
		chargeVO.getRefundVO().setCreationTime(String.valueOf(refund.getCreated()));
		chargeVO.getRefundVO().setBalanceTransaction(refund.getBalanceTransaction());
		chargeVO.getRefundVO().setChargeId(chargeVO.getId());
		chargeVO.getRefundVO().setStripeId(refund.getId());
	}
	
	public static String searchStripeError(String message ){
		if(Pattern.compile("Charge.*has already been refunded.*").matcher(message).matches()) return "ProcessorMDTR.processRefound.InvalidRequestException.1";
		if(Pattern.compile("Amount must be no more than than .*").matcher(message).matches()) return "ProcessorMDTR.processRefound.InvalidRequestException.2";
		if(Pattern.compile("Refund amount.*is greater than unrefunded amount on charge.*").matcher(message).matches()) return "ProcessorMDTR.processRefound.InvalidRequestException.3";
		return "";
	}
	
	public static String formatDate(String date){
		try {
			return getSimpleDateFormat().format(getSystemDateFormat().parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return date;
	}
	
	public static boolean isNullOrEmpty(String value){
		if(value == null )return true;
		if(value.length() == 0 )return true;
		if(value.trim().length() == 0 )return true;
		if(value.equalsIgnoreCase(""))return true;
		else return false;
	}
	
	public static String getCurrentDate(){
		return getSimpleDateFormat().format(Calendar.getInstance().getTime()); 
	}
	
	public static SimpleDateFormat getSimpleDateFormat(){
		return  new SimpleDateFormat("dd-MM-yyyy");
	}
	
	public static SimpleDateFormat getSystemDateFormat(){
		return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public static Calendar getCalendarDateSimple(String stringDate) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar = Calendar.getInstance();
			Date date = Utilities.getSimpleDateFormat().parse(stringDate);
			calendar.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calendar;
	}
	
	public static Calendar getCalendarDateSystem(String stringDate) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar = Calendar.getInstance();
			Date date = Utilities.getSystemDateFormat().parse(stringDate);
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		
		try {
			calendar = Calendar.getInstance();
			Date date = Utilities.getSimpleDateFormat().parse(stringDate);
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		
		return calendar;
	}
	
	public static java.sql.Date stringToSqlDate(String stringDate){
		try {
			Calendar calendar = Calendar.getInstance();
			java.util.Date date = getSimpleDateFormat().parse(stringDate);
			calendar.setTime(date);
			java.sql.Date sqlDate = new java.sql.Date(calendar.getTimeInMillis());
			return sqlDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	}
	
	public static MySQLError extractSQLError(String errorMessage, int mysqlCode){
		MySQLError mySQLError = null;
		switch (mysqlCode) {
		case 1062:
//			String string = "Duplicate entry '2' for key 'Merc_ID_UNIQUE'";
	        Pattern pattern = Pattern.compile("(\\')(.*?)(\\')");
	        Matcher matcher = pattern.matcher(errorMessage);
	        mySQLError = new MySQLError();
	        mySQLError.setValue(matcher.find() ? matcher.group(0).replace("'", ""): "");
	        mySQLError.setSqlObjectName(matcher.find() ? matcher.group(0).replace("'", ""): "");
			break;
		case 1644:
			mySQLError = new MySQLError();
	        mySQLError.setValue(errorMessage);
	        mySQLError.setSqlObjectName(errorMessage);
			break;
		default:
			break;
		}
		return mySQLError;
	}
	
	public static HashMap<String , String> processingCertificateAnswer(String line, HashMap<String , String> infoCertificates){
		if(line.contains("#")){
			String[] messages = line.split("#");
			System.out.println("Clave: " + messages[0]);
			if(messages[0].equalsIgnoreCase("FOLDER")){
				infoCertificates.put("FOLDER", messages[1]);
			}else if(messages[0].equalsIgnoreCase("KEYSTOREBB")){
				infoCertificates.put("KEYSTOREBB", messages[1]);
			}else if(messages[0].equalsIgnoreCase("KEYSTOREMERCHANT")){
				infoCertificates.put("KEYSTOREMERCHANT", messages[1]);
			}
		}
		return infoCertificates;
	}
	
	public static boolean configureCertificate(String path, CertificateVO certificateVO, String fileName){
		boolean answer = false;
		BufferedInputStream input = null;
		OutputStream output = null;
		try {
			input = new BufferedInputStream(certificateVO.getBBKeyStore().getBinaryStream());
			output = new FileOutputStream(new File(path+"/"+fileName));
			byte[] buffer = new byte[8192];
			for (int length = 0; (length = input.read(buffer)) > 0;) {
				output.write(buffer, 0, length);
			}
			answer = true;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if (output != null) try { output.close(); } catch (IOException ignore) {}
			if (input != null) try { input.close(); } catch (IOException ignore) {}
		}
		certificateVO.setBBKeyStore(null);
		return answer;
	}
	
	public static boolean removeCertificate(String path, String fileName){
		return new File(path+"/"+fileName).delete();
	}
	
}
