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
import au.com.billingbuddy.dao.interfaces.ICountryBlockListDAO;
import au.com.billingbuddy.exceptions.objects.CountryBlockListDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CountryBlockListVO;
import au.com.billingbuddy.vo.objects.CountryVO;

public class CountryBlockListDAO extends MySQLConnection implements ICountryBlockListDAO {

	public CountryBlockListDAO() throws MySQLConnectionException{
		super();
	}
	
	public CountryBlockListDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(CountryBlockListVO countryBlockListVO) throws CountryBlockListDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_COUNTRYBLOCKLIST(?,?,?,?,?,?,?)}");
			cstmt.setString(1,countryBlockListVO.getCountryId());
			cstmt.setString(2,countryBlockListVO.getTransaction());
			cstmt.setString(3,countryBlockListVO.getMerchantServerLocation());
			cstmt.setString(4,countryBlockListVO.getMerchantRegistrationLocation());
			cstmt.setString(5,countryBlockListVO.getCreditCardIssueLocation());
			cstmt.setString(6,countryBlockListVO.getCreditCardHolderLocation());
			cstmt.setString(7,"0");
			status = cstmt.executeUpdate();
			countryBlockListVO.setId(cstmt.getString(7));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CountryBlockListDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(CountryBlockListVO countryBlockListVO) throws CountryBlockListDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_COUNTRYBLOCKLIST(?,?,?,?,?,?,?)}");
			cstmt.setString(1,countryBlockListVO.getCountryId());
			cstmt.setString(2,countryBlockListVO.getTransaction());
			cstmt.setString(3,countryBlockListVO.getMerchantServerLocation());
			cstmt.setString(4,countryBlockListVO.getMerchantRegistrationLocation());
			cstmt.setString(5,countryBlockListVO.getCreditCardIssueLocation());
			cstmt.setString(6,countryBlockListVO.getCreditCardHolderLocation());
			cstmt.setString(7,"0");
			status = cstmt.executeUpdate();
			countryBlockListVO.setId(cstmt.getString(7));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CountryBlockListDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public CountryBlockListVO searchByID(String ID) throws CountryBlockListDAOException {
		return null;
	}

	public ArrayList<CountryBlockListVO> search() throws CountryBlockListDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<CountryBlockListVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_COUNTRYBLOCKLIST()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<CountryBlockListVO>();
				while (resultSet.next()) {
					CountryBlockListVO countryBlockListVO = new CountryBlockListVO();
					countryBlockListVO.setCountryVO(new CountryVO());
					countryBlockListVO.getCountryVO().setName(resultSet.getString("Coun_Name"));
					countryBlockListVO.getCountryVO().setNumeric(resultSet.getString("Coun_Numeric"));
					countryBlockListVO.setId(resultSet.getString("Cobl_ID"));
					countryBlockListVO.setTransaction(resultSet.getString("Cobl_Transaction"));
					countryBlockListVO.setMerchantServerLocation(resultSet.getString("Cobl_MerchantServerLocation"));
					countryBlockListVO.setMerchantRegistrationLocation(resultSet.getString("Cobl_MerchantRegistrationLocation"));
					countryBlockListVO.setCreditCardIssueLocation(resultSet.getString("Cobl_CreditCardIssueLocation"));
					countryBlockListVO.setCreditCardHolderLocation(resultSet.getString("Cobl_CreditCardHolderLocation"));
					list.add(countryBlockListVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CountryBlockListDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}


}
