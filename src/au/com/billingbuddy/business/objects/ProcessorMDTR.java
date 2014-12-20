package au.com.billingbuddy.business.objects;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.Json;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.dao.objects.StripeChargeDAO;
import au.com.billingbuddy.exceptions.objects.JsonException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.StripeChargeDAOException;
import au.com.billingbuddy.vo.objects.StripeChargeVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

public class ProcessorMDTR {
	
	private static ProcessorMDTR instance = null;
	private static ConfigurationSystem configurationSystem = ConfigurationSystem.getInstance();
	
	private boolean printTimes;
	private long initialTime;
	private long finalTime;
	
	public static synchronized ProcessorMDTR getInstance() {
		if (instance == null) {
			instance = new ProcessorMDTR();
		}
		return instance;
	}
	
	private ProcessorMDTR() {
		Stripe.apiKey = configurationSystem.getKey("apiKey");
	}
	
	public void chargeTransaction(TransactionVO transactionVO){
		try {
			Map<String, Object> hashMapCharge = new HashMap<String, Object>();
			hashMapCharge.put("amount", transactionVO.getOrderAmount());
			hashMapCharge.put("currency", transactionVO.getOrderCurrency());
			hashMapCharge.put("description", "Charge for test@example.com");

			Map<String, Object> hashMapCard = new HashMap<String, Object>();
			hashMapCard.put("number", transactionVO.getCardVO().getCardNumber());
			hashMapCard.put("exp_month", transactionVO.getCardVO().getExpMonth());
			hashMapCard.put("exp_year", transactionVO.getCardVO().getExpYear());
			hashMapCard.put("cvc", transactionVO.getCardVO().getCvv());
			hashMapCard.put("name", transactionVO.getCardVO().getName());
			
			hashMapCharge.put("card", hashMapCard);
			initialTime = Calendar.getInstance().getTimeInMillis();
			Charge charge = Charge.create(hashMapCharge);
			
			System.out.println("charge.getAmount(): " + charge.getAmount());
			
			
			finalTime = Calendar.getInstance().getTimeInMillis();
			StripeChargeVO stripeChargeVO = new StripeChargeVO();
			Utilities.copyChargeToStripeChargeVO(stripeChargeVO,charge);
			StripeChargeDAO stripeChargeDAO = new StripeChargeDAO();
			stripeChargeDAO.insert(stripeChargeVO);
			System.out.println("stripeChargeVO.getId(): " + stripeChargeVO.getId());
			
//			printValues(charge);
	        printTimes(initialTime, finalTime);
	        
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (CardException e) {
			e.printStackTrace();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (StripeChargeDAOException e) {
			e.printStackTrace();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
		}
	}

	private void printTimes(long initialTime, long finalTime) {
		System.out.println("Time to obtain answer from maxmind: " + (finalTime-initialTime) + " ms.");
	}
	
	public void printValues(Charge charge){
		
		System.out.println("charge.getAmount(): " + charge.getAmount());
		System.out.println("charge.getAmountRefunded(): " + charge.getAmountRefunded());
		System.out.println("charge.getBalanceTransaction(): " + charge.getBalanceTransaction());
		System.out.println("charge.getCaptured(): " + charge.getCaptured());
		System.out.println("charge.getCard(): " + charge.getCard());
		System.out.println("charge.getCreated(): " + charge.getCreated());
		System.out.println("charge.getCurrency(): " + charge.getCurrency());
		System.out.println("charge.getCustomer(): " + charge.getCustomer());
		System.out.println("charge.getDescription(): " + charge.getDescription());
		System.out.println("charge.getDispute(): " + charge.getDispute());
		System.out.println("charge.getDisputed(): " + charge.getDisputed());
		System.out.println("charge.getFailureCode(): " + charge.getFailureCode());
		System.out.println("charge.getFailureMessage(): " + charge.getFailureMessage());
		System.out.println("charge.getId(): " + charge.getId());
		System.out.println("charge.getInvoice(): " + charge.getInvoice());
		System.out.println("charge.getLivemode(): " + charge.getLivemode());
		System.out.println("charge.getMetadata(): " + charge.getMetadata());
		System.out.println("charge.getPaid(): " + charge.getPaid());
		System.out.println("charge.getRefunded(): " + charge.getRefunded());
		System.out.println("charge.getRefunds(): " + charge.getRefunds());
		System.out.println("charge.getStatementDescription(): " + charge.getStatementDescription());
		
		System.out.println("charge.toString(): " + charge.toString());
	}
	
	
}

