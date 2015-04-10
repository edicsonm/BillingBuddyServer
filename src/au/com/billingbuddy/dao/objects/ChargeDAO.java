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
import au.com.billingbuddy.dao.interfaces.IChargeDAO;
import au.com.billingbuddy.exceptions.objects.BinDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.ChargeDAOException;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.CustomerVO;
import au.com.billingbuddy.vo.objects.RefundVO;
import au.com.billingbuddy.vo.objects.StripeChargeVO;
import au.com.billingbuddy.vo.objects.VO;

public class ChargeDAO extends MySQLConnection implements IChargeDAO {

	public ChargeDAO() throws MySQLConnectionException{
		super();
	}
	
	public ChargeDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}
	
	public int insert(ChargeVO chargeVO) throws ChargeDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_CHARGE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			
			cstmt.setString(1,chargeVO.getTransactionId());
			cstmt.setString(2,chargeVO.getCardId());
			cstmt.setString(3,chargeVO.getStripeId());
			cstmt.setString(4,chargeVO.getInvoiceId());
			cstmt.setString(5,chargeVO.getAddressId());
			cstmt.setString(6,chargeVO.getAmount());
			cstmt.setString(7,chargeVO.getCaptured());
			cstmt.setString(8,chargeVO.getCreationTime());
			cstmt.setString(9,chargeVO.getCurrency());
			cstmt.setString(10,chargeVO.getPaid());
			cstmt.setString(11,chargeVO.getRefunded());
			cstmt.setString(12,chargeVO.getFailureCode());
			cstmt.setString(13,chargeVO.getFailureMessage());
			cstmt.setString(14,chargeVO.getReceiptMail());
			cstmt.setString(15,chargeVO.getReceiptNumber());
			cstmt.setString(16,chargeVO.getProcessTime());
			cstmt.setString(17,"0");
			status = cstmt.executeUpdate();
			chargeVO.setId(cstmt.getString(17));
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChargeDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public ChargeVO searchBin(ChargeVO chargeVO) throws ChargeDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_BIN( ? )}");
//			pstmt.setString(1, stripeChargeVO.getBin());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					chargeVO.setId(resultSet.getString("Bin_Id"));
//					stripeChargeVO.setBin(resultSet.getString("Bin_Bin"));
//					stripeChargeVO.setBrand(resultSet.getString("Bin_Brand"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChargeDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return chargeVO;
	}

	public int updateStatusRefund(ChargeVO chargeVO) throws ChargeDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_STATUS_REFUND(?)}");
			cstmt.setString(1,chargeVO.getId());
			status = cstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChargeDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int delete() throws ChargeDAOException {
		return 0;
	}

	public VO searchByID(String ID) throws ChargeDAOException {
		return null;
	}

	public ArrayList<ChargeVO> search() throws ChargeDAOException {
		return null;
	}

	public ArrayList<ChargeVO> search(ChargeVO chargeVO) throws ChargeDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<ChargeVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CHARGE(?)}");
			pstmt.setString(1, chargeVO.getUserId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<ChargeVO>();
				while (resultSet.next()) {
					chargeVO = new ChargeVO();
					chargeVO.setId(resultSet.getString("Char_ID"));
					chargeVO.setCardId(resultSet.getString("Card_ID"));
					chargeVO.setTransactionId(resultSet.getString("Tran_ID"));
					chargeVO.setStripeId(resultSet.getString("Char_IDStripe"));
					
					chargeVO.setCardVO(new CardVO());
					chargeVO.getCardVO().setNumber(resultSet.getString("Card_Number"));
					chargeVO.getCardVO().setBrand(resultSet.getString("Card_Brand"));
					chargeVO.getCardVO().setFunding(resultSet.getString("Card_Funding"));
					chargeVO.getCardVO().setLast4(resultSet.getString("Card_Last4"));
					
					chargeVO.setInvoice(resultSet.getString("Invo_ID"));
					chargeVO.setAddressId(resultSet.getString("Addr_ID"));
					chargeVO.setAmount(resultSet.getString("Char_Amount"));
					chargeVO.setCaptured(resultSet.getString("Char_Captured"));
					chargeVO.setCreationTime(resultSet.getString("Char_CreateTime"));
					chargeVO.setCurrency(resultSet.getString("Char_Currency"));
					chargeVO.setPaid(resultSet.getString("Char_Paid"));
					chargeVO.setRefunded(resultSet.getString("Char_Refunded"));
					chargeVO.setFailureCode(resultSet.getString("Char_FailureCode"));
					chargeVO.setFailureMessage(resultSet.getString("Char_FailureMessage"));
					chargeVO.setReceiptMail(resultSet.getString("Char_ReceiptEmail"));
					chargeVO.setReceiptNumber(resultSet.getString("Char_ReceiptNumber"));
					chargeVO.setProcessTime(resultSet.getString("Char_ProcessTime"));
					chargeVO.setAmountRefunded(resultSet.getString("REFUND"));
					list.add(chargeVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChargeDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}

	public ChargeVO searchDetail(ChargeVO chargeVO) throws ChargeDAOException {
		
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CHARGE_DETAIL(?)}");
			pstmt.setString(1, chargeVO.getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					chargeVO = new ChargeVO();
					chargeVO.setId(resultSet.getString("Char_ID"));
					chargeVO.setCardId(resultSet.getString("Card_ID"));
					chargeVO.setTransactionId(resultSet.getString("Tran_ID"));
					chargeVO.setStripeId(resultSet.getString("Char_IDStripe"));
					
					chargeVO.setCardVO(new CardVO());
					chargeVO.getCardVO().setName(resultSet.getString("Card_Name"));
					chargeVO.getCardVO().setBrand(resultSet.getString("Card_Brand"));
					chargeVO.getCardVO().setFunding(resultSet.getString("Card_Funding"));
					chargeVO.getCardVO().setNumber(resultSet.getString("Card_Number"));
					chargeVO.getCardVO().setLast4(resultSet.getString("Card_Last4"));
					chargeVO.getCardVO().setCustomerVO(new CustomerVO());
					chargeVO.getCardVO().getCustomerVO().setEmail(resultSet.getString("Cust_Email"));
					
					chargeVO.setInvoice(resultSet.getString("Invo_ID"));
					chargeVO.setAddressId(resultSet.getString("Addr_ID"));
					chargeVO.setAmount(resultSet.getString("Char_Amount"));
					chargeVO.setCaptured(resultSet.getString("Char_Captured"));
					chargeVO.setCreationTime(resultSet.getString("Char_CreateTime"));
					chargeVO.setCurrency(resultSet.getString("Char_Currency"));
					chargeVO.setPaid(resultSet.getString("Char_Paid"));
					chargeVO.setRefunded(resultSet.getString("Char_Refunded"));
					chargeVO.setFailureCode(resultSet.getString("Char_FailureCode"));
					chargeVO.setFailureMessage(resultSet.getString("Char_FailureMessage"));
					chargeVO.setReceiptMail(resultSet.getString("Char_ReceiptEmail"));
					chargeVO.setReceiptNumber(resultSet.getString("Char_ReceiptNumber"));
					chargeVO.setProcessTime(resultSet.getString("Char_ProcessTime"));
					chargeVO.setAmountRefunded(resultSet.getString("REFUND"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChargeDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return chargeVO;
	}

}
