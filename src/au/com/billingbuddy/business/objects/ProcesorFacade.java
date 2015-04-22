package au.com.billingbuddy.business.objects;

import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.exceptions.objects.ProcesorFacadeException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.vo.objects.BusinessTypeVO;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.CountryBlockListVO;
import au.com.billingbuddy.vo.objects.CountryRestrictionVO;
import au.com.billingbuddy.vo.objects.CountryVO;
import au.com.billingbuddy.vo.objects.CreditCardRestrictionVO;
import au.com.billingbuddy.vo.objects.CurrencyVO;
import au.com.billingbuddy.vo.objects.IndustryVO;
import au.com.billingbuddy.vo.objects.MerchantConfigurationVO;
import au.com.billingbuddy.vo.objects.MerchantCustomerVO;
import au.com.billingbuddy.vo.objects.MerchantRestrictionVO;
import au.com.billingbuddy.vo.objects.MerchantVO;
import au.com.billingbuddy.vo.objects.PlanVO;
import au.com.billingbuddy.vo.objects.RefundVO;
import au.com.billingbuddy.vo.objects.SubscriptionVO;
import au.com.billingbuddy.vo.objects.TransactionVO;
import au.com.billingbuddy.vo.objects.UserMerchantVO;

public class ProcesorFacade {
	
	private static ProcesorFacade instance = null;
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();
	ProcessorMDTR processorMDTR = ProcessorMDTR.getInstance();
	
	public static synchronized ProcesorFacade getInstance() {
		if (instance == null) {
			instance = new ProcesorFacade();
		}
		return instance;
	}
	
	private ProcesorFacade() {}

