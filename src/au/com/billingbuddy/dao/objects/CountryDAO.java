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
import au.com.billingbuddy.dao.interfaces.ICountryDAO;
import au.com.billingbuddy.exceptions.objects.CountryDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CountryVO;

public class CountryDAO extends MySQLConnection implements ICountryDAO {

	public CountryDAO() throws MySQLConnectionException{
		super();
	}
	
	public CountryDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(CountryVO countryVO) throws CountryDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_PLAN(?,?,?,?,?,?,?,?,?)}");
//			cstmt.setString(1,planVO.getAmount());
//			cstmt.setString(2,planVO.getCreationTime());
//			cstmt.setString(3,planVO.getCurrency());
//			cstmt.setString(4,planVO.getInterval());
//			cstmt.setString(5,planVO.getIntervalCount());
//			cstmt.setString(6,planVO.getName());
//			cstmt.setString(7,planVO.getTrialPeriodDays());
//			cstmt.setString(8,planVO.getStatementDescriptor());
//			cstmt.setString(9,"0");
//			status = cstmt.executeUpdate();
//			planVO.setId(cstmt.getString(9));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CountryDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(CountryVO countryVO) throws CountryDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_PLAN(?,?,?,?,?,?,?,?,?)}");
//			cstmt.setString(1,planVO.getAmount());
//			cstmt.setString(2,planVO.getCreationTime());
//			cstmt.setString(3,planVO.getCurrency());
//			cstmt.setString(4,planVO.getInterval());
//			cstmt.setString(5,planVO.getIntervalCount());
//			cstmt.setString(6,planVO.getName());
//			cstmt.setString(7,planVO.getTrialPeriodDays());
//			cstmt.setString(8,planVO.getStatementDescriptor());
//			cstmt.setString(9,planVO.getId());
//			status = cstmt.executeUpdate();
//			planVO.setId(cstmt.getString(9));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CountryDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int delete(CountryVO countryVO) throws CountryDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_DELETE_PLAN(?)}");
//			cstmt.setString(1,planVO.getId());
//			status = cstmt.executeUpdate();
//			planVO.setId(cstmt.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CountryDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public CountryVO searchByID(String ID) throws CountryDAOException {
		return null;
	}

	public ArrayList<CountryVO> search() throws CountryDAOException {
		return null;
	}

	public ArrayList<CountryVO> search(CountryVO countryVO) throws CountryDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<CountryVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_COUNTRIES()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<CountryVO>();
				while (resultSet.next()) {
					countryVO = new CountryVO();
					countryVO.setNumeric(resultSet.getString("Coun_Numeric"));
					countryVO.setAlpha_2(resultSet.getString("Coun_Alpha_2"));
					countryVO.setAlpha_3(resultSet.getString("Coun_Alpha_3"));
					countryVO.setName(resultSet.getString("Coun_Name"));
					countryVO.setISO_3166_2(resultSet.getString("Count_ISO_3166-2"));
					list.add(countryVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CountryDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}


}
