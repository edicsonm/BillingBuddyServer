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
import au.com.billingbuddy.dao.interfaces.ICountryRestrictionDAO;
import au.com.billingbuddy.exceptions.objects.CountryRestrictionDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CountryRestrictionVO;
import au.com.billingbuddy.vo.objects.CountryVO;

public class CountryRestrictionDAO extends MySQLConnection implements ICountryRestrictionDAO {

	public CountryRestrictionDAO() throws MySQLConnectionException{
		super();
	}
	
	public CountryRestrictionDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(CountryRestrictionVO countryRestrictionVO) throws CountryRestrictionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_COUNTRY_RESTRICTION(?,?,?,?,?)}");
			cstmt.setString(1,countryRestrictionVO.getCountryNumeric());
			cstmt.setString(2,countryRestrictionVO.getValue());
			cstmt.setString(3,countryRestrictionVO.getConcept());
			cstmt.setString(4,countryRestrictionVO.getTimeUnit());
			cstmt.setString(5,"0");
			status = cstmt.executeUpdate();
			countryRestrictionVO.setId(cstmt.getString(5));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CountryRestrictionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(CountryRestrictionVO countryRestrictionVO) throws CountryRestrictionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_COUNTRY_RESTRICTION(?,?,?,?,?)}");
			cstmt.setString(1,countryRestrictionVO.getCountryNumeric());
			cstmt.setString(2,countryRestrictionVO.getValue());
			cstmt.setString(3,countryRestrictionVO.getConcept());
			cstmt.setString(4,countryRestrictionVO.getTimeUnit());
			cstmt.setString(5,countryRestrictionVO.getId());
			status = cstmt.executeUpdate();
			countryRestrictionVO.setId(cstmt.getString(5));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CountryRestrictionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int delete(CountryRestrictionVO countryRestrictionVO) throws CountryRestrictionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_DELETE_COUNTRY_RESTRICTION(?)}");
			cstmt.setString(1,countryRestrictionVO.getId());
			status = cstmt.executeUpdate();
			countryRestrictionVO.setId(cstmt.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CountryRestrictionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public CountryRestrictionVO searchByID(String ID) throws CountryRestrictionDAOException {
		return null;
	}

	public ArrayList<CountryRestrictionVO> search() throws CountryRestrictionDAOException {
		return null;
	}

	public ArrayList<CountryRestrictionVO> search(CountryRestrictionVO countryRestrictionVO) throws CountryRestrictionDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<CountryRestrictionVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_COUNTRY_RESTRICTION()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<CountryRestrictionVO>();
				while (resultSet.next()) {
					countryRestrictionVO = new CountryRestrictionVO();
					countryRestrictionVO.setId(resultSet.getString("Core_ID"));
					countryRestrictionVO.setCountryNumeric(resultSet.getString("Coun_Numeric"));
					countryRestrictionVO.setValue(resultSet.getString("Core_Value"));
					countryRestrictionVO.setConcept(resultSet.getString("Core_Concept"));
					countryRestrictionVO.setTimeUnit(resultSet.getString("Core_TimeUnit"));
					countryRestrictionVO.setCountryVO(new CountryVO());
					countryRestrictionVO.getCountryVO().setName(resultSet.getString("Coun_Name"));
					list.add(countryRestrictionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CountryRestrictionDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}


}
