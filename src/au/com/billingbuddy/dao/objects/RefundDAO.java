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
import au.com.billingbuddy.dao.interfaces.IRefundDAO;
import au.com.billingbuddy.exceptions.objects.ChargeDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.RefundDAOException;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.RefundVO;
import au.com.billingbuddy.vo.objects.VO;

public class RefundDAO extends MySQLConnection implements IRefundDAO {

	public RefundDAO() throws MySQLConnectionException{
		super();
	}
	
	public RefundDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(RefundVO refundVO) throws RefundDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_REFUND(?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,refundVO.getChargeId());
			cstmt.setString(2,refundVO.getStripeId());
			cstmt.setString(3,refundVO.getAmount());
			cstmt.setString(4,refundVO.getCurrency());
			cstmt.setString(5,refundVO.getBalanceTransaction());
			cstmt.setString(6,refundVO.getReason());
			cstmt.setString(7,refundVO.getReceiptNumber());
			cstmt.setString(8,refundVO.getTransactionId());
			cstmt.setString(9,"0");
			status = cstmt.executeUpdate();
			refundVO.setId(cstmt.getString(9));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RefundDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update() throws RefundDAOException {
		return 0;
	}

	public int delete() throws RefundDAOException {
		return 0;
	}

	public VO searchByID(String ID) throws RefundDAOException {
		return null;
	}

	public ArrayList<RefundVO> search() throws RefundDAOException {
		return null;
	}

	public ArrayList<RefundVO> search(RefundVO refundVO) throws RefundDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<RefundVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_REFUND(?)}");
			pstmt.setString(1, refundVO.getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<RefundVO>();
				while (resultSet.next()) {
					refundVO = new RefundVO();
					refundVO.setId(resultSet.getString("Refu_ID"));
					refundVO.setChargeId(resultSet.getString("Char_ID"));
					refundVO.setTransactionId(resultSet.getString("Tran_ID"));
					refundVO.setStripeId(resultSet.getString("Refu_IDStripe"));
					refundVO.setAmount(resultSet.getString("Refu_Amount"));
					refundVO.setCreationTime(resultSet.getString("Refu_CreateTime"));
					refundVO.setCurrency(resultSet.getString("Refu_Currency"));
					refundVO.setBalanceTransaction(resultSet.getString("Refu_BalanceTransaction"));
					refundVO.setReason(resultSet.getString("Refu_Reason"));
					refundVO.setReceiptNumber(resultSet.getString("Refu_ReceiptNumber"));
					list.add(refundVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RefundDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}

	

}
