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
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_CARD( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )}");
			cstmt.setString(1,cardVO.getCustomerId());
			cstmt.setString(2,cardVO.getCardIdStripe());
			cstmt.setString(3,cardVO.getBrand());
			cstmt.setString(4,cardVO.getExpMonth());
			cstmt.setString(5,cardVO.getExpYear());
			cstmt.setString(6,cardVO.getFingerPrint());
			cstmt.setString(7,cardVO.getFunding());
			cstmt.setString(8,cardVO.getNumber());
			cstmt.setString(9,cardVO.getLast4());
			cstmt.setString(10,cardVO.getAddressCity());
			cstmt.setString(11,cardVO.getAddressCountry());
			cstmt.setString(12,cardVO.getAddresState());
			cstmt.setString(13,cardVO.getAddressLine1());
			cstmt.setString(14,cardVO.getAddressLine2());
			cstmt.setString(15,cardVO.getAddressZip());
			cstmt.setString(16,cardVO.getCountry());
			cstmt.setString(17,cardVO.getBlackListed());
			cstmt.setString(18,cardVO.getBlackListedReason());
			cstmt.setString(19,cardVO.getBlackListedCreateTime());
			cstmt.setString(20, "0");
			status = cstmt.executeUpdate();
			cardVO.setId(cstmt.getString(20));
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

}
