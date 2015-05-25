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
import au.com.billingbuddy.dao.interfaces.IDailySubscriptionDAO;
import au.com.billingbuddy.exceptions.objects.DailySubscriptionDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.DailySubscriptionVO;
import au.com.billingbuddy.vo.objects.MerchantCustomerCardVO;

public class DailySubscriptionDAO extends MySQLConnection implements IDailySubscriptionDAO {

	public DailySubscriptionDAO() throws MySQLConnectionException {
		super();
	}
	
	public DailySubscriptionDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}


	public int update(DailySubscriptionVO dailySubscriptionVO) throws DailySubscriptionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_DAILY_SUBSCRIPTION(?,?,?,?)}");
			cstmt.setString(1,dailySubscriptionVO.getStatus());
			cstmt.setString(2,dailySubscriptionVO.getAuthorizerCode());
			cstmt.setString(3,dailySubscriptionVO.getAuthorizerReason());
			if (dailySubscriptionVO.getErrorCode() != null && dailySubscriptionVO.getErrorCode().equalsIgnoreCase("1")) {
				cstmt.setString(4,"0");
			}else if (dailySubscriptionVO.getErrorCode() != null && dailySubscriptionVO.getErrorCode().equalsIgnoreCase("2")) {
				throw new DailySubscriptionDAOException("asdasd");
			}else {
				cstmt.setString(4,dailySubscriptionVO.getId());
			}
//			cstmt.setString(4,dailySubscriptionVO.getId());
//			cstmt.setString(4,"0");
//			cstmt.setString(4,dailySubscriptionVO.getId());
			status = cstmt.executeUpdate();
		} catch (SQLException e) {
			throw new DailySubscriptionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}
	
	public ArrayList<DailySubscriptionVO> search() throws DailySubscriptionDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<DailySubscriptionVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_DAILY_SUBSCRIPTION()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<DailySubscriptionVO>();
				while (resultSet.next()) {
					DailySubscriptionVO dailySubscriptionVO = new DailySubscriptionVO();
					dailySubscriptionVO.setId(resultSet.getString("Dasu_ID"));
					dailySubscriptionVO.setSubscriptionId(resultSet.getString("Subs_ID"));
					dailySubscriptionVO.setMerchantId(resultSet.getString("Merc_ID"));
					dailySubscriptionVO.setQuantity(resultSet.getString("Dasu_Quantity"));
					dailySubscriptionVO.setAmount(resultSet.getString("Dasu_Amount"));
					dailySubscriptionVO.setCurrency(resultSet.getString("Dasu_Currency"));
					dailySubscriptionVO.setMerchantCustomerCardVO(new MerchantCustomerCardVO());
					dailySubscriptionVO.getMerchantCustomerCardVO().setCardVO(new CardVO());
					dailySubscriptionVO.getMerchantCustomerCardVO().setCardId(resultSet.getString("Card_ID"));
					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setId(resultSet.getString("Card_ID"));
					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setNumber(resultSet.getString("Card_Number"));
					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setExpMonth(resultSet.getString("Card_ExpMonth"));
					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setExpYear(resultSet.getString("Card_ExpYear"));
					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setCvv(resultSet.getString("Card_Cvv"));
					dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().setName(resultSet.getString("Card_Name"));
					list.add(dailySubscriptionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DailySubscriptionDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}

}
