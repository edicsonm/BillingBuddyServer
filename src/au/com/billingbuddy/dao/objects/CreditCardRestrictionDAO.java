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
import au.com.billingbuddy.dao.interfaces.ICreditCardRestrictionDAO;
import au.com.billingbuddy.exceptions.objects.CreditCardRestrictionDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CreditCardRestrictionVO;

public class CreditCardRestrictionDAO extends MySQLConnection implements ICreditCardRestrictionDAO {

	public CreditCardRestrictionDAO() throws MySQLConnectionException{
		super();
	}
	
	public CreditCardRestrictionDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(CreditCardRestrictionVO creditCardRestrictionVO) throws CreditCardRestrictionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_CREDITCARD_RESTRICTION(?,?,?,?)}");
			cstmt.setString(1,creditCardRestrictionVO.getValue());
			cstmt.setString(2,creditCardRestrictionVO.getConcept());
			cstmt.setString(3,creditCardRestrictionVO.getTimeUnit());
			cstmt.setString(4,"0");
			status = cstmt.executeUpdate();
			creditCardRestrictionVO.setId(cstmt.getString(4));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CreditCardRestrictionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(CreditCardRestrictionVO creditCardRestrictionVO) throws CreditCardRestrictionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_CREDITCARD_RESTRICTION(?,?,?,?)}");
			cstmt.setString(1,creditCardRestrictionVO.getValue());
			cstmt.setString(2,creditCardRestrictionVO.getConcept());
			cstmt.setString(3,creditCardRestrictionVO.getTimeUnit());
			cstmt.setString(4,creditCardRestrictionVO.getId());
			status = cstmt.executeUpdate();
			creditCardRestrictionVO.setId(cstmt.getString(4));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CreditCardRestrictionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int delete(CreditCardRestrictionVO creditCardRestrictionVO) throws CreditCardRestrictionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_DELETE_CREDITCARD_RESTRICTION(?)}");
			cstmt.setString(1,creditCardRestrictionVO.getId());
			status = cstmt.executeUpdate();
			creditCardRestrictionVO.setId(cstmt.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CreditCardRestrictionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public CreditCardRestrictionVO searchByID(String ID) throws CreditCardRestrictionDAOException {
		return null;
	}

	public ArrayList<CreditCardRestrictionVO> search(CreditCardRestrictionVO creditCardRestrictionVO) throws CreditCardRestrictionDAOException {
		return null;
	}

	public ArrayList<CreditCardRestrictionVO> search() throws CreditCardRestrictionDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<CreditCardRestrictionVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CREDITCARD_RESTRICTION()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<CreditCardRestrictionVO>();
				while (resultSet.next()) {
					CreditCardRestrictionVO creditCardRestrictionVO = new CreditCardRestrictionVO();
					creditCardRestrictionVO.setId(resultSet.getString("Ccre_ID"));
					creditCardRestrictionVO.setValue(resultSet.getString("Ccre_Value"));
					creditCardRestrictionVO.setConcept(resultSet.getString("Ccre_Concept"));
					creditCardRestrictionVO.setTimeUnit(resultSet.getString("Ccre_TimeUnit"));
					list.add(creditCardRestrictionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CreditCardRestrictionDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}


}
