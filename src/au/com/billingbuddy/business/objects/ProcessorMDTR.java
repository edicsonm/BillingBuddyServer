package au.com.billingbuddy.business.objects;

import java.util.ArrayList;
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
import com.stripe.model.Refund;

import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.Currency;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.dao.objects.CardDAO;
import au.com.billingbuddy.dao.objects.ChargeDAO;
import au.com.billingbuddy.dao.objects.CustomerDAO;
import au.com.billingbuddy.dao.objects.PlanDAO;
import au.com.billingbuddy.dao.objects.RefundDAO;
import au.com.billingbuddy.exceptions.objects.CardDAOException;
import au.com.billingbuddy.exceptions.objects.ChargeDAOException;
import au.com.billingbuddy.exceptions.objects.CustomerDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.PlanDAOException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.RefundDAOException;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.CustomerVO;
import au.com.billingbuddy.vo.objects.PlanVO;
import au.com.billingbuddy.vo.objects.RefundVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

public class ProcessorMDTR {
	
	private static ProcessorMDTR instance = null;
	private static ConfigurationSystem configurationSystem = ConfigurationSystem.getInstance();
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();
	
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
	
	public TransactionVO chargePayment(TransactionVO transactionVO) throws ProcessorMDTRException {
		ChargeVO chargeVO = null;
		try {
			
			Map<String, Object> hashMapCharge = new HashMap<String, Object>();
			hashMapCharge.put("amount", Utilities.currencyToStripe(transactionVO.getOrderAmount(), Currency.USD));
			hashMapCharge.put("currency", transactionVO.getOrderCurrency());
			hashMapCharge.put("description", "Charge for test@example.com");

			Map<String, Object> hashMapCard = new HashMap<String, Object>();
			hashMapCard.put("number", transactionVO.getCardVO().getNumber());
			hashMapCard.put("exp_month", transactionVO.getCardVO().getExpMonth());
			hashMapCard.put("exp_year", transactionVO.getCardVO().getExpYear());
			hashMapCard.put("cvc", transactionVO.getCardVO().getCvv());
			hashMapCard.put("name", transactionVO.getCardVO().getName());
			
			hashMapCharge.put("card", hashMapCard);
			initialTime = Calendar.getInstance().getTimeInMillis();
			Charge charge = Charge.create(hashMapCharge);
			printValues(charge);
	        printTimes(initialTime, finalTime);
			
			finalTime = Calendar.getInstance().getTimeInMillis();
			chargeVO = new ChargeVO();
			chargeVO.setCardVO(transactionVO.getCardVO());
			chargeVO.setTransactionId(transactionVO.getId());
			chargeVO.setProcessTime((finalTime-initialTime) + " ms.");
			
			long initialTime = Calendar.getInstance().getTimeInMillis();
			Utilities.copyChargeToChargeVO(chargeVO,charge);
			long finalTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("Tiempo total para copiar los elementos de Stripe: " + (finalTime-initialTime) + " ms.");
			
			transactionVO.setCardVO(chargeVO.getCardVO());
			
			printValues(charge);
	        printTimes(initialTime, finalTime);
	        chargeVO.setProcessTime((finalTime-initialTime) + " ms.");
	        transactionVO.setChargeVO(chargeVO);
	        
	        CardVO cardVO = chargeVO.getCardVO();
	        CustomerVO customerVO = transactionVO.getCardVO().getCustomerVO();
	        
	        CustomerDAO customerDAO = new CustomerDAO();
	        initialTime = Calendar.getInstance().getTimeInMillis();
	        customerDAO.insert(customerVO);
			finalTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("Tiempo total para copiar registrar un Customer: " + (finalTime-initialTime) + " ms.");
	        
	        if(customerVO != null && customerVO.getId() != null){
	        	cardVO.setCustomerId(customerVO.getId());
	        	CardDAO cardDAO = new CardDAO();
		        
	        	initialTime = Calendar.getInstance().getTimeInMillis();
	        	cardDAO.insert(cardVO);
				finalTime = Calendar.getInstance().getTimeInMillis();
				System.out.println("Tiempo total para copiar registrar una Card: " + (finalTime-initialTime) + " ms.");
		        
	        	if(cardVO != null && cardVO.getId() != null){
		        	chargeVO.setCardId(cardVO.getId());
		        	chargeVO.setTransactionId(transactionVO.getId());
		        	ChargeDAO chargeDAO = new ChargeDAO();
					
					initialTime = Calendar.getInstance().getTimeInMillis();
					chargeDAO.insert(chargeVO);
					finalTime = Calendar.getInstance().getTimeInMillis();
					System.out.println("Tiempo total para copiar registrar un Charge: " + (finalTime-initialTime) + " ms.");
					
					System.out.println("chargeVO.getId(): " + chargeVO.getId());
		        } else {
		        	System.out.println("#################################################################");
		        	System.out.println("No fue posible registrar la tarjeta .... ");
		        	System.out.println("#################################################################");
		        }
	        } else {
	        	System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar el cliente .... ");
	        	System.out.println("#################################################################");
	        }
			
		} catch (AuthenticationException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.AuthenticationException");
			throw processorMDTRException;
		} catch (InvalidRequestException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.InvalidRequestException");
			throw processorMDTRException;
		} catch (APIConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.APIConnectionException");
			throw processorMDTRException;
		} catch (CardException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.CardException");
			throw processorMDTRException;
		} catch (APIException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.APIException");
			throw processorMDTRException;
		} catch (ChargeDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.ChargeDAOException");
			throw processorMDTRException;
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CardDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.CardDAOException");
			throw processorMDTRException;
		} catch (CustomerDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.CustomerDAOException");
			throw processorMDTRException;
		} finally{
			transactionVO.setChargeVO(chargeVO);
		}
		return transactionVO;
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
	
	public ArrayList<ChargeVO> listCharge(ChargeVO chargeVO) throws ProcessorMDTRException{
		ArrayList<ChargeVO> listCharge = null;
		try {
			ChargeDAO chargeDAO = new ChargeDAO();
			listCharge = chargeDAO.search(chargeVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCharge.MySQLConnectionException");
			throw processorMDTRException;
		} catch (ChargeDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCharge.ChargeDAOException");
			throw processorMDTRException;
		}
		return listCharge;
	}
	
	public ChargeVO processRefund(ChargeVO chargeVO) throws ProcessorMDTRException {
		Map<String, Object> params = new HashMap<String, Object>(); 
		try {
			Charge charge = Charge.retrieve(chargeVO.getStripeId());
			params.put("amount", Utilities.currencyToStripe(chargeVO.getRefundVO().getAmount(), chargeVO.getCurrency().toUpperCase()));
			Refund refund = charge.getRefunds().create(params);
			System.out.println("refund object: " + refund);
			Utilities.copyRefundToChargeVO(chargeVO, refund);
			RefundDAO refundDAO = new RefundDAO();
			refundDAO.insert(chargeVO.getRefundVO());
			if(chargeVO.getRefundVO() != null && chargeVO.getRefundVO().getId() != null){
				chargeVO.setStatus(instanceConfigurationApplication.getKey("success"));
				chargeVO.setMessage("ProcessorMDTR.processRefound.success");
	        }else{
	        	chargeVO.setStatus(instanceConfigurationApplication.getKey("failure"));
				chargeVO.setMessage("ProcessorMDTR.processRefound.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar el Refund .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (AuthenticationException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.processRefound.AuthenticationException");
			throw processorMDTRException;
		} catch (InvalidRequestException e) {
			System.out.println("e.getParam(): " + e.getParam());
			System.out.println("e.getParam(): " + e.getMessage());
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode(Utilities.searchStripeError(e.getMessage()));
			throw processorMDTRException;
		} catch (APIConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.processRefound.APIConnectionException");
			throw processorMDTRException;
		} catch (CardException e) {
			System.out.println("Status is: " + e.getCode());
			System.out.println("Message is: " + e.getParam());
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.processRefound.CardException");
			throw processorMDTRException;
		} catch (APIException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.processRefound.APIException");
			throw processorMDTRException;
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.processRefound.MySQLConnectionException");
			throw processorMDTRException;
		} catch (RefundDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.processRefound.RefundDAOException");
			throw processorMDTRException;
		}
		return chargeVO;
	}
	
	
	public ArrayList<RefundVO> listRefunds(RefundVO refundVO) throws ProcessorMDTRException {
		ArrayList<RefundVO> listRefunds = null;
		try {
			RefundDAO refundDAO = new RefundDAO();
			listRefunds = refundDAO.search(refundVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listRefunds.MySQLConnectionException");
			throw processorMDTRException;
		} catch (RefundDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listRefunds.RefundDAOException");
			throw processorMDTRException;
		}
		return listRefunds;
	}
	
	public ArrayList<PlanVO> listPlans(PlanVO planVO) throws ProcessorMDTRException {
		ArrayList<PlanVO> listPlans = null;
		try {
			PlanDAO planDAO = new PlanDAO();
			listPlans = planDAO.search(planVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listRefunds.MySQLConnectionException");
			throw processorMDTRException;
		} catch (PlanDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listRefunds.PlanDAOException");
			throw processorMDTRException;
		}
		return listPlans;
	}
	
	public PlanVO savePlan(PlanVO planVO) throws ProcessorMDTRException{
		try {
			PlanDAO planDAO = new PlanDAO();
			planDAO.insert(planVO);
			if(planVO != null && planVO.getId() != null){
				planVO.setStatus(instanceConfigurationApplication.getKey("success"));
				planVO.setMessage("ProcessorMDTR.savePlan.success");
	        }else{
	        	planVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	planVO.setMessage("ProcessorMDTR.savePlan.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar el Plan .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.savePlan.MySQLConnectionException");
			throw processorMDTRException;
		} catch (PlanDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.savePlan.PlanDAOException");
			throw processorMDTRException;
		}
		return planVO;
	}
	
	public PlanVO updatePlan(PlanVO planVO) throws ProcessorMDTRException{
		try {
			PlanDAO planDAO = new PlanDAO();
			planDAO.update(planVO);
			if(planVO != null && planVO.getId() != null){
				planVO.setStatus(instanceConfigurationApplication.getKey("success"));
				planVO.setMessage("ProcessorMDTR.updatePlan.success");
	        }else{
	        	planVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	planVO.setMessage("ProcessorMDTR.updatePlan.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible actualizar el Plan .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updatePlan.MySQLConnectionException");
			throw processorMDTRException;
		} catch (PlanDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updatePlan.PlanDAOException");
			throw processorMDTRException;
		}
		return planVO;
	}
	
	public PlanVO deletePlan(PlanVO planVO) throws ProcessorMDTRException{
		try {
			PlanDAO planDAO = new PlanDAO();
			planDAO.delete(planVO);
			if(planVO != null && planVO.getId() != null){
				planVO.setStatus(instanceConfigurationApplication.getKey("success"));
				planVO.setMessage("ProcessorMDTR.deletePlan.success");
	        }else{
	        	planVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	planVO.setMessage("ProcessorMDTR.deletePlan.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible eliminar el Plan .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deletePlan.MySQLConnectionException");
			throw processorMDTRException;
		} catch (PlanDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deletePlan.PlanDAOException");
			throw processorMDTRException;
		}
		return planVO;
	}
	
	
}

