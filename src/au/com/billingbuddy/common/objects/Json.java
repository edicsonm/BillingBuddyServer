package au.com.billingbuddy.common.objects;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import au.com.billingbuddy.exceptions.objects.CardDAOException;
import au.com.billingbuddy.exceptions.objects.DataSanitizeException;
import au.com.billingbuddy.exceptions.objects.JsonException;
import au.com.billingbuddy.vo.objects.BinVO;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.CustomerVO;
import au.com.billingbuddy.vo.objects.ShippingAddressVO;
import au.com.billingbuddy.vo.objects.StripeChargeVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

public class Json {

	private static Json instance;
	
	public static synchronized Json getInstance() {
		if (instance == null) {
			instance = new Json();
		}
		return instance;
	}
	
	private Json() {}
	
	public static BinVO decodeJSONBinInformation(String jsonMessage) throws JsonException {
		try {
			JSONParser parser = new JSONParser();
			BinVO binVO = new BinVO();
			Object obj = parser.parse(jsonMessage);
			JSONObject jSONObject = (JSONObject) obj;
			binVO.setBin(jSONObject.get("bin").toString());
			binVO.setBrand(jSONObject.get("brand").toString());
			binVO.setSuBrand(jSONObject.get("sub_brand").toString());
			binVO.setCountryCode(jSONObject.get("country_code").toString());
			binVO.setCountryName(jSONObject.get("country_name").toString());
			binVO.setBankName(jSONObject.get("bank").toString());
			binVO.setCardType(jSONObject.get("card_type").toString());
			binVO.setCardCategory(jSONObject.get("card_category").toString());
			return binVO;
		} catch (ParseException e) {
			JsonException jsonException =  new JsonException(e);
			jsonException.setErrorCode("4");
			throw jsonException;
		}
	}

	/**
	 * Method designed to generate a JSON message from three parametres received
	 * from the socket server.
	 * 
	 * @param status
	 * @param message
	 * @param data
	 */
	public static JSONObject encodeJSON(String status, String message, String data) {
		JSONObject jSONObject = new JSONObject();
		jSONObject.put("status", status);
		jSONObject.put("message", message);
		jSONObject.put("data", data);
		return jSONObject;
	}
	
	/** Method designed to decode a JSON message
	 * @param jsonMessage
	 * @return
	 * @throws ParseException 
	 * @throws DataSanitizeException 
	 */
	public static void decodeJSON(String jsonMessage, TransactionVO transactionVO, CardVO cardVO, CustomerVO customerVO, ShippingAddressVO shippingAddressVO) throws JsonException {
		try {
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(jsonMessage);
			JSONObject jSONObject = (JSONObject) obj;
			
			/****** Table Card ******/
			cardVO.setName(jSONObject.get("name").toString());
			cardVO.setCardNumber(jSONObject.get("ccnum").toString());
			cardVO.setBin(cardVO.getCardNumber().substring(0, 6));
			cardVO.setExpYear(jSONObject.get("expyear").toString());
			cardVO.setExpMonth(jSONObject.get("expmonth").toString());
			cardVO.setCvv(jSONObject.get("cvc").toString());

			/****** Table TransactionInformation ******/
			transactionVO.setCompanyName(jSONObject.get("companyname").toString());
			transactionVO.setProduct(jSONObject.get("product").toString());
			transactionVO.setQuantity(jSONObject.get("qty").toString());
			transactionVO.setRate(jSONObject.get("rate").toString());
			transactionVO.setOrderAmount(jSONObject.get("total").toString());

			transactionVO.setOrderCurrency(jSONObject.get("order_currency").toString());
			transactionVO.setMerchantId(jSONObject.get("shopID").toString());
			transactionVO.setTxnType(jSONObject.get("txn_type").toString());
			
			transactionVO.setIp(jSONObject.get("i").toString());
			transactionVO.setBillingAddressCity(jSONObject.get("city").toString());
			transactionVO.setBillingAddressRegion(jSONObject.get("region").toString());
			transactionVO.setBillingAddressPostal(jSONObject.get("postal").toString());
			transactionVO.setBillingAddressCountry(jSONObject.get("country").toString());
			transactionVO.setId(jSONObject.get("txnID").toString());
			transactionVO.setSessionId(jSONObject.get("sessionID").toString());
			transactionVO.setUserAgent(jSONObject.get("user_agent").toString());
			transactionVO.setAcceptLanguage(jSONObject.get("accept_language").toString());
			transactionVO.setDomain(jSONObject.get("domain").toString());
			
			/****** Table ShippingAddress ******/
			shippingAddressVO.setAddress(jSONObject.get("shipAddr").toString());
			shippingAddressVO.setCity(jSONObject.get("shipCity").toString());
			shippingAddressVO.setRegion(jSONObject.get("shipRegion").toString());
			shippingAddressVO.setPostal(jSONObject.get("shipPostal").toString());
			shippingAddressVO.setCountry(jSONObject.get("shipCountry").toString());
			
			/****** Table Customer ******/
			customerVO.setEmail(jSONObject.get("emailMD5").toString());
			customerVO.setUsername(jSONObject.get("usernameMD5").toString());
			customerVO.setPassword(jSONObject.get("passwordMD5").toString());
			
		} catch (NullPointerException e) {
			JsonException jsonException =  new JsonException(e);
			jsonException.setErrorCode("1");
			throw jsonException;
		} catch (ParseException e) {
			JsonException jsonException =  new JsonException(e);
			jsonException.setErrorCode("2");
			throw jsonException;
		} catch (DataSanitizeException e) {
			JsonException jsonException =  new JsonException(e);
			jsonException.setErrorCode("3");
			throw jsonException;
		}
	}

