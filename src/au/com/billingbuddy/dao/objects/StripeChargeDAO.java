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
import au.com.billingbuddy.dao.interfaces.IBinDAO;
import au.com.billingbuddy.dao.interfaces.IStripeChargeDAO;
import au.com.billingbuddy.exceptions.objects.BinDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.StripeChargeDAOException;
import au.com.billingbuddy.vo.objects.StripeChargeVO;
import au.com.billingbuddy.vo.objects.VO;

public class StripeChargeDAO extends MySQLConnection implements IStripeChargeDAO {

	public StripeChargeDAO() throws MySQLConnectionException{
		super();
	}
	
	public StripeChargeDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}
	
	public int insert(StripeChargeVO stripeChargeVO) throws StripeChargeDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_STRIPECHARGE( ? , ?, ?, ?, ?, ?, ?, ?, ? , ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			cstmt.setString( 1,stripeChargeVO.getIdStripe());
			cstmt.setString( 2,stripeChargeVO.getObject());
			cstmt.setString( 3,stripeChargeVO.getLiveMode());
			cstmt.setString( 4,stripeChargeVO.getPaid());
			cstmt.setString( 5,stripeChargeVO.getAmount());
			cstmt.setString( 6,stripeChargeVO.getCurrency());
			cstmt.setString( 7,stripeChargeVO.getRefunded());
			cstmt.setString( 8,stripeChargeVO.getCaptured());
			cstmt.setString( 9,stripeChargeVO.getBalanceTransaction());
			cstmt.setString( 10,stripeChargeVO.getIdDispute());
			cstmt.setString( 11,stripeChargeVO.getFailureMessage());
			cstmt.setString( 12,stripeChargeVO.getFailureCode());
			cstmt.setString( 13,stripeChargeVO.getAmountRefunded());
			cstmt.setString( 14,stripeChargeVO.getIdCustomer());
			cstmt.setString( 15,stripeChargeVO.getInvoice());
			cstmt.setString( 16,stripeChargeVO.getDescription());
			cstmt.setString( 17,stripeChargeVO.getDispute());
			cstmt.setString( 18,stripeChargeVO.getMetadata());
			cstmt.setString( 19,stripeChargeVO.getStatementDescription());
			cstmt.setString( 20,stripeChargeVO.getReceiptEmail());
			cstmt.setString( 21,stripeChargeVO.getReceiptNumber());
			cstmt.setString( 22,stripeChargeVO.getShipping());
			cstmt.setString( 23,stripeChargeVO.getIdCard());
			cstmt.setString(24, "0");
			status = cstmt.executeUpdate();
			stripeChargeVO.setId(cstmt.getString(24));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StripeChargeDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public StripeChargeVO searchBin(StripeChargeVO stripeChargeVO) throws StripeChargeDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_BIN( ? )}");
//			pstmt.setString(1, stripeChargeVO.getBin());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					stripeChargeVO.setId(resultSet.getString("Bin_Id"));
//					stripeChargeVO.setBin(resultSet.getString("Bin_Bin"));
//					stripeChargeVO.setBrand(resultSet.getString("Bin_Brand"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StripeChargeDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return stripeChargeVO;
	}

	public int update() throws StripeChargeDAOException {
		return 0;
	}

	public int delete() throws StripeChargeDAOException {
		return 0;
	}

	public VO searchByID(String ID) throws StripeChargeDAOException {
		return null;
	}

	public ArrayList<StripeChargeVO> search() throws StripeChargeDAOException {
		return null;
	}

}
