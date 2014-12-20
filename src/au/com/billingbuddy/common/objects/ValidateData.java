package au.com.billingbuddy.common.objects;

import au.com.billingbuddy.exceptions.objects.DataSanitizeException;

public class ValidateData {
	
	public static String STRING="^[0-9a-zA-Z<>?,.~!@#$%^&*()_+]+$";
	public static String DIGITS="^[a-zA-Z]*$";
//	public static String DECIMALNUMERIC="(^[0-9]$)|^([0-9]+.?[0-9]*$)";
	public static String DECIMALNUMERIC="^[0-9]+(\\.[0-9]{1,3})?$";
	public static String CARDNUMBER="^[0-9]{16,19}$";
	public static String NAME="^\\w+(\\s+\\w+)*$";
	public static String YEAR="^((\\d){2}|(\\d){4})$";
	public static String MONTH="^(0[1-9])|(1[1-2])$";
	public static String COUNTRY="^[a-zA-Z]{2}";
	public static String CVC="^[0-9]{3}$";
	public static String BIN="^[0-9]{6}$";
	public static String DOMAIN="[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static String EMAIL="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public static void validate(String str, String rule) throws DataSanitizeException{
		if (str == null || str.isEmpty() || !str.matches(rule)){
			System.out.println(str + " --> Error in parameter  --> " +rule );
			throw new DataSanitizeException(str + " --> Error in parameter  --> " +rule );
		}
	}
	
//	public void DataSanitize(TransactionVO transactionVO) throws DataSanitizeException{
//		validate(transactionVO.getCreditCardNumber().trim(), CCNUM);
//		validate(transactionVO.getName().trim(),NAME);
//		validate(transactionVO.getEmail().trim(), EMAIL);
//		validate(transactionVO.getCompanyName().trim(), STRING);
//		validate(transactionVO.getExpirationYear().trim(), YEAR);
//		validate(transactionVO.getExpirationMonth().trim(), MONTH);
//		validate(transactionVO.getCvc().trim(), CVC);
//		validate(transactionVO.getProduct().trim(), STRING);
//		validate(transactionVO.getQuantity().trim(), FLOAT);
//		validate(transactionVO.getRate().trim(), FLOAT);
//		validate(transactionVO.getTotal().trim(), FLOAT);
//		
//	}
	
}