	public ChargeVO processRefund(ChargeVO chargeVO) throws ProcesorFacadeException{
		try {
			processorMDTR.processRefund(chargeVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return chargeVO;
	}
	
	public ArrayList<RefundVO> listRefunds(RefundVO refundVO) throws ProcesorFacadeException{
		ArrayList<RefundVO> listRefunds = null;
		try {
			listRefunds = processorMDTR.listRefunds(refundVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listRefunds;
	}
	
	public ArrayList<ChargeVO> listCharge(ChargeVO chargeVO) throws ProcesorFacadeException{
		ArrayList<ChargeVO> listCharges = null;
		try {
			listCharges = processorMDTR.listCharge(chargeVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listCharges;
	}
	
	public ArrayList<ChargeVO> listChargeByDayFiter(ChargeVO chargeVO) throws ProcesorFacadeException{
		ArrayList<ChargeVO> listCharges = null;
		try {
			listCharges = processorMDTR.listChargeByDayFiter(chargeVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listCharges;
	}
	
	public ChargeVO listChargeDetail(ChargeVO chargeVO) throws ProcesorFacadeException{
		try {
			chargeVO = processorMDTR.listChargeDetail(chargeVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return chargeVO;
	}
	
	public ArrayList<TransactionVO> searchTransactionsByCustomer(TransactionVO transactionVO) throws ProcesorFacadeException{
		ArrayList<TransactionVO> listTransactionsByCustomer = null;
		try {
			listTransactionsByCustomer = processorMDTR.searchTransactionsByCustomer(transactionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listTransactionsByCustomer;
	}
	

/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
	
	public ArrayList<PlanVO> listPlans(PlanVO planVO) throws ProcesorFacadeException{
		ArrayList<PlanVO> listPlans = null;
		try {
			listPlans = processorMDTR.listPlans(planVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listPlans;
	}
	
	public PlanVO savePlan(PlanVO planVO) throws ProcesorFacadeException{
		try {
			processorMDTR.savePlan(planVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return planVO;
	}
	
	public PlanVO updatePlan(PlanVO planVO) throws ProcesorFacadeException{
		try {
			processorMDTR.updatePlan(planVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return planVO;
	}
	
	public PlanVO deletePlan(PlanVO planVO) throws ProcesorFacadeException{
		try {
			processorMDTR.deletePlan(planVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return planVO;
	}

/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
	
	public ArrayList<SubscriptionVO> listSubscriptions(SubscriptionVO subscriptionVO) throws ProcesorFacadeException{
		ArrayList<SubscriptionVO> listSubscriptions = null;
		try {
			listSubscriptions = processorMDTR.listSubscriptions(subscriptionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listSubscriptions;
	}
	
	public SubscriptionVO saveSubscription(SubscriptionVO subscriptionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.saveSubscription(subscriptionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return subscriptionVO;
	}
	
	public SubscriptionVO updateSubscription(SubscriptionVO subscriptionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.updateSubscription(subscriptionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return subscriptionVO;
	}
	
	public SubscriptionVO deleteSubscription(SubscriptionVO subscriptionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.deleteSubscription(subscriptionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return subscriptionVO;
	}

/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
	
	public ArrayList<CountryRestrictionVO> listCountryRestrictions(CountryRestrictionVO countryRestrictionVO) throws ProcesorFacadeException{
		ArrayList<CountryRestrictionVO> listCountryRestrictions = null;
		try {
			listCountryRestrictions = processorMDTR.listCountryRestrictions(countryRestrictionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listCountryRestrictions;
	}
	
	public CountryRestrictionVO saveCountryRestriction(CountryRestrictionVO countryRestrictionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.saveCountryRestriction(countryRestrictionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return countryRestrictionVO;
	}
	
	public CountryRestrictionVO updateCountryRestriction(CountryRestrictionVO countryRestrictionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.updateCountryRestriction(countryRestrictionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return countryRestrictionVO;
	}
	
	public CountryRestrictionVO changeStatusCountryRestriction(CountryRestrictionVO countryRestrictionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.changeStatusCountryRestriction(countryRestrictionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return countryRestrictionVO;
	}

/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
	
	public ArrayList<CountryVO> listCountries() throws ProcesorFacadeException{
		ArrayList<CountryVO> listCountries = null;
		try {
			listCountries = processorMDTR.listCountries();
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listCountries;
	}	

/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
	
	public ArrayList<MerchantRestrictionVO> listMerchantRestrictions(MerchantRestrictionVO merchantRestrictionVO) throws ProcesorFacadeException{
		ArrayList<MerchantRestrictionVO> listMerchantRestrictions = null;
		try {
			listMerchantRestrictions = processorMDTR.listMerchantRestrictions(merchantRestrictionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listMerchantRestrictions;
	}
	
	public MerchantRestrictionVO saveMerchantRestriction(MerchantRestrictionVO merchantRestrictionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.saveMerchantRestriction(merchantRestrictionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantRestrictionVO;
	}
	
	public MerchantRestrictionVO updateMerchantRestriction(MerchantRestrictionVO merchantRestrictionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.updateMerchantRestriction(merchantRestrictionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantRestrictionVO;
	}
	
	public MerchantRestrictionVO changeStatusMerchantRestriction(MerchantRestrictionVO merchantRestrictionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.changeStatusMerchantRestriction(merchantRestrictionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantRestrictionVO;
	}

/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
	
	public ArrayList<MerchantVO> listMerchants(MerchantVO merchantVO) throws ProcesorFacadeException{
		ArrayList<MerchantVO> listMerchants = null;
		try {
			listMerchants = processorMDTR.listMerchants(merchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listMerchants;
	}
	
	public ArrayList<MerchantVO> listAllMerchants(MerchantVO merchantVO) throws ProcesorFacadeException{
		ArrayList<MerchantVO> listMerchants = null;
		try {
			listMerchants = processorMDTR.listAllMerchants(merchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listMerchants;
	}
	
	public ArrayList<MerchantVO> listAllMerchantsFilter(MerchantVO merchantVO) throws ProcesorFacadeException{
		ArrayList<MerchantVO> listMerchants = null;
		try {
			listMerchants = processorMDTR.listAllMerchantsFilter(merchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listMerchants;
	}
	
	public ArrayList<MerchantVO> searchMerchantsToConfigure(MerchantVO merchantVO) throws ProcesorFacadeException{
		ArrayList<MerchantVO> listMerchants = null;
		try {
			listMerchants = processorMDTR.searchMerchantsToConfigure(merchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listMerchants;
	}
	
	public MerchantVO searchMerchantDetailsUpdateProfile(MerchantVO merchantVO) throws ProcesorFacadeException{
		try {
			merchantVO = processorMDTR.searchMerchantDetailsUpdateProfile(merchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantVO;
	}
	
	public MerchantVO listMerchantDetails(MerchantVO merchantVO) throws ProcesorFacadeException{
		try {
			merchantVO = processorMDTR.listMerchantDetails(merchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantVO;
	}	

	public MerchantVO saveMerchant(MerchantVO merchantVO) throws ProcesorFacadeException{
		try {
			processorMDTR.saveMerchant(merchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantVO;
	}
	
	public MerchantVO updateMerchant(MerchantVO merchantVO) throws ProcesorFacadeException{
		try {
			processorMDTR.updateMerchant(merchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantVO;
	}
	
	public MerchantVO changeStatusMerchant(MerchantVO merchantVO) throws ProcesorFacadeException{
		try {
			processorMDTR.changeStatusMerchant(merchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantVO;
	}	

/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
/**********************************************************************************************************************************/
	
	public ArrayList<MerchantConfigurationVO> listMerchantConfigurations(MerchantConfigurationVO merchantConfigurationVO) throws ProcesorFacadeException{
		ArrayList<MerchantConfigurationVO> listMerchantConfigurations = null;
		try {
			listMerchantConfigurations = processorMDTR.listMerchantConfigurations(merchantConfigurationVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listMerchantConfigurations;
	}
	
	public MerchantConfigurationVO listMerchantConfigurationDetail(MerchantConfigurationVO merchantConfigurationVO) throws ProcesorFacadeException{
		try {
			merchantConfigurationVO = processorMDTR.listMerchantConfigurationDetail(merchantConfigurationVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantConfigurationVO;
	}	

	public MerchantConfigurationVO saveMerchantConfiguration(MerchantConfigurationVO merchantConfigurationVO) throws ProcesorFacadeException{
		try {
			processorMDTR.saveMerchantConfiguration(merchantConfigurationVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantConfigurationVO;
	}
	
	public MerchantConfigurationVO updateMerchantConfiguration(MerchantConfigurationVO merchantConfigurationVO) throws ProcesorFacadeException{
		try {
			processorMDTR.updateMerchantConfiguration(merchantConfigurationVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantConfigurationVO;
	}
	
	public MerchantVO validateMerchant(MerchantVO merchantVO) throws ProcesorFacadeException{
		try {
			merchantVO = processorMDTR.validateMerchant(merchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return merchantVO;
	}

	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
	public ArrayList<CreditCardRestrictionVO> listCreditCardRestrictions() throws ProcesorFacadeException{
		ArrayList<CreditCardRestrictionVO> listCreditCardRestrictions = null;
		try {
			listCreditCardRestrictions = processorMDTR.listCreditCardRestrictions();
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listCreditCardRestrictions;
	}
	
	public CreditCardRestrictionVO saveCreditCardRestriction(CreditCardRestrictionVO creditCardRestrictionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.saveCreditCardRestriction(creditCardRestrictionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return creditCardRestrictionVO;
	}
	
	public CreditCardRestrictionVO updateCreditCardRestriction(CreditCardRestrictionVO creditCardRestrictionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.updateCreditCardRestriction(creditCardRestrictionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return creditCardRestrictionVO;
	}
	
	public CreditCardRestrictionVO deleteCreditCardRestriction(CreditCardRestrictionVO creditCardRestrictionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.deleteCreditCardRestriction(creditCardRestrictionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return creditCardRestrictionVO;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
	public ArrayList<CountryBlockListVO> listCountryBlockList() throws ProcesorFacadeException{
		ArrayList<CountryBlockListVO> listCountryBlockList = null;
		try {
			listCountryBlockList = processorMDTR.listCountryBlockList();
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listCountryBlockList;
	}
	
	public ArrayList<CountryBlockListVO> updateCountryBlockList(ArrayList<CountryBlockListVO> listCountryBlockListOriginal, ArrayList<CountryBlockListVO> listCountryBlockList) throws ProcesorFacadeException{
		try {
			listCountryBlockList = processorMDTR.updateCountryBlockList(listCountryBlockListOriginal, listCountryBlockList);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listCountryBlockList;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
	public ArrayList<BusinessTypeVO> listBusinessTypes() throws ProcesorFacadeException{
		ArrayList<BusinessTypeVO> listBusinessTypes = null;
		try {
			listBusinessTypes = processorMDTR.listBusinessTypes();
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listBusinessTypes;
	}
	
	public BusinessTypeVO listBusinessTypeDetails(BusinessTypeVO businessTypeVO) throws ProcesorFacadeException{
		return null;
	}
	
	public BusinessTypeVO saveBusinessType(BusinessTypeVO businessTypeVO) throws ProcesorFacadeException{
		try {
			processorMDTR.saveBusinessType(businessTypeVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return businessTypeVO;
	}
	
	public BusinessTypeVO updateBusinessType(BusinessTypeVO businessTypeVO) throws ProcesorFacadeException{
		try {
			processorMDTR.updateBusinessType(businessTypeVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return businessTypeVO;
	}
	
	public BusinessTypeVO deleteBusinessType(BusinessTypeVO businessTypeVO) throws ProcesorFacadeException{
		try {
			processorMDTR.deleteBusinessType(businessTypeVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return businessTypeVO;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
	public ArrayList<IndustryVO> listIndustries() throws ProcesorFacadeException{
		ArrayList<IndustryVO> listIndustries = null;
		try {
			listIndustries = processorMDTR.listIndustries();
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listIndustries;
	}
	
	public IndustryVO listIndustryDetails(IndustryVO industryVO) throws ProcesorFacadeException{
		return null;
	}
	
	public IndustryVO saveIndustry(IndustryVO industryVO) throws ProcesorFacadeException{
		try {
			processorMDTR.saveIndustry(industryVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return industryVO;
	}
	
	public IndustryVO updateIndustry(IndustryVO industryVO) throws ProcesorFacadeException{
		try {
			processorMDTR.updateIndustry(industryVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return industryVO;
	}
	
	public IndustryVO deleteIndustry(IndustryVO industryVO) throws ProcesorFacadeException{
		try {
			processorMDTR.deleteIndustry(industryVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return industryVO;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
	public ArrayList<TransactionVO> searchAmountByDay(TransactionVO transactionVO) throws ProcesorFacadeException{
		ArrayList<TransactionVO> listAmountsByDay = null;
		try {
			listAmountsByDay = processorMDTR.searchAmountByDay(transactionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listAmountsByDay;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
	public ArrayList<TransactionVO> searchRejectedByDay(TransactionVO transactionVO) throws ProcesorFacadeException{
		ArrayList<TransactionVO> listRejectedsByDay = null;
		try {
			listRejectedsByDay = processorMDTR.searchRejectedByDay(transactionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listRejectedsByDay;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
	public ArrayList<TransactionVO> searchChargesByDay(TransactionVO transactionVO) throws ProcesorFacadeException{
		ArrayList<TransactionVO> listAmountsByDay = null;
		try {
			listAmountsByDay = processorMDTR.searchChargesByDay(transactionVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listAmountsByDay;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
	public ArrayList<CurrencyVO> listCurrencies() throws ProcesorFacadeException{
		ArrayList<CurrencyVO> listCurrencies = null;
		try {
			listCurrencies = processorMDTR.listCurrencies();
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listCurrencies;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	
	public UserMerchantVO saveUserMerchant(UserMerchantVO userMerchantVO) throws ProcesorFacadeException{
		try {
			processorMDTR.saveUserMerchant(userMerchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return userMerchantVO;
	}
	
	public ArrayList<UserMerchantVO> listUserMerchants(UserMerchantVO userMerchantVO) throws ProcesorFacadeException{
		ArrayList<UserMerchantVO> listUserMerchants = null;
		try {
			listUserMerchants = processorMDTR.listUserMerchants(userMerchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listUserMerchants;
	}
	
	public UserMerchantVO rechargeAdministratorAccess(UserMerchantVO userMerchantVO) throws ProcesorFacadeException{
		try {
			processorMDTR.rechargeAdministratorAccess(userMerchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return userMerchantVO;
	}
	
	public UserMerchantVO searchMerchantUser(UserMerchantVO userMerchantVO) throws ProcesorFacadeException{
		try {
			processorMDTR.searchMerchantUser(userMerchantVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return userMerchantVO;
	}
	
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<MerchantCustomerVO> listMerchantsCustomer(MerchantCustomerVO merchantCustomerVO) throws ProcesorFacadeException{
		ArrayList<MerchantCustomerVO> listMerchantsCustomer = null;
		try {
			listMerchantsCustomer = processorMDTR.listMerchantsCustomer(merchantCustomerVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listMerchantsCustomer;
	}
	
	public ArrayList<MerchantCustomerVO> listCustomersMerchant(MerchantCustomerVO merchantCustomerVO) throws ProcesorFacadeException{
		ArrayList<MerchantCustomerVO> listCustomersMerchant = null;
		try {
			listCustomersMerchant = processorMDTR.listCustomersMerchant(merchantCustomerVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listCustomersMerchant;
	}
	
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	/**********************************************************************************************************************************/
	public ArrayList<CardVO> listCardsByCustomer(CardVO cardVO) throws ProcesorFacadeException{
		ArrayList<CardVO> listCardsByCustomer = null;
		try {
			listCardsByCustomer = processorMDTR.listCardsByCustomer(cardVO);
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listCardsByCustomer;
	}
}
