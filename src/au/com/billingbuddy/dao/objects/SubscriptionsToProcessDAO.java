package au.com.billingbuddy.dao.objects;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.connection.objects.MySQLConnection;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.interfaces.IDailySubscriptionDAO;
import au.com.billingbuddy.exceptions.objects.SubscriptionsToProcessDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.SubscriptionsToProcessVO;
import au.com.billingbuddy.vo.objects.MerchantCustomerCardVO;

public class SubscriptionsToProcessDAO extends MySQLConnection implements IDailySubscriptionDAO {

	public SubscriptionsToProcessDAO() throws MySQLConnectionException {
		super();
	}
	
	public SubscriptionsToProcessDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int update(SubscriptionsToProcessVO subscriptionsToProcessVO) throws SubscriptionsToProcessDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_SUBSCRIPTIONS_TO_PROCESS(?,?,?,?,?)}");
			cstmt.setString(1,subscriptionsToProcessVO.getStatus());
			cstmt.setString(2,subscriptionsToProcessVO.getAuthorizerCode());
			cstmt.setString(3,subscriptionsToProcessVO.getAuthorizerReason());
			cstmt.setInt(4,subscriptionsToProcessVO.getProcessAttempt());
			if (subscriptionsToProcessVO.getErrorCode() != null && subscriptionsToProcessVO.getErrorCode().equalsIgnoreCase("1")) {
				cstmt.setString(5,"0");
			}else if (subscriptionsToProcessVO.getErrorCode() != null && subscriptionsToProcessVO.getErrorCode().equalsIgnoreCase("2")) {
				throw new SubscriptionsToProcessDAOException("Error generado para pruebas ... ");
			}else {
				cstmt.setString(5,subscriptionsToProcessVO.getId());
			}
