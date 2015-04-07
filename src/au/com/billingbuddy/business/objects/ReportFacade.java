package au.com.billingbuddy.business.objects;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

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
	
	public StringWriter searchAmountByDay(TransactionVO transactionVO, StreamSource xslStream, Map<String, String> mapConfiguration) throws ReportFacadeException{
		StringWriter report = null;
		try {
			report = reportMDTR.searchAmountByDay(transactionVO, xslStream, mapConfiguration);
		} catch (ReportMDTRException e) {
			ReportFacadeException reportFacadeException = new ReportFacadeException(e);
			reportFacadeException.setErrorCode(e.getErrorCode());
			throw reportFacadeException;
		}
		return report;
	}
	
	public StringWriter searchChargesByDay(TransactionVO transactionVO) throws ReportFacadeException{
		StringWriter report = null;
		try {
			report = reportMDTR.searchChargesByDay(transactionVO);
		} catch (ReportMDTRException e) {
			ReportFacadeException reportFacadeException = new ReportFacadeException(e);
			reportFacadeException.setErrorCode(e.getErrorCode());
			throw reportFacadeException;
		}
		return report;
	}

	public StringWriter searchRejectedByDay(TransactionVO transactionVO) throws ReportFacadeException{
		StringWriter report = null;
		try {
			report = reportMDTR.searchRejectedByDay(transactionVO);
		} catch (ReportMDTRException e) {
			ReportFacadeException reportFacadeException = new ReportFacadeException(e);
			reportFacadeException.setErrorCode(e.getErrorCode());
			throw reportFacadeException;
		}
		return report;
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
	
	public ArrayList<TransactionVO> searchTransactionsByDayFilter(TransactionVO transactionVO) throws ReportFacadeException{
		ArrayList<TransactionVO> listTransactionsByDay = null;
		try {
			listTransactionsByDay = reportMDTR.searchTransactionsByDayFilter(transactionVO);
		} catch (ReportMDTRException e) {
			ReportFacadeException reportFacadeException = new ReportFacadeException(e);
			reportFacadeException.setErrorCode(e.getErrorCode());
			throw reportFacadeException;
		}
		return listTransactionsByDay;
	}

}
