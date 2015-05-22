package au.com.billingbuddy.business.objects;

import java.util.ArrayList;

import au.com.billingbuddy.dao.objects.SubmittedProcessLogDAO;
import au.com.billingbuddy.exceptions.objects.AdministrationMDTRException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.SubmittedProcessLogDAOException;
import au.com.billingbuddy.vo.objects.SubmittedProcessLogVO;

public class AdministrationMDTR {
	
	private static AdministrationMDTR instance = null;
	
	public static synchronized AdministrationMDTR getInstance() {
		if (instance == null) {
			instance = new AdministrationMDTR();
		}
		return instance;
	}
	
	private AdministrationMDTR() {}
	
	public ArrayList<SubmittedProcessLogVO> listSubmittedProcessLogs(SubmittedProcessLogVO submittedProcessLogVO) throws AdministrationMDTRException {
		ArrayList<SubmittedProcessLogVO> listSubmittedProcessLogs = null;
		try {
			SubmittedProcessLogDAO submittedProcessLogDAO = new SubmittedProcessLogDAO();
			listSubmittedProcessLogs = submittedProcessLogDAO.search(submittedProcessLogVO);
		} catch (MySQLConnectionException e) {
			AdministrationMDTRException administrationMDTRException = new AdministrationMDTRException(e);
			administrationMDTRException.setErrorCode("AdministrationMDTR.proccesDailySubscriptions.MySQLConnectionException");
			throw administrationMDTRException;
		} catch (SubmittedProcessLogDAOException e) {
			AdministrationMDTRException administrationMDTRException = new AdministrationMDTRException(e);
			administrationMDTRException.setErrorCode("AdministrationMDTR.proccesDailySubscriptions.SubmittedProcessLogDAOException");
			throw administrationMDTRException;
		}
		return listSubmittedProcessLogs;
	}
	
	/*public ArrayList<SubmittedProcessLogVO> reprocessErrorFile(SubmittedProcessLogVO submittedProcessLogVO) throws AdministrationMDTRException {
		ArrayList<SubmittedProcessLogVO> listSubmittedProcessLogs = null;
		try {
			SubmittedProcessLogDAO submittedProcessLogDAO = new SubmittedProcessLogDAO();
			listSubmittedProcessLogs = submittedProcessLogDAO.search(submittedProcessLogVO);
		} catch (MySQLConnectionException e) {
			AdministrationMDTRException administrationMDTRException = new AdministrationMDTRException(e);
			administrationMDTRException.setErrorCode("AdministrationMDTR.proccesDailySubscriptions.MySQLConnectionException");
			throw administrationMDTRException;
		} catch (SubmittedProcessLogDAOException e) {
			AdministrationMDTRException administrationMDTRException = new AdministrationMDTRException(e);
			administrationMDTRException.setErrorCode("AdministrationMDTR.proccesDailySubscriptions.SubmittedProcessLogDAOException");
			throw administrationMDTRException;
		}
		return listSubmittedProcessLogs;
	}*/
	
}
