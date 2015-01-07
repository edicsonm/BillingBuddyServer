package au.com.billingbuddy.business.objects;

import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.exceptions.objects.ProcesorFacadeException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.PlanVO;
import au.com.billingbuddy.vo.objects.RefundVO;

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
			e.printStackTrace();
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
//			procesorFacadeException.setErrorMenssage(instanceConfigurationApplication.getKey(e.getErrorCode()));
			throw procesorFacadeException;
		}
		return chargeVO;
	}
	
	public ArrayList<RefundVO> listRefunds(RefundVO refundVO) throws ProcesorFacadeException{
		ArrayList<RefundVO> listRefunds = null;
		try {
			listRefunds = processorMDTR.listRefunds(refundVO);
		} catch (ProcessorMDTRException e) {
			e.printStackTrace();
		}
		return listRefunds;
	}
	
	public ArrayList<ChargeVO> listCharge(ChargeVO chargeVO) throws ProcesorFacadeException{
		ArrayList<ChargeVO> listCharges = null;
		try {
			listCharges = processorMDTR.listCharge(chargeVO);
		} catch (ProcessorMDTRException e) {
			e.printStackTrace();
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return listCharges;
	}
	
	public ArrayList<PlanVO> listPlans(PlanVO planVO) throws ProcesorFacadeException{
		ArrayList<PlanVO> listPlans = null;
		try {
			listPlans = processorMDTR.listPlans(planVO);
		} catch (ProcessorMDTRException e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
			ProcesorFacadeException procesorFacadeException = new ProcesorFacadeException(e);
			procesorFacadeException.setErrorCode(e.getErrorCode());
			throw procesorFacadeException;
		}
		return planVO;
	}

}
