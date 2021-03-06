package au.com.billingbuddy.dao.objects;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import au.com.billigbuddy.utils.BBUtils;
import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.connection.objects.MySQLConnection;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.interfaces.ISubscriptionDAO;
import au.com.billingbuddy.exceptions.objects.SubscriptionDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CardVO;
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
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_SUBSCRIPTION(?, ?, ?, ?, ?, ?, ?, ?)}");
			cstmt.setString(1,subscriptionVO.getPlanId());
			cstmt.setString(2,subscriptionVO.getMerchantCustomerCardId());
			cstmt.setString(3,subscriptionVO.getDiscountId());
			cstmt.setString(4,subscriptionVO.getQuantity());
			cstmt.setDate(5,BBUtils.formatStringToSqlDate(6,subscriptionVO.getTrialStart()));
			cstmt.setDate(6,BBUtils.formatStringToSqlDate(6,subscriptionVO.getTrialEnd()));
			cstmt.setString(7,subscriptionVO.getTaxPercent());
			cstmt.setString(8,"0");
			
//			cstmt.setString(7,subscriptionVO.getStatus());
//			cstmt.setString(8,subscriptionVO.getApplicationFeePercent());
//			cstmt.setDate(9,Utilities.stringToSqlDate(subscriptionVO.getCanceledAt()));
//			cstmt.setDate(10,Utilities.stringToSqlDate(subscriptionVO.getCurrentPeriodStart()));
//			cstmt.setDate(12,Utilities.stringToSqlDate(subscriptionVO.getEndedAt()));
//			cstmt.setDate(15,Utilities.stringToSqlDate(subscriptionVO.getCurrentPeriodEnd()));
			
			status = cstmt.executeUpdate();
			subscriptionVO.setId(cstmt.getString(8));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubscriptionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}


	public int cancel(SubscriptionVO subscriptionVO) throws SubscriptionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_CANCEL_SUBSCRIPTION(?)}");
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
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_SUBSCRIPTION(?,?)}");
			pstmt.setString(1,subscriptionVO.getUserId());
			pstmt.setString(2,subscriptionVO.getMerchantCustomerCardVO().getMerchantCustomerVO().getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<SubscriptionVO>();
				while (resultSet.next()) {
					subscriptionVO = new SubscriptionVO();
					subscriptionVO.setId(resultSet.getString("Subs_ID"));
					subscriptionVO.setPlanId(resultSet.getString("Plan_ID"));
					subscriptionVO.setMerchantCustomerCardId(resultSet.getString("Mcca_ID"));
					
					subscriptionVO.setPlanVO(new PlanVO());
					subscriptionVO.getPlanVO().setName(resultSet.getString("Plan_Name"));
					
					subscriptionVO.setCardVO(new CardVO());
					subscriptionVO.getCardVO().setNumber(resultSet.getString("Card_Number"));
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

	public int update(SubscriptionVO subscriptionVO)
			throws SubscriptionDAOException {
		// TODO Auto-generated method stub
		return 0;
	}


}
