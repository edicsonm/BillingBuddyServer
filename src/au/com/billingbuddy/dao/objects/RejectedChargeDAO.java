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
import au.com.billingbuddy.dao.interfaces.IRejectedChargeDAO;
import au.com.billingbuddy.exceptions.objects.BinDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.RejectedChargeDAOException;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.RefundVO;
import au.com.billingbuddy.vo.objects.RejectedChargeVO;
import au.com.billingbuddy.vo.objects.StripeChargeVO;
import au.com.billingbuddy.vo.objects.VO;

public class RejectedChargeDAO extends MySQLConnection implements IRejectedChargeDAO {

	public RejectedChargeDAO() throws MySQLConnectionException{
		super();
	}
	
	public RejectedChargeDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}
	
	public int insert(RejectedChargeVO RejectedChargeVO) throws RejectedChargeDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_REJECTED_CHARGE(?,?,?,?,?,?,?,?,?,?)}");
			
			cstmt.setString(1,RejectedChargeVO.getTransactionId());
			cstmt.setString(2,RejectedChargeVO.getAmount());
			cstmt.setString(3,RejectedChargeVO.getCurrency());
			cstmt.setString(4,RejectedChargeVO.getFailureCode());
			cstmt.setString(5,RejectedChargeVO.getFailureMessage());
			cstmt.setString(6,RejectedChargeVO.getCardNumber());
			cstmt.setString(7,RejectedChargeVO.getExpYear());
			cstmt.setString(8,RejectedChargeVO.getExpMonth());
			cstmt.setString(9,RejectedChargeVO.getCardHolderName());
			cstmt.setString(10,"0");
			status = cstmt.executeUpdate();
			RejectedChargeVO.setId(cstmt.getString(10));
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RejectedChargeDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public ArrayList<RejectedChargeVO> search(RejectedChargeVO RejectedChargeVO) throws RejectedChargeDAOException {
		return null;
	}

	public ArrayList<RejectedChargeVO> search() throws RejectedChargeDAOException {
		return null;
	}

	public VO searchByID(String ID) throws RejectedChargeDAOException {
		return null;
	}

}
