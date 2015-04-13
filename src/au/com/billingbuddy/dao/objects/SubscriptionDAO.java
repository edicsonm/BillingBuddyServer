package au.com.billingbuddy.dao.objects;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.connection.objects.MySQLConnection;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.interfaces.ISubscriptionDAO;
import au.com.billingbuddy.exceptions.objects.SubscriptionDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.PlanVO;
import au.com.billingbuddy.vo.objects.SubscriptionVO;

public class SubscriptionDAO extends MySQLConnection implements ISubscriptionDAO {

	public SubscriptionDAO() throws MySQLConnectionException{
		super();
	}
	
	public SubscriptionDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(SubscriptionVO subscriptionVO) throws SubscriptionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_SUBSCRIPTION(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			cstmt.setString(1,subscriptionVO.getPlanId());
			cstmt.setString(2,subscriptionVO.getCustomerId());
			cstmt.setString(3,subscriptionVO.getDiscountId());
			cstmt.setString(4,subscriptionVO.getQuantity());
			cstmt.setDate(5,Utilities.stringToSqlDate(subscriptionVO.getTrialStart()));
			cstmt.setDate(6,Utilities.stringToSqlDate(subscriptionVO.getTrialEnd()));
			cstmt.setDate(7,Utilities.stringToSqlDate(subscriptionVO.getStart()));
			cstmt.setString(8,subscriptionVO.getTaxPercent());
			cstmt.setString(9,"0");
			
//			cstmt.setString(7,subscriptionVO.getStatus());
//			cstmt.setString(8,subscriptionVO.getApplicationFeePercent());
//			cstmt.setDate(9,Utilities.stringToSqlDate(subscriptionVO.getCanceledAt()));
//			cstmt.setDate(10,Utilities.stringToSqlDate(subscriptionVO.getCurrentPeriodStart()));
//			cstmt.setDate(12,Utilities.stringToSqlDate(subscriptionVO.getEndedAt()));
//			cstmt.setDate(15,Utilities.stringToSqlDate(subscriptionVO.getCurrentPeriodEnd()));
			
			status = cstmt.executeUpdate();
			subscriptionVO.setId(cstmt.getString(9));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubscriptionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(SubscriptionVO subscriptionVO) throws SubscriptionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_SUBSCRIPTION(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,subscriptionVO.getPlanId());
			cstmt.setString(2,subscriptionVO.getCustomerId());
			cstmt.setString(3,subscriptionVO.getDiscountId());
			cstmt.setString(4,subscriptionVO.getCancelAtPeriodEnd());
			cstmt.setString(5,subscriptionVO.getQuantity());
			cstmt.setDate(6,Utilities.stringToSqlDate(subscriptionVO.getStart()));
			cstmt.setString(7,subscriptionVO.getStatus());
			cstmt.setString(8,subscriptionVO.getApplicationFeePercent());
			cstmt.setDate(9,Utilities.stringToSqlDate(subscriptionVO.getCanceledAt()));
			cstmt.setDate(10,Utilities.stringToSqlDate(subscriptionVO.getCurrentPeriodStart()));
			cstmt.setDate(11,Utilities.stringToSqlDate(subscriptionVO.getTrialEnd()));
			cstmt.setDate(12,Utilities.stringToSqlDate(subscriptionVO.getEndedAt()));
			cstmt.setDate(13,Utilities.stringToSqlDate(subscriptionVO.getTrialStart()));
			cstmt.setString(14,subscriptionVO.getTaxPercent());
			cstmt.setDate(15,Utilities.stringToSqlDate(subscriptionVO.getCurrentPeriodEnd()));
			cstmt.setString(16,subscriptionVO.getId());
			status = cstmt.executeUpdate();
			subscriptionVO.setId(cstmt.getString(16));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubscriptionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int delete(SubscriptionVO subscriptionVO) throws SubscriptionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_DELETE_SUBSCRIPTION(?)}");
			cstmt.setString(1,subscriptionVO.getId());
			status = cstmt.executeUpdate();
			subscriptionVO.setId(cstmt.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubscriptionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public SubscriptionVO searchByID(String ID) throws SubscriptionDAOException {
		return null;
	}

	public ArrayList<SubscriptionVO> search() throws SubscriptionDAOException {
		return null;
	}

	public ArrayList<SubscriptionVO> search(SubscriptionVO subscriptionVO) throws SubscriptionDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<SubscriptionVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_SUBSCRIPTION()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<SubscriptionVO>();
				while (resultSet.next()) {
					subscriptionVO = new SubscriptionVO();
					subscriptionVO.setId(resultSet.getString("Subs_ID"));
					subscriptionVO.setPlanId(resultSet.getString("Plan_ID"));
					subscriptionVO.setPlanVO(new PlanVO());
					subscriptionVO.getPlanVO().setName(resultSet.getString("Plan_Name"));
					subscriptionVO.setCustomerId(resultSet.getString("Cust_ID"));
					subscriptionVO.setDiscountId(resultSet.getString("Disc_ID"));
					subscriptionVO.setCancelAtPeriodEnd(resultSet.getString("Subs_CancelAtPeriodEnd"));
					subscriptionVO.setQuantity(resultSet.getString("Subs_Quantity"));
					subscriptionVO.setStart(resultSet.getString("Subs_Start"));
					subscriptionVO.setStatus(resultSet.getString("Subs_Status"));
					subscriptionVO.setApplicationFeePercent(resultSet.getString("Subs_ApplicationFeePercent"));
					subscriptionVO.setCanceledAt(resultSet.getString("Subs_CanceledAt"));
					subscriptionVO.setCurrentPeriodStart(resultSet.getString("Subs_CurrentPeriodStart"));
					subscriptionVO.setCurrentPeriodEnd(resultSet.getString("Subs_CurrentPeriodEnd"));
					subscriptionVO.setTrialEnd(resultSet.getString("Subs_TrialEnd"));
					subscriptionVO.setEndedAt(resultSet.getString("Subs_EndedAt"));
					subscriptionVO.setTrialStart(resultSet.getString("Subs_TrialStart"));
					subscriptionVO.setTaxPercent(resultSet.getString("Subs_TaxPercent"));
					subscriptionVO.setCreationTime(resultSet.getString("Subs_CreationTime"));
					list.add(subscriptionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubscriptionDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}


}
