package au.com.billingbuddy.business.objects;

import java.util.ArrayList;

import au.com.billingbuddy.dao.objects.ChargeDAO;
import au.com.billingbuddy.dao.objects.TransactionDAO;
import au.com.billingbuddy.exceptions.objects.ChargeDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.ProcesorFacadeException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.ReportMDTRException;
import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.vo.objects.ChargeVO;
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
	
	public ArrayList<TransactionVO> searchAmountByDay(TransactionVO transactionVO) throws ReportMDTRException{
		ArrayList<TransactionVO> listAmountByDay = null;
		try {
			TransactionDAO transactionDAO = new TransactionDAO();
			listAmountByDay = transactionDAO.searchAmountsByDay(transactionVO);
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
		}
		return listAmountByDay;
	}
	
	public ArrayList<TransactionVO> searchChargesByDay(TransactionVO transactionVO) throws ReportMDTRException{
		ArrayList<TransactionVO> listChargesByDay = null;
		try {
			TransactionDAO transactionDAO = new TransactionDAO();
			listChargesByDay = transactionDAO.searchChargesByDay(transactionVO);
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
		}
		return listChargesByDay;
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
