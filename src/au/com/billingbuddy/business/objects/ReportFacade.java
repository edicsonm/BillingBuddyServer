package au.com.billingbuddy.business.objects;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.ProcesorFacadeException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.ReportFacadeException;
import au.com.billingbuddy.exceptions.objects.ReportMDTRException;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

public class ReportFacade {
	
	private static ReportFacade instance = null;
	ReportMDTR reportMDTR = ReportMDTR.getInstance();
	
	public static synchronized ReportFacade getInstance() {
		if (instance == null) {
			instance = new ReportFacade();
		}
		return instance;
	}
	
	private ReportFacade() {}
	
	public ArrayList<TransactionVO> searchAmountByDay(TransactionVO transactionVO) throws ReportFacadeException{
		ArrayList<TransactionVO> listAmountByDay = null;
		try {
			listAmountByDay = reportMDTR.searchAmountByDay(transactionVO);
		} catch (ReportMDTRException e) {
			ReportFacadeException reportFacadeException = new ReportFacadeException(e);
			reportFacadeException.setErrorCode(e.getErrorCode());
			throw reportFacadeException;
		}
		return listAmountByDay;
	}
	
	public ArrayList<TransactionVO> searchChargesByDay(TransactionVO transactionVO) throws ReportFacadeException{
		ArrayList<TransactionVO> listChargesByDay = null;
		try {
			listChargesByDay = reportMDTR.searchChargesByDay(transactionVO);
		} catch (ReportMDTRException e) {
			ReportFacadeException reportFacadeException = new ReportFacadeException(e);
			reportFacadeException.setErrorCode(e.getErrorCode());
			throw reportFacadeException;
		}
		return listChargesByDay;
	}
	
	public ArrayList<TransactionVO> searchTransactionsByDay(TransactionVO transactionVO) throws ReportFacadeException{
		ArrayList<TransactionVO> listTransactionsByDay = null;
		try {
			listTransactionsByDay = reportMDTR.searchTransactionsByDay(transactionVO);
		} catch (ReportMDTRException e) {
			ReportFacadeException reportFacadeException = new ReportFacadeException(e);
			reportFacadeException.setErrorCode(e.getErrorCode());
			throw reportFacadeException;
		}
		return listTransactionsByDay;
	}

}
