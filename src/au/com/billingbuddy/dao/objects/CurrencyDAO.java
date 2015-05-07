package au.com.billingbuddy.dao.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.connection.objects.MySQLConnection;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.interfaces.ICurrencyDAO;
import au.com.billingbuddy.exceptions.objects.CurrencyDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CurrencyVO;

public class CurrencyDAO extends MySQLConnection implements ICurrencyDAO {

	public CurrencyDAO() throws MySQLConnectionException{
		super();
	}
	
	public CurrencyDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public CurrencyVO searchByID(CurrencyVO currencyVO) throws CurrencyDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CURRENCY_DETAIL( ? )}");
			pstmt.setString(1, currencyVO.getNumericCode());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					currencyVO.setNumericCode(resultSet.getString("Curr_NumericCode"));
					currencyVO.setCountryNumeric(resultSet.getString("Coun_Numeric"));
					currencyVO.setCountryName(resultSet.getString("Coun_Name"));
					currencyVO.setName(resultSet.getString("Curr_Name"));
					currencyVO.setAlphabeticCode(resultSet.getString("Curr_AlphabeticCode"));
					currencyVO.setMinorUnit(resultSet.getString("Curr_MinorUnit"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CurrencyDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return currencyVO;
	}

	public ArrayList<CurrencyVO> search() throws CurrencyDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<CurrencyVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CURRENCY( )}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<CurrencyVO>();
				while (resultSet.next()) {
					CurrencyVO currencyVO = new CurrencyVO();
					currencyVO.setNumericCode(resultSet.getString("Curr_NumericCode"));
					currencyVO.setCountryNumeric(resultSet.getString("Coun_Numeric"));
					currencyVO.setCountryName(resultSet.getString("Coun_Name"));
					currencyVO.setName(resultSet.getString("Curr_Name"));
					currencyVO.setAlphabeticCode(resultSet.getString("Curr_AlphabeticCode"));
					currencyVO.setMinorUnit(resultSet.getString("Curr_MinorUnit"));
					list.add(currencyVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CurrencyDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}

}
