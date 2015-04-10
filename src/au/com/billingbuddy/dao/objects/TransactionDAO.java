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
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.MerchantVO;
import au.com.billingbuddy.vo.objects.RejectedChargeVO;
import au.com.billingbuddy.vo.objects.TransactionVO;
import au.com.billingbuddy.vo.objects.VO;

public class TransactionDAO extends MySQLConnection implements ITransactionDAO {
	
	ConfigurationSystem configurationSystem = ConfigurationSystem.getInstance();
	
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
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_TRANSACTION( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,transactionVO.getIp());
			cstmt.setString(2,transactionVO.getMaxmindId());
			cstmt.setString(3,transactionVO.getBillingAddressCity());
			cstmt.setString(4,transactionVO.getBillingAddressRegion());
			cstmt.setString(5,transactionVO.getBillingAddressPostal());
			cstmt.setString(6,transactionVO.getBillingAddressCountry());
			cstmt.setString(7,transactionVO.getShippingAddress());
			cstmt.setString(8,transactionVO.getShippingAddressCity());
			cstmt.setString(9,transactionVO.getShippingAddressRegion());
			cstmt.setString(10,transactionVO.getShippingAddressPostal());
			cstmt.setString(11,transactionVO.getShippingAddressCountry());
			cstmt.setString(12,transactionVO.getDomain());
			cstmt.setString(13,transactionVO.getCustomerPhone());
			cstmt.setString(14,transactionVO.getEmailMD5());
			cstmt.setString(15,transactionVO.getUsernameMD5());
			cstmt.setString(16,transactionVO.getBin());
			cstmt.setString(17,transactionVO.getBinName());
			cstmt.setString(18,transactionVO.getBinPhone());
			cstmt.setString(19,transactionVO.getUserAgent());
			cstmt.setString(20,transactionVO.getAcceptLanguage());
			cstmt.setString(21,transactionVO.getOrderAmount());
			cstmt.setString(22,transactionVO.getOrderCurrency());
			cstmt.setString(23,transactionVO.getMerchantId());
			cstmt.setString(24,transactionVO.getTxnType());
			cstmt.setString(25,transactionVO.getcVVResult());
			cstmt.setString(26,transactionVO.getRequestedType());
			cstmt.setString(27,transactionVO.getRiskScore());
			cstmt.setString(28,transactionVO.getCountryMatch());
			cstmt.setString(29,transactionVO.getHighRiskCountry());
			cstmt.setString(30,transactionVO.getDistance());
			cstmt.setString(31,transactionVO.getIpAccuracyRadius());
			cstmt.setString(32,transactionVO.getIpCity());
			cstmt.setString(33,transactionVO.getIpRegion());
			cstmt.setString(34,transactionVO.getIpRegionName());
			cstmt.setString(35,transactionVO.getIpPostalCode());
			cstmt.setString(36,transactionVO.getIpMetroCode());
			cstmt.setString(37,transactionVO.getIpAreaCode());
			cstmt.setString(38,transactionVO.getCountryCode());
			cstmt.setString(39,transactionVO.getIpCountryName());
			cstmt.setString(40,transactionVO.getIpContinentCode());
			cstmt.setString(41,transactionVO.getIpLatitude());
			cstmt.setString(42,transactionVO.getIpLongitude());
			cstmt.setString(43,transactionVO.getIpTimeZone());
			cstmt.setString(44,transactionVO.getIpAsnum());
			cstmt.setString(45,transactionVO.getIpUserType());
			cstmt.setString(46,transactionVO.getIpNetSpeedCell());
			cstmt.setString(47,transactionVO.getIpDomain());
			cstmt.setString(48,transactionVO.getIpIsp());
			cstmt.setString(49,transactionVO.getIpOrg());
			cstmt.setString(50,transactionVO.getIpCityConf());
			cstmt.setString(51,transactionVO.getIpRegionConf());
			cstmt.setString(52,transactionVO.getIpPostalConf());
			cstmt.setString(53,transactionVO.getIpCountryConf());
			cstmt.setString(54,transactionVO.getAnonymousProxy());
			cstmt.setString(55,transactionVO.getProxyScore());
			cstmt.setString(56,transactionVO.getIpCorporateProxy());
			cstmt.setString(57,transactionVO.getFreeMail());
			cstmt.setString(58,transactionVO.getCarderEmail());
			cstmt.setString(59,transactionVO.getBinMatch());
			cstmt.setString(60,transactionVO.getBinCountry());
			cstmt.setString(61,transactionVO.getBinNameMatch());
			cstmt.setString(62,transactionVO.getBinPhoneMatch());
			cstmt.setString(63,transactionVO.getPrepaid());
			cstmt.setString(64,transactionVO.getCustPhoneInBillingLoc());
			cstmt.setString(65,transactionVO.getShipForward());
			cstmt.setString(66,transactionVO.getCityPostalMatch());
			cstmt.setString(67,transactionVO.getShipCityPostalMatch());
			cstmt.setString(68,transactionVO.getMinfraudVersion());
			cstmt.setString(69,transactionVO.getServiceLevel());
			cstmt.setString(70,transactionVO.getError());
			cstmt.setString(71,transactionVO.getProcessTime());
			cstmt.setString(72,"0");
			status = cstmt.executeUpdate();
			transactionVO.setId(cstmt.getString(72));
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
//					transactionVO.setCustomerId(rs.getString("Tran_CustomerId"));
					transactionVO.setCardId(rs.getString("Tran_CardId"));
					transactionVO.setOrderAmount(rs.getString("Tran_OrderAmount"));
					transactionVO.setCardVO(new CardVO());
					transactionVO.getCardVO().setNumber(rs.getString("Card_Number"));
					transactionVO.getCardVO().setName(rs.getString("Card_Name"));
					listTransactions.add(transactionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TransactionDAOException(e);
		} finally {
			PsRs(pstmt, rs, getConnection());
		}
		return listTransactions;
	}

	public ArrayList<TransactionVO> searchAmountsByDay(TransactionVO transactionVO) throws TransactionDAOException {
		ArrayList<TransactionVO> listTransactions = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			transactionVO.setInitialDateReport(Utilities.validateDateReport(transactionVO.getInitialDateReport(), Integer.parseInt(configurationSystem.getKey("days.PROC_SEARCH_AMOUNT_BY_DAY"))));
			transactionVO.setFinalDateReport(Utilities.validateDateReport(transactionVO.getFinalDateReport(), 0));
			
			pstmt = getConnection().prepareStatement("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_AMOUNT_BY_DAY( ?, ? , ?, ?)}");
			pstmt.setString(1, transactionVO.getInitialDateReport());
			pstmt.setString(2, transactionVO.getFinalDateReport());
			pstmt.setString(3, transactionVO.getMerchantId());
			pstmt.setString(4, transactionVO.getUserId());
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				listTransactions = new ArrayList<TransactionVO>();
				while (rs.next()) {
					transactionVO = new TransactionVO();
					transactionVO.setDateReport(rs.getString("DAY"));
					transactionVO.setAmountDateReport(rs.getString("TOTAL"));
					listTransactions.add(transactionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TransactionDAOException(e);
		} finally {
			PsRs(pstmt, rs, getConnection());
		}
		return listTransactions;
	}

	public ArrayList<TransactionVO> searchChargesByDay(TransactionVO transactionVO) throws TransactionDAOException {
		ArrayList<TransactionVO> listTransactions = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			transactionVO.setInitialDateReport(Utilities.validateDateReport(transactionVO.getInitialDateReport(), Integer.parseInt(configurationSystem.getKey("days.PROC_SEARCH_AMOUNT_BY_DAY"))));
			transactionVO.setFinalDateReport(Utilities.validateDateReport(transactionVO.getFinalDateReport(), 0));
			
			pstmt = getConnection().prepareStatement("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CHARGES_BY_DAY( ? ,? ,? ,? )}");
			pstmt.setString(1, transactionVO.getInitialDateReport());
			pstmt.setString(2, transactionVO.getFinalDateReport());
			pstmt.setString(3, transactionVO.getMerchantId());
			pstmt.setString(4, transactionVO.getUserId());
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				listTransactions = new ArrayList<TransactionVO>();
				while (rs.next()) {
					transactionVO = new TransactionVO();
					transactionVO.setDateReport(rs.getString("DAY"));
					transactionVO.setTotalDateReport(rs.getString("TOTAL"));
					listTransactions.add(transactionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TransactionDAOException(e);
		} finally {
			PsRs(pstmt, rs, getConnection());
		}
		return listTransactions;
	}
	
	public ArrayList<TransactionVO> searchRejectedByDay(TransactionVO transactionVO) throws TransactionDAOException {
		ArrayList<TransactionVO> listTransactions = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			transactionVO.setInitialDateReport(Utilities.validateDateReport(transactionVO.getInitialDateReport(), Integer.parseInt(configurationSystem.getKey("days.PROC_SEARCH_AMOUNT_BY_DAY"))));
			transactionVO.setFinalDateReport(Utilities.validateDateReport(transactionVO.getFinalDateReport(), 0));
			
			pstmt = getConnection().prepareStatement("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_REJECTEDS_BY_DAY( ?, ? , ? , ?)}");
			
			pstmt.setString(1, transactionVO.getInitialDateReport());
			pstmt.setString(2, transactionVO.getFinalDateReport());
			pstmt.setString(3, transactionVO.getMerchantId());
			pstmt.setString(4, transactionVO.getUserId());
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				listTransactions = new ArrayList<TransactionVO>();
				while (rs.next()) {
					transactionVO = new TransactionVO();
					transactionVO.setDateReport(rs.getString("DAY"));
					transactionVO.setTotalDateReport(rs.getString("TOTAL"));
					listTransactions.add(transactionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TransactionDAOException(e);
		} finally {
			PsRs(pstmt, rs, getConnection());
		}
		return listTransactions;
	}
	
	public ArrayList<TransactionVO> searchTransactionsByDay(TransactionVO transactionVO) throws TransactionDAOException {
		ArrayList<TransactionVO> listTransactions = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			transactionVO.setInitialDateReport(Utilities.validateDateReport(transactionVO.getInitialDateReport(), Integer.parseInt(configurationSystem.getKey("days.PROC_SEARCH_AMOUNT_BY_DAY"))));
			transactionVO.setFinalDateReport(Utilities.validateDateReport(transactionVO.getFinalDateReport(), 0));
			
			pstmt = getConnection().prepareStatement("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_TRANSACTIONS_BY_DAY( ?, ?, ? )}");
			pstmt.setString(1, transactionVO.getInitialDateReport());
			pstmt.setString(2, transactionVO.getFinalDateReport());
			pstmt.setString(3, transactionVO.getUserId());
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				listTransactions = new ArrayList<TransactionVO>();
				while (rs.next()) {
					
					transactionVO = new TransactionVO();
					transactionVO.setId(rs.getString("Tran_ID"));
					transactionVO.setCreationTime(rs.getString("Tran_CreateTime"));
					transactionVO.setIpCity(rs.getString("Tran_IPCity"));
					transactionVO.setIpRegionName(rs.getString("Tran_IPRegionName"));
					transactionVO.setCountryCode(rs.getString("Tran_CountryCode"));
					transactionVO.setIpCountryName(rs.getString("Tran_IPCountryName"));
					
					
					transactionVO.setChargeVO(new ChargeVO());
					transactionVO.getChargeVO().setId(rs.getString("Char_ID"));
					transactionVO.getChargeVO().setAmount(rs.getString("Char_Amount"));
					transactionVO.getChargeVO().setCurrency(rs.getString("Char_Currency"));
					transactionVO.getChargeVO().setCreationTime(rs.getString("Char_CreateTime"));
					
					transactionVO.setCardVO(new CardVO());
					transactionVO.getCardVO().setId(rs.getString("Card_ID"));
					transactionVO.getCardVO().setLast4(rs.getString("Card_Last4"));
					transactionVO.getCardVO().setNumber(rs.getString("Card_Number"));
					
					transactionVO.getCardVO().setCustomerId(rs.getString("Cust_ID"));
					transactionVO.getCardVO().setName(rs.getString("Card_Name"));
					
					transactionVO.getCardVO().setBrand(rs.getString("Card_Brand"));
					transactionVO.getCardVO().setFunding(rs.getString("Card_Funding"));
					transactionVO.getCardVO().setExpMonth(rs.getString("Card_ExpMonth"));
					transactionVO.getCardVO().setExpYear(rs.getString("Card_ExpYear"));
					transactionVO.getCardVO().setCountry(rs.getString("Card_Country"));
					
					transactionVO.setMerchantVO(new MerchantVO());
					transactionVO.getMerchantVO().setId(rs.getString("Merc_ID"));
					transactionVO.getMerchantVO().setName(rs.getString("Merc_Name"));
					
					listTransactions.add(transactionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TransactionDAOException(e);
		} finally {
			PsRs(pstmt, rs, getConnection());
		}
		return listTransactions;
	}
	public ArrayList<TransactionVO> searchTransactionsByDayFilter(TransactionVO transactionVO) throws TransactionDAOException {
		ArrayList<TransactionVO> listTransactions = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			transactionVO.setInitialDateReport(Utilities.validateDateReport(transactionVO.getInitialDateReport(), Integer.parseInt(configurationSystem.getKey("days.PROC_SEARCH_AMOUNT_BY_DAY"))));
			transactionVO.setFinalDateReport(Utilities.validateDateReport(transactionVO.getFinalDateReport(), 0));
			
			pstmt = getConnection().prepareStatement("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_TRANSACTIONS_BY_DAY_FILTER( ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			
//			System.out.println("transactionVO.getCardVO().getNumber(): " + transactionVO.getCardVO().getNumber());
//			System.out.println("transactionVO.getMerchantId(): " + transactionVO.getMerchantId());
//			System.out.println("transactionVO.getCardVO().getBrand(): " + transactionVO.getCardVO().getBrand());
//			
//			System.out.println("transactionVO.getCardVO().getCountry(): " + transactionVO.getCardVO().getCountry());
//			System.out.println("transactionVO.getChargeVO().getCurrency(): " + transactionVO.getChargeVO().getCurrency());
//			System.out.println("transactionVO.getMatch(): " + transactionVO.getMatch());
//			
//			System.out.println("transactionVO.getInitialDateReport(): " + transactionVO.getInitialDateReport());
//			System.out.println("transactionVO.getFinalDateReport(): " + transactionVO.getFinalDateReport());
//			System.out.println("transactionVO.getUserId(): " + transactionVO.getUserId());
			
			pstmt.setString(1, transactionVO.getCardVO().getNumber());
			pstmt.setString(2, transactionVO.getMerchantId());
			pstmt.setString(3, transactionVO.getCardVO().getBrand());
			
			pstmt.setString(4, transactionVO.getCardVO().getCountry());
			pstmt.setString(5, transactionVO.getChargeVO().getCurrency());
			pstmt.setString(6, transactionVO.getMatch());
			
			pstmt.setString(7, transactionVO.getInitialDateReport());
			pstmt.setString(8, transactionVO.getFinalDateReport());
			pstmt.setString(9, transactionVO.getUserId());
			
			rs = pstmt.executeQuery();
			if (rs != null) {
				listTransactions = new ArrayList<TransactionVO>();
				while (rs.next()) {
					
					transactionVO = new TransactionVO();
					transactionVO.setId(rs.getString("Tran_ID"));
					transactionVO.setCreationTime(rs.getString("Tran_CreateTime"));
					transactionVO.setIpCity(rs.getString("Tran_IPCity"));
					transactionVO.setIpRegionName(rs.getString("Tran_IPRegionName"));
					transactionVO.setCountryCode(rs.getString("Tran_CountryCode"));
					transactionVO.setIpCountryName(rs.getString("Tran_IPCountryName"));
					
					transactionVO.setChargeVO(new ChargeVO());
					transactionVO.getChargeVO().setId(rs.getString("Char_ID"));
					transactionVO.getChargeVO().setAmount(rs.getString("Char_Amount"));
					transactionVO.getChargeVO().setCurrency(rs.getString("Char_Currency"));
					transactionVO.getChargeVO().setCreationTime(rs.getString("Char_CreateTime"));
					
					transactionVO.setCardVO(new CardVO());
					transactionVO.getCardVO().setId(rs.getString("Card_ID"));
					transactionVO.getCardVO().setLast4(rs.getString("Card_Last4"));
					transactionVO.getCardVO().setNumber(rs.getString("Card_Number"));
					
					transactionVO.getCardVO().setCustomerId(rs.getString("Cust_ID"));
					transactionVO.getCardVO().setName(rs.getString("Card_Name"));
					
					transactionVO.getCardVO().setBrand(rs.getString("Card_Brand"));
					transactionVO.getCardVO().setFunding(rs.getString("Card_Funding"));
					transactionVO.getCardVO().setExpMonth(rs.getString("Card_ExpMonth"));
					transactionVO.getCardVO().setExpYear(rs.getString("Card_ExpYear"));
					transactionVO.getCardVO().setCountry(rs.getString("Card_Country"));
					
					transactionVO.setMerchantVO(new MerchantVO());
					transactionVO.getMerchantVO().setId(rs.getString("Merc_ID"));
					transactionVO.getMerchantVO().setName(rs.getString("Merc_Name"));
					
					listTransactions.add(transactionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TransactionDAOException(e);
		} finally {
			PsRs(pstmt, rs, getConnection());
		}
		return listTransactions;
	}

}

