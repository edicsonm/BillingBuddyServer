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
import au.com.billingbuddy.dao.interfaces.ITransactionDAO;
import au.com.billingbuddy.exceptions.objects.DataSanitizeException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.vo.objects.BinVO;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.TransactionVO;
import au.com.billingbuddy.vo.objects.VO;

public class TransactionDAO extends MySQLConnection implements ITransactionDAO {

	public TransactionDAO() throws MySQLConnectionException{
		super();
	}
	
	public TransactionDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}
	
	public int insert(TransactionVO transactionVO) throws TransactionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_TRANSACTION( ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			cstmt.setString(1, transactionVO.getOrderCurrency());
			cstmt.setString(2, transactionVO.getMerchantId());
			cstmt.setString(3, transactionVO.getTxnType());
			cstmt.setString(4, transactionVO.getUserAgent());
			cstmt.setString(5, transactionVO.getAcceptLanguage());
			cstmt.setString(6, transactionVO.getCustomerId());
			cstmt.setString(7, transactionVO.getCardId());
			cstmt.setString(8, transactionVO.getBillingAddressCity());
			cstmt.setString(9, transactionVO.getBillingAddressRegion());
			cstmt.setString(10, transactionVO.getBillingAddressPostal());
			cstmt.setString(11, transactionVO.getBillingAddressCountry());
			cstmt.setString(12, transactionVO.getShippingAddressId());
			cstmt.setString(13, transactionVO.getProduct());
			cstmt.setString(14, transactionVO.getQuantity());
			cstmt.setString(15, transactionVO.getRate());
			cstmt.setString(16, transactionVO.getOrderAmount());
			cstmt.setString(17, transactionVO.getCompanyName());
			cstmt.setString(18, transactionVO.getIp());
			cstmt.setString(19, transactionVO.getSessionId());
			cstmt.setString(20, transactionVO.getDomain());
			cstmt.setString(21, "0");
			status = cstmt.executeUpdate();
			transactionVO.setId(cstmt.getString(21));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TransactionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(TransactionVO transactionVO) throws TransactionDAOException {
		return 0;
	}

	public int delete() throws TransactionDAOException {
		return 0;
	}

	public ArrayList<TransactionVO> search() throws TransactionDAOException {
		return null;
	}

	public VO searchByID(String ID) throws TransactionDAOException {
		return null;
	}

	public ArrayList<TransactionVO> search(TransactionVO transactionVO) throws TransactionDAOException {
		ArrayList<TransactionVO> listTransactions = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = getConnection().prepareStatement("{call "+ConfigurationSystem.getKey("schema")+".PROC_LIST_TRANSACTION( ? )}");
			pstmt.setString(1, Utilities.isNullOrEmpty(transactionVO.getId()) ? "0" : transactionVO.getId());
			rs = pstmt.executeQuery();
			if (rs != null) {
				listTransactions = new ArrayList<TransactionVO>();
				while (rs.next()) {
					transactionVO = new TransactionVO();
					transactionVO.setId(rs.getString("Tran_Id"));
					transactionVO.setOrderCurrency(rs.getString("Tran_OrderCurrency"));
					transactionVO.setTxnType(rs.getString("Tran_TxnType"));
					transactionVO.setCustomerId(rs.getString("Tran_CustomerId"));
					transactionVO.setCardId(rs.getString("Tran_CardId"));
					transactionVO.setOrderAmount(rs.getString("Tran_OrderAmount"));
					transactionVO.setCardVO(new CardVO());
					transactionVO.getCardVO().setCardNumber(rs.getString("Card_Number"));
					transactionVO.getCardVO().setName(rs.getString("Card_Name"));
					listTransactions.add(transactionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TransactionDAOException(e);
		} catch (DataSanitizeException e) {
			throw new TransactionDAOException(e);
		} finally {
			PsRs(pstmt, rs, getConnection());
		}
		return listTransactions;
	}

}

