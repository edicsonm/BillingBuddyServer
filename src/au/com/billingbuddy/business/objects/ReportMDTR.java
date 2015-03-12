package au.com.billingbuddy.business.objects;

import java.io.StringWriter;
import java.util.ArrayList;

import au.com.billingbuddy.business.objects.reports.ReporteAmountByDay;
import au.com.billingbuddy.business.objects.reports.ReporteChargesByDay;
import au.com.billingbuddy.business.objects.reports.ReporteRejectedByDay;
import au.com.billingbuddy.dao.objects.ChargeDAO;
import au.com.billingbuddy.dao.objects.TransactionDAO;
import au.com.billingbuddy.exceptions.objects.ChargeDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.ProcesorFacadeException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.ReportMDTRException;
import au.com.billingbuddy.exceptions.objects.ReporteAmountByDayException;
import au.com.billingbuddy.exceptions.objects.ReporteChargesByDayException;
import au.com.billingbuddy.exceptions.objects.ReporteRejectedByDayException;
import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.vo.objects.TransactionVO;

public class ReportMDTR {
	
	private static ReportMDTR instance = null;
	
	public static synchronized ReportMDTR getInstance() {
		if (instance == null) {
			instance = new ReportMDTR();
		}
		return instance;
	}
	
	private ReportMDTR() {}
	
	public StringWriter searchAmountByDay(TransactionVO transactionVO) throws ReportMDTRException{
		StringWriter report = null;
		try {
			TransactionDAO transactionDAO = new TransactionDAO();
			ReporteAmountByDay reporteAmountByDay = ReporteAmountByDay.getInstance();
			report = reporteAmountByDay.CreateXml(transactionDAO.searchAmountsByDay(transactionVO));
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ReportMDTRException reportMDTRException = new ReportMDTRException(e);
			reportMDTRException.setErrorCode("ReportMDTR.searchAmountByDay.MySQLConnectionException");
			throw reportMDTRException;
		} catch (TransactionDAOException e) {
			e.printStackTrace();
			ReportMDTRException reportMDTRException = new ReportMDTRException(e);
			reportMDTRException.setErrorCode("ReportMDTR.searchAmountByDay.TransactionDAOException");
			throw reportMDTRException;
		} catch (ReporteAmountByDayException e) {
			e.printStackTrace();
			ReportMDTRException reportMDTRException = new ReportMDTRException(e);
			reportMDTRException.setErrorCode("ReportMDTR.searchAmountByDay.ReporteAmountByDayException");
			throw reportMDTRException;
		}
		return report;
	}
	
	public StringWriter searchChargesByDay(TransactionVO transactionVO) throws ReportMDTRException{
		StringWriter report = null;
		try {
			TransactionDAO transactionDAO = new TransactionDAO();
			ReporteChargesByDay reporteChargesByDay = ReporteChargesByDay.getInstance();
			report = reporteChargesByDay.CreateXml(transactionDAO.searchChargesByDay(transactionVO));
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ReportMDTRException reportMDTRException = new ReportMDTRException(e);
			reportMDTRException.setErrorCode("ReportMDTR.searchChargesByDay.MySQLConnectionException");
			throw reportMDTRException;
		} catch (TransactionDAOException e) {
			e.printStackTrace();
			ReportMDTRException reportMDTRException = new ReportMDTRException(e);
			reportMDTRException.setErrorCode("ReportMDTR.searchChargesByDay.TransactionDAOException");
			throw reportMDTRException;
		} catch (ReporteChargesByDayException e) {
			e.printStackTrace();
			ReportMDTRException reportMDTRException = new ReportMDTRException(e);
			reportMDTRException.setErrorCode("ReportMDTR.searchChargesByDay.ReporteChargesByDayException");
			throw reportMDTRException;
		}
		return report;
	}
	
	public StringWriter searchRejectedByDay(TransactionVO transactionVO) throws ReportMDTRException{
		StringWriter report = null;
		try {
			TransactionDAO transactionDAO = new TransactionDAO();
			ReporteRejectedByDay reporteRejectedByDay = ReporteRejectedByDay.getInstance();
			report = reporteRejectedByDay.CreateXml(transactionDAO.searchRejectedByDay(transactionVO));
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ReportMDTRException reportMDTRException = new ReportMDTRException(e);
			reportMDTRException.setErrorCode("ReportMDTR.searchRejectedsByDay.MySQLConnectionException");
			throw reportMDTRException;
		} catch (TransactionDAOException e) {
			e.printStackTrace();
			ReportMDTRException reportMDTRException = new ReportMDTRException(e);
			reportMDTRException.setErrorCode("ReportMDTR.searchRejectedsByDay.TransactionDAOException");
			throw reportMDTRException;
		} catch (ReporteRejectedByDayException e) {
			e.printStackTrace();
			ReportMDTRException reportMDTRException = new ReportMDTRException(e);
			reportMDTRException.setErrorCode("ReportMDTR.searchRejectedsByDay.ReporteRejectedsByDayException");
			throw reportMDTRException;
		}
		return report;
	}
	
	public ArrayList<TransactionVO> searchTransactionsByDay(TransactionVO transactionVO) throws ReportMDTRException{
		ArrayList<TransactionVO> listTransactionsByDay = null;
		try {
			TransactionDAO transactionDAO = new TransactionDAO();
			listTransactionsByDay = transactionDAO.searchTransactionsByDay(transactionVO);
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			ReportMDTRException reportMDTRException = new ReportMDTRException(e);
			reportMDTRException.setErrorCode("ReportMDTR.searchTransactionsByDay.MySQLConnectionException");
			throw reportMDTRException;
		} catch (TransactionDAOException e) {
			e.printStackTrace();
			ReportMDTRException reportMDTRException = new ReportMDTRException(e);
			reportMDTRException.setErrorCode("ReportMDTR.searchTransactionsByDay.TransactionDAOException");
			throw reportMDTRException;
		}
		return listTransactionsByDay;
	}

}
