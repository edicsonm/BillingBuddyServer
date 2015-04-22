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
import au.com.billingbuddy.dao.interfaces.ICardDAO;
import au.com.billingbuddy.exceptions.objects.CardDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.CertificateVO;
import au.com.billingbuddy.vo.objects.VO;

public class CardDAO extends MySQLConnection implements ICardDAO {
	
	public CardDAO() throws MySQLConnectionException{
		super();
	}
	
	public CardDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(CardVO cardVO) throws CardDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_CARD( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )}");
			cstmt.setString(1,cardVO.getCustomerId());
			cstmt.setString(2,cardVO.getCardIdStripe());
			cstmt.setString(3,cardVO.getBrand());
			cstmt.setString(4,cardVO.getExpMonth());
			cstmt.setString(5,cardVO.getExpYear());
			cstmt.setString(6,cardVO.getFingerPrint());
			cstmt.setString(7,cardVO.getFunding());
			cstmt.setString(8,cardVO.getNumber());
			cstmt.setString(9,cardVO.getName());
			cstmt.setString(10,cardVO.getLast4());
			cstmt.setString(11,cardVO.getAddressCity());
			cstmt.setString(12,cardVO.getAddressCountry());
			cstmt.setString(13,cardVO.getAddresState());
			cstmt.setString(14,cardVO.getAddressLine1());
			cstmt.setString(15,cardVO.getAddressLine2());
			cstmt.setString(16,cardVO.getAddressZip());
			cstmt.setString(17,cardVO.getCountry());
			cstmt.setString(18,cardVO.getBlackListed());
			cstmt.setString(19,cardVO.getBlackListedReason());
			cstmt.setString(20,cardVO.getBlackListedCreateTime());
			cstmt.setString(21, "0");
			status = cstmt.executeUpdate();
			cardVO.setId(cstmt.getString(21));
		} catch (SQLException e) {
			CardDAOException cardDAOException =  new CardDAOException(e);
			throw cardDAOException;
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update() throws CardDAOException {
		return 0;
	}

	public int delete() throws CardDAOException {
		return 0;
	}

	public VO searchByID(String ID) throws CardDAOException {
		return null;
	}

	public ArrayList<CardVO> search() throws CardDAOException {
		return null;
	}

	public CardVO searchCard(CardVO cardVO) throws CardDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CARD( ?, ?, ?, ?)}");
			pstmt.setString(1, cardVO.getName());
			pstmt.setString(2, cardVO.getBin());
			pstmt.setString(3, cardVO.getExpYear());
			pstmt.setString(4, cardVO.getExpMonth());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					cardVO.setId(resultSet.getString("id"));
				}
			}
		} catch (SQLException e) {
			CardDAOException cardDAOException =  new CardDAOException(e);
			throw cardDAOException;
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return cardVO;
	}
	
	public CardVO searchCardByNumber(CardVO cardVO) throws CardDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CARD_BY_NUMBER( ? )}");
			pstmt.setString(1, cardVO.getNumber());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				cardVO = null;
				while (resultSet.next()) {
					cardVO = new CardVO();
					cardVO.setId(resultSet.getString("Card_ID"));
				}
			}
		} catch (SQLException e) {
			CardDAOException cardDAOException =  new CardDAOException(e);
			throw cardDAOException;
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return cardVO;
	}
	
	public ArrayList<CardVO> searchCardsByCustomer(CardVO cardVO) throws CardDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<CardVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CARD_BY_CUSTOMER( ? )}");
			pstmt.setString(1, cardVO.getCustomerId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<CardVO>();
				while (resultSet.next()) {
					cardVO = new CardVO();
					cardVO.setId(resultSet.getString("Card_ID"));
					cardVO.setCustomerId(resultSet.getString("Cust_ID"));
					cardVO.setCustomerId(resultSet.getString("Cust_ID"));
					cardVO.setName(resultSet.getString("Card_Name"));
					cardVO.setNumber(resultSet.getString("Card_Number"));
					cardVO.setExpMonth(resultSet.getString("Card_ExpMonth"));
					cardVO.setExpYear(resultSet.getString("Card_ExpYear"));
					cardVO.setBrand(resultSet.getString("Card_Brand"));
					cardVO.setAddressCity(resultSet.getString("Card_AddressCity"));
					cardVO.setAddresState(resultSet.getString("Card_AddressState"));
					cardVO.setAddressCountry(resultSet.getString("Card_AddressCountry"));
					list.add(cardVO);
				}
			}
		} catch (SQLException e) {
			CardDAOException cardDAOException =  new CardDAOException(e);
			throw cardDAOException;
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}
	

}