	public static void decodeJSONStripeCharge(String jsonMessage, StripeChargeVO stripeChargeVO) throws JsonException {
		try {
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(jsonMessage);
			JSONObject jSONObject = (JSONObject) obj;
			
			stripeChargeVO.setIdStripe(jSONObject.get("id").toString());
			stripeChargeVO.setObject("charge");
			stripeChargeVO.setLiveMode(jSONObject.get("livemode").toString());
			stripeChargeVO.setPaid(jSONObject.get("paid").toString());
			stripeChargeVO.setAmount(jSONObject.get("amount").toString());
			stripeChargeVO.setCurrency(jSONObject.get("currency").toString());
			stripeChargeVO.setRefunded("");
			stripeChargeVO.setCaptured(jSONObject.get("captured").toString());
			stripeChargeVO.setBalanceTransaction(jSONObject.get("balance_transaction").toString());
			stripeChargeVO.setIdDispute(null);
			stripeChargeVO.setFailureMessage(jSONObject.get("failure_message").toString());
			stripeChargeVO.setFailureCode(jSONObject.get("failure_code").toString());
			stripeChargeVO.setAmountRefunded("");
			stripeChargeVO.setIdCustomer(null);
			stripeChargeVO.setInvoice(jSONObject.get("invoice").toString());
			stripeChargeVO.setDescription(jSONObject.get("description").toString());
			stripeChargeVO.setDispute(null);
			stripeChargeVO.setMetadata(jSONObject.get("metadata").toString());
			stripeChargeVO.setStatementDescription(jSONObject.get("metadata").toString());
			stripeChargeVO.setReceiptEmail(null);
			stripeChargeVO.setReceiptNumber(null);
			stripeChargeVO.setShipping(null);
			stripeChargeVO.setIdCard(null);

		} catch (NullPointerException e) {
			JsonException jsonException =  new JsonException(e);
			jsonException.setErrorCode("1");
			throw jsonException;
		} catch (ParseException e) {
			JsonException jsonException =  new JsonException(e);
			jsonException.setErrorCode("2");
			throw jsonException;
		}
	}
	
	
	/** Method designed to extract a message from a JSON file
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public JSONObject fileReader(String filePath) throws IOException, ParseException {
		FileReader reader = new FileReader(filePath);
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
		return jsonObject;
	}
}
