package au.com.billingbuddy.dao.objects;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.connection.objects.MySQLConnection;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.interfaces.ISubmittedProcessLogDAO;
import au.com.billingbuddy.exceptions.objects.SubmittedProcessLogDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.SubmittedProcessLogVO;

public class SubmittedProcessLogDAO extends MySQLConnection implements ISubmittedProcessLogDAO {

	public SubmittedProcessLogDAO() throws MySQLConnectionException{
		super();
	}
	
	public SubmittedProcessLogDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}


	public int insert(SubmittedProcessLogVO submittedProcessLogVO) throws SubmittedProcessLogDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_SUBMITTED_PROCESS_LOG(?,?,?,?)}");
			cstmt.setString(1,submittedProcessLogVO.getProcessName());
			cstmt.setString(2,submittedProcessLogVO.getStartTime());
			cstmt.setString(3,submittedProcessLogVO.getStatusProcess());
			//cstmt.setString(3,submittedProcessLogVO.getId());
			status = cstmt.executeUpdate();
			submittedProcessLogVO.setId(cstmt.getString(4));
		} catch (SQLException e) {
			throw new SubmittedProcessLogDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}
	
	public int update(SubmittedProcessLogVO submittedProcessLogVO) throws SubmittedProcessLogDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_SUBMITTED_PROCESS_LOG(?,?,?,?)}");
			cstmt.setString(1,submittedProcessLogVO.getEndTime());
			cstmt.setString(2,submittedProcessLogVO.getStatusProcess());
			cstmt.setString(3,submittedProcessLogVO.getInformation());
			cstmt.setString(4,submittedProcessLogVO.getId());
			status = cstmt.executeUpdate();
		} catch (SQLException e) {
			throw new SubmittedProcessLogDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}
	
	public ArrayList<SubmittedProcessLogVO> search() throws SubmittedProcessLogDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<SubmittedProcessLogVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_SUBMITTED_PROCESS_LOG(?,?,?,?)}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<SubmittedProcessLogVO>();
				while (resultSet.next()) {
					SubmittedProcessLogVO submittedProcessLogVO = new SubmittedProcessLogVO();
//					dailySubscriptionVO.setId(resultSet.getString("Dasu_ID"));
//					dailySubscriptionVO.setSubscriptionId(resultSet.getString("Subs_ID"));
//					dailySubscriptionVO.setMerchantId(resultSet.getString("Merc_ID"));
//					dailySubscriptionVO.setQuantity(resultSet.getString("Dasu_Quantity"));
//					dailySubscriptionVO.setAmount(resultSet.getString("Dasu_Amount"));
//					dailySubscriptionVO.setCurrency(resultSet.getString("Dasu_Currency"));
//					dailySubscriptionVO.setMerchantCustomerCardVO(new MerchantCustomerCardVO());
//					dailySubscriptionVO.getMerchantCustomerCardVO().setCardVO(new CardVO());
//					dailySubscriptionVO.getMerchantCustomerCardVO().setCardId(resultSet.getString("Card_ID"));
//					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setId(resultSet.getString("Card_ID"));
//					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setNumber(resultSet.getString("Card_Number"));
//					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setExpMonth(resultSet.getString("Card_ExpMonth"));
//					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setExpYear(resultSet.getString("Card_ExpYear"));
//					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setCvv(resultSet.getString("Card_Cvv"));
//					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setName(resultSet.getString("Card_Name"));
					list.add(submittedProcessLogVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubmittedProcessLogDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}
	
	public ArrayList<SubmittedProcessLogVO> search(SubmittedProcessLogVO submittedProcessLogVO) throws SubmittedProcessLogDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<SubmittedProcessLogVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_SUBMITTED_PROCESS_LOG(?,?,?,?,?,?)}");
			pstmt.setString(1,submittedProcessLogVO.getProcessName());
			pstmt.setString(2,submittedProcessLogVO.getInitialDateReport());
			pstmt.setString(3,submittedProcessLogVO.getFinalDateReport());
			pstmt.setString(4,submittedProcessLogVO.getStatusProcess());
			pstmt.setString(5,submittedProcessLogVO.getMatch());
			pstmt.setString(6,submittedProcessLogVO.getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<SubmittedProcessLogVO>();
				while (resultSet.next()) {
					SubmittedProcessLogVO submittedProcessLogVOAUX = new SubmittedProcessLogVO();
					submittedProcessLogVOAUX.setId(resultSet.getString("Splo_ID"));
					submittedProcessLogVOAUX.setProcessName(resultSet.getString("Splo_ProcessName"));
					submittedProcessLogVOAUX.setStartTime(resultSet.getString("Splo_StartTime"));
					submittedProcessLogVOAUX.setEndTime(resultSet.getString("Splo_EndTime"));
					submittedProcessLogVOAUX.setStatusProcess(resultSet.getString("Splo_StatusProcess"));
					submittedProcessLogVOAUX.setInformation(resultSet.getString("Splo_Information"));
					list.add(submittedProcessLogVOAUX);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubmittedProcessLogDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}
	
	public SubmittedProcessLogVO searchByID(SubmittedProcessLogVO submittedProcessLogVO) throws SubmittedProcessLogDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_SUBMITTED_PROCESS_LOG_BY_ID(?)}");
			pstmt.setString(1,submittedProcessLogVO.getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					submittedProcessLogVO.setId(resultSet.getString("Splo_ID"));
					submittedProcessLogVO.setProcessName(resultSet.getString("Splo_ProcessName"));
					submittedProcessLogVO.setStartTime(resultSet.getString("Splo_StartTime"));
					submittedProcessLogVO.setEndTime(resultSet.getString("Splo_EndTime"));
					submittedProcessLogVO.setStatusProcess(resultSet.getString("Splo_StatusProcess"));
					submittedProcessLogVO.setInformation(resultSet.getString("Splo_Information"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubmittedProcessLogDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return submittedProcessLogVO;
	}

}