//			cstmt.setString(4,subscriptionsToProcessVO.getId());
//			cstmt.setString(4,"0");
//			cstmt.setString(4,subscriptionsToProcessVO.getId());
			status = cstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubscriptionsToProcessDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}
	
	/*public int updateUnPaid(SubscriptionsToProcessVO subscriptionsToProcessVO) throws SubscriptionsToProcessDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_UNPAID_DAILY_SUBSCRIPTION(?,?,?,?,?)}");
			cstmt.setString(1,subscriptionsToProcessVO.getStatus());
			cstmt.setString(2,subscriptionsToProcessVO.getProcessAttempt());
			cstmt.setString(3,subscriptionsToProcessVO.getAuthorizerCode());
			cstmt.setString(4,subscriptionsToProcessVO.getAuthorizerReason());
			cstmt.setString(5,subscriptionsToProcessVO.getId());
			if (subscriptionsToProcessVO.getErrorCode() != null && subscriptionsToProcessVO.getErrorCode().equalsIgnoreCase("1")) {
				cstmt.setString(4,"0");
			}else if (subscriptionsToProcessVO.getErrorCode() != null && subscriptionsToProcessVO.getErrorCode().equalsIgnoreCase("2")) {
				throw new SubscriptionsToProcessDAOException("asdasd");
			}else {
				cstmt.setString(4,subscriptionsToProcessVO.getId());
			}
//			cstmt.setString(4,subscriptionsToProcessVO.getId());
//			cstmt.setString(4,"0");
//			cstmt.setString(4,subscriptionsToProcessVO.getId());
			status = cstmt.executeUpdate();
		} catch (SQLException e) {
			throw new SubscriptionsToProcessDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}*/
	
	public ArrayList<SubscriptionsToProcessVO> search() throws SubscriptionsToProcessDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<SubscriptionsToProcessVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_SUBSCRIPTIONS_TO_PROCESS()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<SubscriptionsToProcessVO>();
				while (resultSet.next()) {
					SubscriptionsToProcessVO subscriptionsToProcessVO = new SubscriptionsToProcessVO();
					subscriptionsToProcessVO.setId(resultSet.getString("Supr_ID"));
					subscriptionsToProcessVO.setSubscriptionId(resultSet.getString("Subs_ID"));
					subscriptionsToProcessVO.setMerchantId(resultSet.getString("Merc_ID"));
					subscriptionsToProcessVO.setQuantity(resultSet.getString("Supr_Quantity"));
					subscriptionsToProcessVO.setAmount(resultSet.getString("Supr_Amount"));
					subscriptionsToProcessVO.setCurrency(resultSet.getString("Supr_Currency"));
					subscriptionsToProcessVO.setMerchantCustomerCardVO(new MerchantCustomerCardVO());
					subscriptionsToProcessVO.getMerchantCustomerCardVO().setCardVO(new CardVO());
					subscriptionsToProcessVO.getMerchantCustomerCardVO().setCardId(resultSet.getString("Card_ID"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setId(resultSet.getString("Card_ID"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setNumber(resultSet.getString("Card_Number"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setExpMonth(resultSet.getString("Card_ExpMonth"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setExpYear(resultSet.getString("Card_ExpYear"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setCvv(resultSet.getString("Card_Cvv"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setName(resultSet.getString("Card_Name"));
					list.add(subscriptionsToProcessVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubscriptionsToProcessDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}

	public HashMap<String,String> execute() throws SubscriptionsToProcessDAOException {
		CallableStatement cstmt = null;
		HashMap<String,String> res = null;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_EXECUTE_SUBSCRIPTIONS_PROCESS(?,?,?,?,?,?)}");
			cstmt.executeUpdate();
			res = new HashMap<String,String>();
			res.put("PROC_PROCESS_NEW_SUBSCRIPTION_TRIALING", cstmt.getString(1));
			res.put("PROC_PROCESS_NEW_SUBSCRIPTION_PENDING", cstmt.getString(2));
			res.put("PROC_PROCESS_DAILY_SUBSCRIPTION", cstmt.getString(3));
			res.put("PROC_PROCESS_WEEKLY_SUBSCRIPTION", cstmt.getString(4));
			res.put("PROC_PROCESS_MONTHLY_SUBSCRIPTION", cstmt.getString(5));
			res.put("PROC_PROCESS_YEARLY_SUBSCRIPTION", cstmt.getString(6));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubscriptionsToProcessDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return res;
	}
	
	/*public ArrayList<SubscriptionsToProcessVO> searchUnPaid() throws SubscriptionsToProcessDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<SubscriptionsToProcessVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_DAILY_UNPAID_SUBSCRIPTION()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<SubscriptionsToProcessVO>();
				while (resultSet.next()) {
					SubscriptionsToProcessVO subscriptionsToProcessVO = new SubscriptionsToProcessVO();
					subscriptionsToProcessVO.setId(resultSet.getString("Supr_ID"));
					subscriptionsToProcessVO.setSubscriptionId(resultSet.getString("Subs_ID"));
					subscriptionsToProcessVO.setMerchantId(resultSet.getString("Merc_ID"));
					subscriptionsToProcessVO.setQuantity(resultSet.getString("Supr_Quantity"));
					subscriptionsToProcessVO.setAmount(resultSet.getString("Supr_Amount"));
					subscriptionsToProcessVO.setCurrency(resultSet.getString("Supr_Currency"));
					subscriptionsToProcessVO.setProcessAttempt(resultSet.getString("Supr_ProcessAttempt"));					
					subscriptionsToProcessVO.setMerchantCustomerCardVO(new MerchantCustomerCardVO());
					subscriptionsToProcessVO.getMerchantCustomerCardVO().setCardVO(new CardVO());
					subscriptionsToProcessVO.getMerchantCustomerCardVO().setCardId(resultSet.getString("Card_ID"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setId(resultSet.getString("Card_ID"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setNumber(resultSet.getString("Card_Number"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setExpMonth(resultSet.getString("Card_ExpMonth"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setExpYear(resultSet.getString("Card_ExpYear"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setCvv(resultSet.getString("Card_Cvv"));
					subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().setName(resultSet.getString("Card_Name"));
					list.add(subscriptionsToProcessVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubscriptionsToProcessDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}
*/
	/*@Override
	public int searchUnPaidBloked() throws SubscriptionsToProcessDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		int count = 0;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_DAILY_UNPAID_SUBSCRIPTION_BLOCKED()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					count = resultSet.getInt("Count");
				}
			}
			System.out.println("resultSet.getFetchSize(): " + count);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SubscriptionsToProcessDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return count;
	}*/

}
