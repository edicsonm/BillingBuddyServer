package au.com.billingbuddy.business.objects;

import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.exceptions.objects.ProcesorFacadeException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.vo.objects.BusinessTypeVO;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.CountryBlockListVO;
import au.com.billingbuddy.vo.objects.CountryRestrictionVO;
import au.com.billingbuddy.vo.objects.CountryVO;
import au.com.billingbuddy.vo.objects.CreditCardRestrictionVO;
import au.com.billingbuddy.vo.objects.IndustryVO;
import au.com.billingbuddy.vo.objects.MerchantConfigurationVO;
import au.com.billingbuddy.vo.objects.MerchantRestrictionVO;
import au.com.billingbuddy.vo.objects.MerchantVO;
import au.com.billingbuddy.vo.objects.PlanVO;
import au.com.billingbuddy.vo.objects.RefundVO;
import au.com.billingbuddy.vo.objects.SubscriptionVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

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
	
	public CountryRestrictionVO deleteCountryRestriction(CountryRestrictionVO countryRestrictionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.deleteCountryRestriction(countryRestrictionVO);
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
	
	public MerchantRestrictionVO deleteMerchantRestriction(MerchantRestrictionVO merchantRestrictionVO) throws ProcesorFacadeException{
		try {
			processorMDTR.deleteMerchantRestriction(merchantRestrictionVO);
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
	
	public ArrayList<MerchantVO> listMerchants() throws ProcesorFacadeException{
		ArrayList<MerchantVO> listMerchants = null;
		try {
			listMerchants = processorMDTR.listMerchants();
		} catch (ProcessorMDTRException e) {
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listMerchants;
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
	
	public MerchantVO deleteMerchant(MerchantVO merchantVO) throws ProcesorFacadeException{
		try {
			processorMDTR.deleteMerchant(merchantVO);
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
	
	public ArrayList<MerchantConfigurationVO> listMerchantConfigurations() throws ProcesorFacadeException{
		ArrayList<MerchantConfigurationVO> listMerchantConfigurations = null;
		try {
			listMerchantConfigurations = processorMDTR.listMerchantConfigurations();
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
			processorMDTR.validateMerchant(merchantVO);
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
	
}
