package au.com.billingbuddy.business.objects;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;

import au.com.billingbuddy.business.objects.reports.ReporteAmountByDay;
import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.Currency;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.objects.BusinessTypeDAO;
import au.com.billingbuddy.dao.objects.CardDAO;
import au.com.billingbuddy.dao.objects.ChargeDAO;
import au.com.billingbuddy.dao.objects.CountryBlockListDAO;
import au.com.billingbuddy.dao.objects.CountryDAO;
import au.com.billingbuddy.dao.objects.CountryRestrictionDAO;
import au.com.billingbuddy.dao.objects.CreditCardRestrictionDAO;
import au.com.billingbuddy.dao.objects.CurrencyDAO;
import au.com.billingbuddy.dao.objects.CustomerDAO;
import au.com.billingbuddy.dao.objects.IndustryDAO;
import au.com.billingbuddy.dao.objects.MerchantConfigurationDAO;
import au.com.billingbuddy.dao.objects.MerchantDAO;
import au.com.billingbuddy.dao.objects.MerchantRestrictionDAO;
import au.com.billingbuddy.dao.objects.PlanDAO;
import au.com.billingbuddy.dao.objects.RefundDAO;
import au.com.billingbuddy.dao.objects.RejectedChargeDAO;
import au.com.billingbuddy.dao.objects.SubscriptionDAO;
import au.com.billingbuddy.dao.objects.TransactionDAO;
import au.com.billingbuddy.dao.objects.UserMerchantDAO;
import au.com.billingbuddy.exceptions.objects.BusinessTypeDAOException;
import au.com.billingbuddy.exceptions.objects.CardDAOException;
import au.com.billingbuddy.exceptions.objects.ChargeDAOException;
import au.com.billingbuddy.exceptions.objects.CountryBlockListDAOException;
import au.com.billingbuddy.exceptions.objects.CountryDAOException;
import au.com.billingbuddy.exceptions.objects.CountryRestrictionDAOException;
import au.com.billingbuddy.exceptions.objects.CreditCardRestrictionDAOException;
import au.com.billingbuddy.exceptions.objects.CurrencyDAOException;
import au.com.billingbuddy.exceptions.objects.CustomerDAOException;
import au.com.billingbuddy.exceptions.objects.IndustryDAOException;
import au.com.billingbuddy.exceptions.objects.MerchantConfigurationDAOException;
import au.com.billingbuddy.exceptions.objects.MerchantDAOException;
import au.com.billingbuddy.exceptions.objects.MerchantRestrictionDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.MySQLTransactionException;
import au.com.billingbuddy.exceptions.objects.PlanDAOException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.RefundDAOException;
import au.com.billingbuddy.exceptions.objects.RejectedChargeDAOException;
import au.com.billingbuddy.exceptions.objects.ReportMDTRException;
import au.com.billingbuddy.exceptions.objects.ReporteAmountByDayException;
import au.com.billingbuddy.exceptions.objects.SubscriptionDAOException;
import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.exceptions.objects.UserMerchantDAOException;
import au.com.billingbuddy.vo.objects.BusinessTypeVO;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.CountryBlockListVO;
import au.com.billingbuddy.vo.objects.CountryRestrictionVO;
import au.com.billingbuddy.vo.objects.CountryVO;
import au.com.billingbuddy.vo.objects.CreditCardRestrictionVO;
import au.com.billingbuddy.vo.objects.CurrencyVO;
import au.com.billingbuddy.vo.objects.CustomerVO;
import au.com.billingbuddy.vo.objects.IndustryVO;
import au.com.billingbuddy.vo.objects.MerchantConfigurationVO;
import au.com.billingbuddy.vo.objects.MerchantRestrictionVO;
import au.com.billingbuddy.vo.objects.MerchantVO;
import au.com.billingbuddy.vo.objects.PlanVO;
import au.com.billingbuddy.vo.objects.RefundVO;
import au.com.billingbuddy.vo.objects.RejectedChargeVO;
import au.com.billingbuddy.vo.objects.SubscriptionVO;
import au.com.billingbuddy.vo.objects.TransactionVO;
import au.com.billingbuddy.vo.objects.UserMerchantVO;

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
			try {
				RejectedChargeVO rejectedChargeVO = new RejectedChargeVO();
				rejectedChargeVO.setTransactionId(transactionVO.getId());
				rejectedChargeVO.setAmount(Utilities.currencyToStripe(transactionVO.getOrderAmount(), Currency.USD));
				rejectedChargeVO.setCurrency(transactionVO.getOrderCurrency());
				rejectedChargeVO.setCardNumber(transactionVO.getCardVO().getNumber());
				rejectedChargeVO.setExpYear(transactionVO.getCardVO().getExpYear());
				rejectedChargeVO.setExpMonth(transactionVO.getCardVO().getExpMonth());
				rejectedChargeVO.setCardHolderName(transactionVO.getCardVO().getName());
				rejectedChargeVO.setFailureCode(e.getCode());
				rejectedChargeVO.setFailureMessage(e.getMessage());
				RejectedChargeDAO rejectedChargeDAO = new RejectedChargeDAO();
				rejectedChargeDAO.insert(rejectedChargeVO);
			} catch (MySQLConnectionException ex) {
				ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(ex);
				processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.MySQLConnectionException");
				throw processorMDTRException;
			} catch (RejectedChargeDAOException ex) {
				ex.printStackTrace();
				ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(ex);
				processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.RejectedChargesDAOException");
				throw processorMDTRException;
			}
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.chargePayment.CardException." + e.getCode());
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
		if(charge != null){
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
		}else{
			System.out.println(charge);
		}
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
		MySQLTransaction mySQLTransaction = null;
		try {
			
			mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			
			Charge charge = Charge.retrieve(chargeVO.getStripeId());
			params.put("amount", Utilities.currencyToStripe(chargeVO.getRefundVO().getAmount(), chargeVO.getCurrency().toUpperCase()));
			Refund refund = charge.getRefunds().create(params);
			Utilities.copyRefundToChargeVO(chargeVO, refund);
			RefundDAO refundDAO = new RefundDAO(mySQLTransaction);
			refundDAO.insert(chargeVO.getRefundVO());
			
			if(chargeVO.getRefundVO() != null && chargeVO.getRefundVO().getId() != null){
				ChargeDAO chargeDAO = new ChargeDAO(mySQLTransaction);
				if(chargeDAO.updateStatusRefund(chargeVO) != 0){
					chargeVO.setStatus(instanceConfigurationApplication.getKey("success"));
					chargeVO.setMessage("ProcessorMDTR.processRefound.success");
					mySQLTransaction.commit();
				}else{
		        	chargeVO.setStatus(instanceConfigurationApplication.getKey("failure"));
					chargeVO.setMessage("ProcessorMDTR.processRefound.failure");
					System.out.println("#################################################################");
		        	System.out.println("No fue posible registrar el Refund .... ");
		        	System.out.println("#################################################################");
		        	mySQLTransaction.rollback();
		        }
	        }else{
	        	chargeVO.setStatus(instanceConfigurationApplication.getKey("failure"));
				chargeVO.setMessage("ProcessorMDTR.processRefound.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar el Refund .... ");
	        	System.out.println("#################################################################");
	        	mySQLTransaction.rollback();
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
		} catch (MySQLTransactionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.processRefound.MySQLTransactionException");
			throw processorMDTRException;
		} catch (ChargeDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.processRefound.ChargeDAOException");
			throw processorMDTRException;
		}finally{
			try {
				if(mySQLTransaction != null){
					mySQLTransaction.close();
				}
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
			}
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

/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
	public ArrayList<PlanVO> listPlans(PlanVO planVO) throws ProcessorMDTRException {
		ArrayList<PlanVO> listPlans = null;
		try {
			PlanDAO planDAO = new PlanDAO();
			listPlans = planDAO.search(planVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listPlans.MySQLConnectionException");
			throw processorMDTRException;
		} catch (PlanDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listPlans.PlanDAOException");
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
	
/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
	public ArrayList<SubscriptionVO> listSubscriptions(SubscriptionVO subscriptionVO) throws ProcessorMDTRException {
		ArrayList<SubscriptionVO> listSubscriptions = null;
		try {
			SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
			listSubscriptions = subscriptionDAO.search(subscriptionVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listSubscription.MySQLConnectionException");
			throw processorMDTRException;
		} catch (SubscriptionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listSubscription.SubscriptionDAOException");
			throw processorMDTRException;
		}
		return listSubscriptions;
	}
	
	public SubscriptionVO saveSubscription(SubscriptionVO subscriptionVO) throws ProcessorMDTRException{
		try {
			SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
			subscriptionDAO.insert(subscriptionVO);
			if(subscriptionVO != null && subscriptionVO.getId() != null){
				subscriptionVO.setStatus(instanceConfigurationApplication.getKey("success"));
				subscriptionVO.setMessage("ProcessorMDTR.saveSubscription.success");
	        }else{
	        	subscriptionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	subscriptionVO.setMessage("ProcessorMDTR.saveSubscription.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar la Subscription  .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveSubscription.MySQLConnectionException");
			throw processorMDTRException;
		} catch (SubscriptionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveSubscription.SubscriptionDAOException");
			throw processorMDTRException;
		}
		return subscriptionVO;
	}
	
	public SubscriptionVO updateSubscription(SubscriptionVO subscriptionVO) throws ProcessorMDTRException{
		try {
			SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
			subscriptionDAO.update(subscriptionVO);
			if(subscriptionVO != null && subscriptionVO.getId() != null){
				subscriptionVO.setStatus(instanceConfigurationApplication.getKey("success"));
				subscriptionVO.setMessage("ProcessorMDTR.updateSubscription.success");
	        }else{
	        	subscriptionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	subscriptionVO.setMessage("ProcessorMDTR.updateSubscription.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible actualizar la Subscription .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateSubscription.MySQLConnectionException");
			throw processorMDTRException;
		} catch (SubscriptionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateSubscription.SubscriptionDAOException");
			throw processorMDTRException;
		}
		return subscriptionVO;
	}
	
	public SubscriptionVO deleteSubscription(SubscriptionVO subscriptionVO) throws ProcessorMDTRException{
		try {
			SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
			subscriptionDAO.delete(subscriptionVO);
			if(subscriptionVO != null && subscriptionVO.getId() != null){
				subscriptionVO.setStatus(instanceConfigurationApplication.getKey("success"));
				subscriptionVO.setMessage("ProcessorMDTR.deleteSubscription.success");
	        }else{
	        	subscriptionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	subscriptionVO.setMessage("ProcessorMDTR.deleteSubscription.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible eliminar la Subscription .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deleteSubscription.MySQLConnectionException");
			throw processorMDTRException;
		} catch (SubscriptionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deleteSubscription.SubscriptionDAOException");
			throw processorMDTRException;
		}
		return subscriptionVO;
	}

	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<CountryRestrictionVO> listCountryRestrictions(CountryRestrictionVO countryRestrictionVO) throws ProcessorMDTRException {
		ArrayList<CountryRestrictionVO> listCountryRestrictions = null;
		try {
			CountryRestrictionDAO countryRestrictionDAO = new CountryRestrictionDAO();
			listCountryRestrictions = countryRestrictionDAO.search(countryRestrictionVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCountryRestrictions.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CountryRestrictionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCountryRestrictions.CountryRestrictionDAOException");
			throw processorMDTRException;
		}
		return listCountryRestrictions;
	}
	
	public CountryRestrictionVO saveCountryRestriction(CountryRestrictionVO countryRestrictionVO) throws ProcessorMDTRException{
		try {
			CountryRestrictionDAO countryRestrictionDAO = new CountryRestrictionDAO();
			countryRestrictionDAO.insert(countryRestrictionVO);
			if(countryRestrictionVO != null && countryRestrictionVO.getId() != null){
				countryRestrictionVO.setStatus(instanceConfigurationApplication.getKey("success"));
				countryRestrictionVO.setMessage("ProcessorMDTR.saveCountryRestriction.success");
	        }else{
	        	countryRestrictionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	countryRestrictionVO.setMessage("ProcessorMDTR.saveCountryRestriction.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar la Restriccion por pais.... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveCountryRestriction.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CountryRestrictionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveCountryRestriction.CountryRestrictionDAOException");
			throw processorMDTRException;
		}
		return countryRestrictionVO;
	}
	
	public CountryRestrictionVO updateCountryRestriction(CountryRestrictionVO countryRestrictionVO) throws ProcessorMDTRException{
		try {
			CountryRestrictionDAO countryRestrictionDAO = new CountryRestrictionDAO();
			countryRestrictionDAO.update(countryRestrictionVO);
			if(countryRestrictionVO != null && countryRestrictionVO.getId() != null){
				countryRestrictionVO.setStatus(instanceConfigurationApplication.getKey("success"));
				countryRestrictionVO.setMessage("ProcessorMDTR.updateCountryRestriction.success");
	        }else{
	        	countryRestrictionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	countryRestrictionVO.setMessage("ProcessorMDTR.updateCountryRestriction.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible actualizar la Restriccion por pais .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateCountryRestriction.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CountryRestrictionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateCountryRestriction.CountryRestrictionDAOException");
			throw processorMDTRException;
		}
		return countryRestrictionVO;
	}
	
	public CountryRestrictionVO deleteCountryRestriction(CountryRestrictionVO countryRestrictionVO) throws ProcessorMDTRException{
		try {
			CountryRestrictionDAO countryRestrictionDAO = new CountryRestrictionDAO();
			countryRestrictionDAO.delete(countryRestrictionVO);
			if(countryRestrictionVO != null && countryRestrictionVO.getId() != null){
				countryRestrictionVO.setStatus(instanceConfigurationApplication.getKey("success"));
				countryRestrictionVO.setMessage("ProcessorMDTR.deleteCountryRestriction.success");
	        }else{
	        	countryRestrictionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	countryRestrictionVO.setMessage("ProcessorMDTR.deleteCountryRestriction.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible eliminar la Restriccion por Pais .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deleteCountryRestriction.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CountryRestrictionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deleteCountryRestriction.CountryRestrictionDAOException");
			throw processorMDTRException;
		}
		return countryRestrictionVO;
	}
		
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<CountryVO> listCountries() throws ProcessorMDTRException {
		ArrayList<CountryVO> listCountries = null;
		try {
			CountryDAO countryDAO = new CountryDAO();
			listCountries = countryDAO.search();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCountries.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CountryDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCountries.CountryDAOException");
			throw processorMDTRException;
		}
		return listCountries;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<MerchantRestrictionVO> listMerchantRestrictions(MerchantRestrictionVO merchantRestrictionVO) throws ProcessorMDTRException {
		ArrayList<MerchantRestrictionVO> listMerchantRestrictions = null;
		try {
			MerchantRestrictionDAO merchantRestrictionDAO = new MerchantRestrictionDAO();
			listMerchantRestrictions = merchantRestrictionDAO.search(merchantRestrictionVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerchantRestrictions.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantRestrictionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerchantRestrictions.MerchantRestrictionDAOException");
			throw processorMDTRException;
		}
		return listMerchantRestrictions;
	}
	
	public MerchantRestrictionVO saveMerchantRestriction(MerchantRestrictionVO merchantRestrictionVO) throws ProcessorMDTRException{
		try {
			MerchantRestrictionDAO merchantRestrictionDAO = new MerchantRestrictionDAO();
			merchantRestrictionDAO.insert(merchantRestrictionVO);
			if(merchantRestrictionVO != null && merchantRestrictionVO.getId() != null){
				merchantRestrictionVO.setStatus(instanceConfigurationApplication.getKey("success"));
				merchantRestrictionVO.setMessage("ProcessorMDTR.saveMerchantRestriction.success");
	        }else{
	        	merchantRestrictionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	merchantRestrictionVO.setMessage("ProcessorMDTR.saveMerchantRestriction.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar la Restriccion por Merchant.... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveMerchantRestriction.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantRestrictionDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveMerchantRestriction.MerchantRestrictionDAOException"+ (!Utilities.isNullOrEmpty(e.getSqlObjectName())? ("."+e.getSqlObjectName()):""));
			throw processorMDTRException;
		}
		return merchantRestrictionVO;
	}
	
	public MerchantRestrictionVO updateMerchantRestriction(MerchantRestrictionVO merchantRestrictionVO) throws ProcessorMDTRException{
		try {
			MerchantRestrictionDAO merchantRestrictionDAO = new MerchantRestrictionDAO();
			merchantRestrictionDAO.update(merchantRestrictionVO);
			if(merchantRestrictionVO != null && merchantRestrictionVO.getId() != null){
				merchantRestrictionVO.setStatus(instanceConfigurationApplication.getKey("success"));
				merchantRestrictionVO.setMessage("ProcessorMDTR.updateMerchantRestriction.success");
	        }else{
	        	merchantRestrictionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	merchantRestrictionVO.setMessage("ProcessorMDTR.updateMerchantRestriction.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible actualizar la Restriccion por Merchant .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateMerchantRestriction.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantRestrictionDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateMerchantRestriction.MerchantRestrictionDAOException"+ (!Utilities.isNullOrEmpty(e.getSqlObjectName())? ("."+e.getSqlObjectName()):""));
			throw processorMDTRException;
		}
		return merchantRestrictionVO;
	}
	
	public MerchantRestrictionVO deleteMerchantRestriction(MerchantRestrictionVO merchantRestrictionVO) throws ProcessorMDTRException{
		try {
			MerchantRestrictionDAO merchantRestrictionDAO = new MerchantRestrictionDAO();
			merchantRestrictionDAO.delete(merchantRestrictionVO);
			if(merchantRestrictionVO != null && merchantRestrictionVO.getId() != null){
				merchantRestrictionVO.setStatus(instanceConfigurationApplication.getKey("success"));
				merchantRestrictionVO.setMessage("ProcessorMDTR.deleteMerchantRestriction.success");
	        }else{
	        	merchantRestrictionVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	merchantRestrictionVO.setMessage("ProcessorMDTR.deleteMerchantRestriction.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible eliminar la Restriccion por Merchant .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deleteMerchantRestriction.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantRestrictionDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deleteMerchantRestriction.MerchantRestrictionDAOException");
			throw processorMDTRException;
		}
		return merchantRestrictionVO;
	}	

	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<MerchantVO> listMerchants(MerchantVO merchantVO) throws ProcessorMDTRException {
		ArrayList<MerchantVO> listMerchants = null;
		try {
			MerchantDAO merchantDAO = new MerchantDAO();
			listMerchants = merchantDAO.search(merchantVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerhants.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerhants.MerchantDAOException");
			throw processorMDTRException;
		}
		return listMerchants;
	}
	
	public ArrayList<MerchantVO> searchMerchantsToConfigure(MerchantVO merchantVO) throws ProcessorMDTRException {
		ArrayList<MerchantVO> listMerchants = null;
		try {
			MerchantDAO merchantDAO = new MerchantDAO();
			listMerchants = merchantDAO.searchMerchantsToConfigure(merchantVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.searchMerchantsToConfigure.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.searchMerchantsToConfigure.MerchantDAOException");
			throw processorMDTRException;
		}
		return listMerchants;
	}
	
	public MerchantVO listMerchantDetails(MerchantVO merchantVO) throws ProcessorMDTRException{
		try {
			MerchantDAO merchantDAO = new MerchantDAO();
			merchantDAO.searchDetail(merchantVO);
			if(merchantVO != null && !Utilities.isNullOrEmpty(merchantVO.getId())){
				MerchantRestrictionVO merchantRestrictionVO = new MerchantRestrictionVO();
				merchantRestrictionVO.setId(merchantVO.getId());
				MerchantRestrictionDAO merchantRestrictionDAO = new MerchantRestrictionDAO();
				merchantVO.setListMerchantRestrictionsVO(merchantRestrictionDAO.searchDetails(merchantRestrictionVO));
				
				MerchantConfigurationVO merchantConfigurationVO = new MerchantConfigurationVO();
				merchantConfigurationVO.setMerchantId(merchantVO.getId());
				MerchantConfigurationDAO merchantConfigurationDAO = new MerchantConfigurationDAO();
				merchantConfigurationDAO.searchDetailByMerchantId(merchantConfigurationVO);
				merchantVO.setMerchantConfigurationVO(merchantConfigurationVO);
			}
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerchantDetail.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerchantDetail.MerchantDAOException");
			throw processorMDTRException;
		} catch (MerchantRestrictionDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerchantDetail.MerchantRestrictionDAOException");
			throw processorMDTRException;
		} catch (MerchantConfigurationDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerchantDetail.MerchantConfigurationDAOException");
			throw processorMDTRException;
		}
		return merchantVO;
	}
	
	public MerchantVO saveMerchant(MerchantVO merchantVO) throws ProcessorMDTRException{
		try {
			MerchantDAO merchantDAO = new MerchantDAO();
			merchantDAO.insert(merchantVO);
			if(merchantVO != null && merchantVO.getId() != null){
				merchantVO.setStatus(instanceConfigurationApplication.getKey("success"));
				merchantVO.setMessage("ProcessorMDTR.saveMerchant.success");
	        }else{
	        	merchantVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	merchantVO.setMessage("ProcessorMDTR.saveMerchant.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar el Merchant.... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveMerchant.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveMerchant.MerchantDAOException");
			throw processorMDTRException;
		}
		return merchantVO;
	}
	
	public MerchantVO updateMerchant(MerchantVO merchantVO) throws ProcessorMDTRException{
		try {
			MerchantDAO merchantDAO = new MerchantDAO();
			merchantDAO.update(merchantVO);
			if(merchantVO != null && merchantVO.getId() != null){
				merchantVO.setStatus(instanceConfigurationApplication.getKey("success"));
				merchantVO.setMessage("ProcessorMDTR.updateMerchant.success");
	        }else{
	        	merchantVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	merchantVO.setMessage("ProcessorMDTR.updateMerchant.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible actualizar el Merchant .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateMerchant.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateMerchant.MerchantDAOException");
			throw processorMDTRException;
		}
		return merchantVO;
	}
	
	public MerchantVO deleteMerchant(MerchantVO merchantVO) throws ProcessorMDTRException{
		try {
			MerchantDAO merchantDAO = new MerchantDAO();
			merchantDAO.delete(merchantVO);
			if(merchantVO != null && merchantVO.getId() != null){
				merchantVO.setStatus(instanceConfigurationApplication.getKey("success"));
				merchantVO.setMessage("ProcessorMDTR.deleteMerchant.success");
	        }else{
	        	merchantVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	merchantVO.setMessage("ProcessorMDTR.deleteMerchant.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible eliminar el Merchant .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deleteMerchant.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deleteMerchant.MerchantDAOException");
			throw processorMDTRException;
		}
		return merchantVO;
	}


	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
	public ArrayList<MerchantConfigurationVO> listMerchantConfigurations(MerchantConfigurationVO merchantConfigurationVO) throws ProcessorMDTRException {
		ArrayList<MerchantConfigurationVO> listMerchantConfigurations = null;
		try {
			MerchantConfigurationDAO merchantConfigurationDAO = new MerchantConfigurationDAO();
			listMerchantConfigurations = merchantConfigurationDAO.searchByUserID(merchantConfigurationVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerchantConfiguration.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantConfigurationDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerchantConfiguration.MerchantConfigurationDAOException");
			throw processorMDTRException;
		}
		return listMerchantConfigurations;
	}

	public MerchantConfigurationVO listMerchantConfigurationDetail(MerchantConfigurationVO merchantConfigurationVO) throws ProcessorMDTRException{
		try {
			MerchantConfigurationDAO merchantConfigurationDAO = new MerchantConfigurationDAO();
			merchantConfigurationDAO.searchDetail(merchantConfigurationVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerchantConfigurationDetail.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantConfigurationDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listMerchantConfigurationDetail.MerchantConfigurationDAOException");
			throw processorMDTRException;
		}
		return merchantConfigurationVO;
	}	
	
	public MerchantConfigurationVO saveMerchantConfiguration(MerchantConfigurationVO merchantConfigurationVO) throws ProcessorMDTRException{
		try {
			MerchantConfigurationDAO merchantConfigurationDAO = new MerchantConfigurationDAO();
			merchantConfigurationDAO.insert(merchantConfigurationVO);
			if(merchantConfigurationVO != null && merchantConfigurationVO.getId() != null){
				merchantConfigurationVO.setStatus(instanceConfigurationApplication.getKey("success"));
				merchantConfigurationVO.setMessage("ProcessorMDTR.saveMerchantConfiguration.success");
	        }else{
	        	merchantConfigurationVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	merchantConfigurationVO.setMessage("ProcessorMDTR.saveMerchantConfiguration.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar la Configuracion del Merchant.... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveMerchantConfiguration.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantConfigurationDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveMerchantConfiguration.MerchantConfigurationDAOException"+ (!Utilities.isNullOrEmpty(e.getSqlObjectName())? ("."+e.getSqlObjectName()):""));
			throw processorMDTRException;
		}
		return merchantConfigurationVO;
	}
	
	public MerchantConfigurationVO updateMerchantConfiguration(MerchantConfigurationVO merchantConfigurationVO) throws ProcessorMDTRException{
		try {
			MerchantConfigurationDAO merchantConfigurationDAO = new MerchantConfigurationDAO();
			if(merchantConfigurationDAO.update(merchantConfigurationVO) >= 1){
				merchantConfigurationVO.setStatus(instanceConfigurationApplication.getKey("success"));
				merchantConfigurationVO.setMessage("ProcessorMDTR.updateMerchantConfiguration.success");
	        }else{
	        	merchantConfigurationVO.setStatus(instanceConfigurationApplication.getKey("failure"));
	        	merchantConfigurationVO.setMessage("ProcessorMDTR.updateMerchantConfiguration.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible actualizar la Configuracion del Merchant .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateMerchantConfiguration.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantConfigurationDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateMerchantConfiguration.MerchantConfigurationDAOException");
			throw processorMDTRException;
		}
		return merchantConfigurationVO;
	}

	public MerchantVO validateMerchant(MerchantVO merchantVO) throws ProcessorMDTRException{
		MySQLTransaction mySQLTransaction = null;
		try {
			mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			MerchantDAO merchantDAO = new MerchantDAO(mySQLTransaction);
			merchantVO = merchantDAO.searchDetail(merchantVO);
			if(merchantVO != null && !Utilities.isNullOrEmpty(merchantVO.getId())){
				MerchantRestrictionVO merchantRestrictionVO = new MerchantRestrictionVO();
				merchantRestrictionVO.setMerchantId(merchantVO.getId());
				MerchantRestrictionDAO merchantRestrictionDAO = new MerchantRestrictionDAO(mySQLTransaction);
				ArrayList<MerchantRestrictionVO> listMerchantRestrictionsVO = merchantRestrictionDAO.searchDetails(merchantRestrictionVO);
				for (MerchantRestrictionVO merchantRestrictionVO2 : listMerchantRestrictionsVO) {
					if(merchantRestrictionVO2.getConcept().equalsIgnoreCase("Transactions")) {
						merchantVO.setTimeUnit(merchantRestrictionVO2.getTimeUnit());
						merchantDAO.verifyRestrictionByTransactions(merchantVO);
						if(!Utilities.isNullOrEmpty(merchantVO.getNumberTransactions()) && Integer.parseInt(merchantVO.getNumberTransactions()) > Double.parseDouble(merchantRestrictionVO2.getValue())){
							ProcessorMDTRException processorMDTRException = new ProcessorMDTRException("");
							processorMDTRException.setErrorCode("ProcessorMDTR.validateMerchant.MerchantRestrictionDAOException.RestrictionByTransactions");
							throw processorMDTRException;
						}
					}else if(merchantRestrictionVO2.getConcept().equalsIgnoreCase("Amount")) {
						merchantVO.setTimeUnit(merchantRestrictionVO2.getTimeUnit());
						merchantDAO.verifyRestrictionByAmount(merchantVO);
						if(!Utilities.isNullOrEmpty(merchantVO.getAmountTransactions()) && (Double.parseDouble(merchantVO.getAmountTransactions()) > Double.parseDouble(merchantRestrictionVO2.getValue()))){
							ProcessorMDTRException processorMDTRException = new ProcessorMDTRException("");
							processorMDTRException.setErrorCode("ProcessorMDTR.validateMerchant.MerchantRestrictionDAOException.RestrictionByAmount");
							throw processorMDTRException;
						}
					}
				}
				
				MerchantConfigurationVO merchantConfigurationVO = new MerchantConfigurationVO();
				merchantConfigurationVO.setMerchantId(merchantVO.getId());
				MerchantConfigurationDAO merchantConfigurationDAO = new MerchantConfigurationDAO(mySQLTransaction);
				merchantConfigurationVO = merchantConfigurationDAO.searchDetailByMerchantId(merchantConfigurationVO);
				if(merchantConfigurationVO != null) {
					merchantVO.setMerchantConfigurationVO(merchantConfigurationVO);
				}else{
					merchantVO.setMerchantConfigurationVO(null);
					merchantVO.setStatus(instanceConfigurationApplication.getKey("failure"));
					merchantVO.setMessage("ProcessorMDTR.validateMerchant.MerchantRestrictionDAOException.MisconfigureMerchant");
//					ProcessorMDTRException processorMDTRException = new ProcessorMDTRException("");
//					processorMDTRException.setErrorCode("ProcessorMDTR.validateMerchant.MerchantRestrictionDAOException.MisconfigureMerchant");
//					throw processorMDTRException;
				}
			}
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.validateMerchant.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.validateMerchant.MerchantDAOException");
			throw processorMDTRException;
		} catch (MerchantConfigurationDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.validateMerchant.MerchantConfigurationDAOException");
			throw processorMDTRException;
		} catch (MerchantRestrictionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.validateMerchant.MerchantRestrictionDAOException");
			throw processorMDTRException;
		} catch (MySQLTransactionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.validateMerchant.MySQLTransactionException");
			throw processorMDTRException;
		}finally{
			try {
				if(mySQLTransaction != null){
					mySQLTransaction.end();
				}
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
			}
		}
		return merchantVO;
	}

	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<CreditCardRestrictionVO> listCreditCardRestrictions() throws ProcessorMDTRException {
		ArrayList<CreditCardRestrictionVO> listCreditCardRestrictions = null;
		try {
			CreditCardRestrictionDAO creditCardRestrictionDAO = new CreditCardRestrictionDAO();
			listCreditCardRestrictions = creditCardRestrictionDAO.search();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCreditCardRestrictions.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CreditCardRestrictionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCreditCardRestrictions.CreditCardRestrictionDAOException");
			throw processorMDTRException;
		}
		return listCreditCardRestrictions;
	}
	
	public CreditCardRestrictionVO saveCreditCardRestriction(CreditCardRestrictionVO creditCardRestrictionVO) throws ProcessorMDTRException{
		try {
			CreditCardRestrictionDAO creditCardRestrictionDAO = new CreditCardRestrictionDAO();
			creditCardRestrictionDAO.insert(creditCardRestrictionVO);
			if(creditCardRestrictionVO != null && creditCardRestrictionVO.getId() != null){
				creditCardRestrictionVO.setStatus(ConfigurationApplication.getKey("success"));
				creditCardRestrictionVO.setMessage("ProcessorMDTR.saveCreditCardRestriction.success");
	        }else{
	        	creditCardRestrictionVO.setStatus(ConfigurationApplication.getKey("failure"));
	        	creditCardRestrictionVO.setMessage("ProcessorMDTR.saveCountryRestriction.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar la Restriccion por Tarjeta de Credito.... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveCreditCardRestriction.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CreditCardRestrictionDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveCreditCardRestriction.CreditCardRestrictionDAOException"+ (!Utilities.isNullOrEmpty(e.getSqlObjectName())? ("."+e.getSqlObjectName()):""));
			throw processorMDTRException;
		}
		return creditCardRestrictionVO;
	}
	
	public CreditCardRestrictionVO updateCreditCardRestriction(CreditCardRestrictionVO creditCardRestrictionVO) throws ProcessorMDTRException{
		try {
			CreditCardRestrictionDAO creditCardRestrictionDAO = new CreditCardRestrictionDAO();
			creditCardRestrictionDAO.update(creditCardRestrictionVO);
			if(creditCardRestrictionVO != null && creditCardRestrictionVO.getId() != null){
				creditCardRestrictionVO.setStatus(ConfigurationApplication.getKey("success"));
				creditCardRestrictionVO.setMessage("ProcessorMDTR.updateCreditCardRestriction.success");
	        }else{
	        	creditCardRestrictionVO.setStatus(ConfigurationApplication.getKey("failure"));
	        	creditCardRestrictionVO.setMessage("ProcessorMDTR.updateCreditCardRestriction.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible actualizar la Restriccion por Tarjeta de Credito .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateCountryRestriction.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CreditCardRestrictionDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateCountryRestriction.CreditCardRestrictionDAOException");
			throw processorMDTRException;
		}
		return creditCardRestrictionVO;
	}
	
	public CreditCardRestrictionVO deleteCreditCardRestriction(CreditCardRestrictionVO creditCardRestrictionVO) throws ProcessorMDTRException{
		try {
			CreditCardRestrictionDAO creditCardRestrictionDAO = new CreditCardRestrictionDAO();
			creditCardRestrictionDAO.delete(creditCardRestrictionVO);
			if(creditCardRestrictionVO != null && creditCardRestrictionVO.getId() != null){
				creditCardRestrictionVO.setStatus(ConfigurationApplication.getKey("success"));
				creditCardRestrictionVO.setMessage("ProcessorMDTR.deleteCreditCardRestriction.success");
	        }else{
	        	creditCardRestrictionVO.setStatus(ConfigurationApplication.getKey("failure"));
	        	creditCardRestrictionVO.setMessage("ProcessorMDTR.deleteCreditCardRestriction.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible eliminar la Restriccion por Tarjeta de Credito .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deleteCreditCardRestriction.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CreditCardRestrictionDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.deleteCreditCardRestriction.CreditCardRestrictionDAOException");
			throw processorMDTRException;
		}
		return creditCardRestrictionVO;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<CountryBlockListVO> listCountryBlockList() throws ProcessorMDTRException {
		ArrayList<CountryBlockListVO> listCountryBlockList = null;
		try {
			CountryBlockListDAO countryBlockListDAO = new CountryBlockListDAO();
			listCountryBlockList = countryBlockListDAO.search();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCountryBlockList.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CountryBlockListDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCountryBlockList.CountryBlockListDAOException");
			throw processorMDTRException;
		}
		return listCountryBlockList;
	}
	
	public ArrayList<CountryBlockListVO> updateCountryBlockList(ArrayList<CountryBlockListVO> listCountryBlockListOriginal, ArrayList<CountryBlockListVO> listCountryBlockList) throws ProcessorMDTRException {
		MySQLTransaction mySQLTransaction = null;
		try {
			mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			CountryBlockListDAO countryBlockListDAO = new CountryBlockListDAO(mySQLTransaction);
			for (int i = 0; i < listCountryBlockList.size(); i++) {
				/*CountryBlockListVO countryBlockListVOOriginal = (CountryBlockListVO)listCountryBlockListOriginal.get(i);*/
				CountryBlockListVO countryBlockListVO = (CountryBlockListVO)listCountryBlockList.get(i);
					
				if(countryBlockListVO.getId() == null){
					//Insert
					countryBlockListDAO.insert(countryBlockListVO);
				}else{
					//Update
					countryBlockListDAO.update(countryBlockListVO);
				}
				
				/*if( !countryBlockListVOOriginal.getTransaction().equalsIgnoreCase(countryBlockListVO.getTransaction())||
				    !countryBlockListVOOriginal.getMerchantServerLocation().equalsIgnoreCase(countryBlockListVO.getMerchantServerLocation())||
				    !countryBlockListVOOriginal.getMerchantRegistrationLocation().equalsIgnoreCase(countryBlockListVO.getMerchantRegistrationLocation())||
				    !countryBlockListVOOriginal.getCreditCardIssueLocation().equalsIgnoreCase(countryBlockListVO.getCreditCardIssueLocation())||
				    !countryBlockListVOOriginal.getCreditCardHolderLocation().equalsIgnoreCase(countryBlockListVO.getCreditCardHolderLocation())){
					//Existe un elemento diferente
					if(countryBlockListVO.getId().equalsIgnoreCase("null")){
						//Insert
						countryBlockListDAO.insert(countryBlockListVO);
					}else{
						//Update
						countryBlockListDAO.update(countryBlockListVO);
					}
				}*/
				
				/*countryBlockListVO.getTransaction();
				countryBlockListVO.getMerchantServerLocation();
				countryBlockListVO.getMerchantRegistrationLocation();
				countryBlockListVO.getCreditCardIssueLocation();
				countryBlockListVO.getCreditCardHolderLocation();*/
			}
			listCountryBlockList = countryBlockListDAO.search();
			mySQLTransaction.commit();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateCountryBlockList.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CountryBlockListDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateCountryBlockList.CountryBlockListDAOException");
			throw processorMDTRException;
		} catch (MySQLTransactionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateCountryBlockList.MySQLTransactionException");
			throw processorMDTRException;
		}finally{
			try {
				if(mySQLTransaction != null){
					mySQLTransaction.end();
				}
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
			}
		}
		return listCountryBlockList;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<BusinessTypeVO> listBusinessTypes() throws ProcessorMDTRException {
		ArrayList<BusinessTypeVO> listBusinessTypes = null;
		try {
			BusinessTypeDAO businessTypeDAO = new BusinessTypeDAO();
			listBusinessTypes = businessTypeDAO.search();
		} catch (MySQLConnectionException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listBusinessTypes.MySQLConnectionException");
			throw processorMDTRException;
		} catch (BusinessTypeDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listBusinessTypes.BusinessTypeDAOException");
			throw processorMDTRException;
		}
		return listBusinessTypes;
	}	
	
	public MerchantVO listBusinessTypeDetails(BusinessTypeVO businessTypeVO) throws ProcessorMDTRException{
		return null;
	}
	
	public BusinessTypeVO saveBusinessType(BusinessTypeVO businessTypeVO) throws ProcessorMDTRException{
		try {
			BusinessTypeDAO businessTypeDAO = new BusinessTypeDAO();
			businessTypeDAO.insert(businessTypeVO);
			if(businessTypeVO != null && businessTypeVO.getId() != null){
				businessTypeVO.setStatus(ConfigurationApplication.getKey("success"));
				businessTypeVO.setMessage("ProcessorMDTR.saveBusinessType.success");
	        }else{
	        	businessTypeVO.setStatus(ConfigurationApplication.getKey("failure"));
	        	businessTypeVO.setMessage("ProcessorMDTR.saveBusinessType.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar el Business Type.... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveBusinessType.MySQLConnectionException");
			throw processorMDTRException;
		} catch (BusinessTypeDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveBusinessType.BusinessTypeDAOException");
			throw processorMDTRException;
		}
		return businessTypeVO;
	}
	
	public BusinessTypeVO updateBusinessType(BusinessTypeVO businessTypeVO) throws ProcessorMDTRException{
		try {
			BusinessTypeDAO businessTypeDAO = new BusinessTypeDAO();
			businessTypeDAO.update(businessTypeVO);
			if(businessTypeVO != null && businessTypeVO.getId() != null){
				businessTypeVO.setStatus(ConfigurationApplication.getKey("success"));
				businessTypeVO.setMessage("ProcessorMDTR.updateBusinessType.success");
	        }else{
	        	businessTypeVO.setStatus(ConfigurationApplication.getKey("failure"));
	        	businessTypeVO.setMessage("ProcessorMDTR.updateBusinessType.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible actualizar el Business Type .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateBusinessType.MySQLConnectionException");
			throw processorMDTRException;
		} catch (BusinessTypeDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateBusinessType.BusinessTypeDAOException");
			throw processorMDTRException;
		}
		return businessTypeVO;
	}
	
	public BusinessTypeVO deleteBusinessType(BusinessTypeVO businessTypeVO) throws ProcessorMDTRException{
		return null;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<IndustryVO> listIndustries() throws ProcessorMDTRException {
		ArrayList<IndustryVO> listIndustries = null;
		try {
			IndustryDAO industryDAO = new IndustryDAO();
			listIndustries = industryDAO.search();
		} catch (MySQLConnectionException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listIndustries.MySQLConnectionException");
			throw processorMDTRException;
		} catch (IndustryDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listIndustries.IndustryDAOException");
			throw processorMDTRException;
		}
		return listIndustries;
	}	
	
	public IndustryVO listIndustryDetails(IndustryVO industryVO) throws ProcessorMDTRException{
		return null;
	}
	
	public IndustryVO saveIndustry(IndustryVO industryVO) throws ProcessorMDTRException{
		try {
			IndustryDAO industryDAO = new IndustryDAO();
			industryDAO.insert(industryVO);
			if(industryVO != null && industryVO.getId() != null){
				industryVO.setStatus(ConfigurationApplication.getKey("success"));
				industryVO.setMessage("ProcessorMDTR.saveIndustry.success");
	        }else{
	        	industryVO.setStatus(ConfigurationApplication.getKey("failure"));
	        	industryVO.setMessage("ProcessorMDTR.saveIndustry.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar la Industry .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveIndustry.MySQLConnectionException");
			throw processorMDTRException;
		} catch (IndustryDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveIndustry.IndustryDAOException");
			throw processorMDTRException;
		}
		return industryVO;
	}
	
	public IndustryVO updateIndustry(IndustryVO industryVO) throws ProcessorMDTRException{
		try {
			IndustryDAO industryDAO = new IndustryDAO();
			industryDAO.update(industryVO);
			if(industryVO != null && industryVO.getId() != null){
				industryVO.setStatus(ConfigurationApplication.getKey("success"));
				industryVO.setMessage("ProcessorMDTR.updateIndustry.success");
	        }else{
	        	industryVO.setStatus(ConfigurationApplication.getKey("failure"));
	        	industryVO.setMessage("ProcessorMDTR.updateIndustry.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible actualizar la Industry .... ");
	        	System.out.println("#################################################################");
	        }
		} catch (MySQLConnectionException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateIndustry.MySQLConnectionException");
			throw processorMDTRException;
		} catch (IndustryDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.updateIndustry.IndustryDAOException");
			throw processorMDTRException;
		}
		return industryVO;
	}
	
	public IndustryVO deleteIndustry(IndustryVO industryVO) throws ProcessorMDTRException{
		return null;
	}
	
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<TransactionVO> searchAmountByDay(TransactionVO transactionVO) throws ProcessorMDTRException{
		ArrayList<TransactionVO> listAmountsByDay = null;
		try {
			TransactionDAO transactionDAO = new TransactionDAO();
			listAmountsByDay = transactionDAO.searchAmountsByDay(transactionVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.searchAmountByDay.MySQLConnectionException");
			throw processorMDTRException;
		} catch (TransactionDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.searchAmountByDay.TransactionDAOException");
			throw processorMDTRException;
		}
		return listAmountsByDay;
	}
	
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<CurrencyVO> listCurrencies() throws ProcessorMDTRException{
		ArrayList<CurrencyVO> listCurrencies = null;
		try {
			CurrencyDAO currencyDAO = new CurrencyDAO();
			listCurrencies = currencyDAO.search();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCurrencies.MySQLConnectionException");
			throw processorMDTRException;
		} catch (CurrencyDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listCurrencies.CurrencyDAOException");
			throw processorMDTRException;
		}
		return listCurrencies;
	}
	
	public ArrayList<CurrencyVO> searchCurrency(CurrencyVO currencyVO) throws ProcessorMDTRException{
		return null;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public UserMerchantVO saveUserMerchant(UserMerchantVO userMerchantVO) throws ProcessorMDTRException{
		MySQLTransaction mySQLTransaction = null;
		try {
			mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			
			MerchantDAO merchantDAO = new MerchantDAO(mySQLTransaction);
			if(merchantDAO.insertBasicInformation(userMerchantVO.getMerchantVO()) != 0){
				userMerchantVO.setMerchantId(userMerchantVO.getMerchantVO().getId());
				UserMerchantDAO userMerchantDAO = new UserMerchantDAO(mySQLTransaction);
				if(userMerchantDAO.insert(userMerchantVO) != 0){
					mySQLTransaction.commit();
					userMerchantVO.setStatus(ConfigurationApplication.getKey("success"));
					userMerchantVO.setMessage("ProcessorMDTR.saveUserMerchant.success");
				} else {
					mySQLTransaction.rollback();
					userMerchantVO.setStatus(ConfigurationApplication.getKey("failure"));
					userMerchantVO.setMessage("ProcessorMDTR.saveUserMerchant.failure");
					System.out.println("#################################################################");
		        	System.out.println("No fue posible registrar el merchant para el usuario .... ");
		        	System.out.println("#################################################################");
				}
			} else {
				mySQLTransaction.rollback();
				userMerchantVO.setStatus(ConfigurationApplication.getKey("failure"));
				userMerchantVO.setMessage("ProcessorMDTR.saveUserMerchant.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible registrar el merchant para el usuario .... ");
	        	System.out.println("#################################################################");
			}
		} catch (MySQLConnectionException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveIndustry.MySQLConnectionException");
			throw processorMDTRException;
		} catch (MerchantDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveUserMerchant.MerchantDAOException");
			throw processorMDTRException;
		} catch (UserMerchantDAOException e) {
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveUserMerchant.UserMerchantDAOException");
			throw processorMDTRException;
		} catch (MySQLTransactionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.saveUserMerchant.MySQLTransactionException");
			throw processorMDTRException;
		}finally{
			try {
				if(mySQLTransaction != null){
					mySQLTransaction.close();
				}
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
			}
		}
		return userMerchantVO;
	}
	
	public ArrayList<UserMerchantVO> listUserMerchants(UserMerchantVO userMerchantVO) throws ProcessorMDTRException{
		ArrayList<UserMerchantVO> listUserMerchants = null;
		try {
			UserMerchantDAO userMerchantDAO = new UserMerchantDAO();
			listUserMerchants = userMerchantDAO.search(userMerchantVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listUserMerchants.MySQLConnectionException");
			throw processorMDTRException;
		} catch (UserMerchantDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.listUserMerchants.UserMerchantDAOException");
			throw processorMDTRException;
		}
		return listUserMerchants;
	}
	
	public UserMerchantVO rechargeAdministratorAccess(UserMerchantVO userMerchantVO) throws ProcessorMDTRException{
		try {
			UserMerchantDAO userMerchantDAO = new UserMerchantDAO();
			if(userMerchantDAO.rechargeAdministratorAccess(userMerchantVO) != 0){
				userMerchantVO.setStatus(ConfigurationApplication.getKey("success"));
				userMerchantVO.setMessage("ProcessorMDTR.rechargeAdministratorAccess.success");
			} else {
				userMerchantVO.setStatus(ConfigurationApplication.getKey("failure"));
				userMerchantVO.setMessage("ProcessorMDTR.rechargeAdministratorAccess.failure");
				System.out.println("#################################################################");
	        	System.out.println("No fue posible recargar los accesos para el usuario administrador .... ");
	        	System.out.println("#################################################################");
			}
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.rechargeAdministratorAccess.MySQLConnectionException");
			throw processorMDTRException;
		} catch (UserMerchantDAOException e) {
			e.printStackTrace();
			ProcessorMDTRException processorMDTRException = new ProcessorMDTRException(e);
			processorMDTRException.setErrorCode("ProcessorMDTR.rechargeAdministratorAccess.UserMerchantDAOException");
			throw processorMDTRException;
		}
		return userMerchantVO;
	}
	
}

