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
import au.com.billingbuddy.dao.interfaces.IUserMerchantDAO;
import au.com.billingbuddy.exceptions.objects.MerchantRestrictionDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.exceptions.objects.UserMerchantDAOException;
import au.com.billingbuddy.vo.objects.MerchantVO;
import au.com.billingbuddy.vo.objects.UserMerchantVO;

public class UserMerchantDAO extends MySQLConnection implements IUserMerchantDAO {

	public UserMerchantDAO() throws MySQLConnectionException{
		super();
	}
	
	public UserMerchantDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public UserMerchantVO searchByID(UserMerchantVO userMerchantVO) throws UserMerchantDAOException {
		return userMerchantVO;
	}

	public ArrayList<UserMerchantVO> search() throws UserMerchantDAOException {
//		Connection connection = this.connection;
//		ResultSet resultSet = null; 
//		PreparedStatement pstmt = null;
//		ArrayList<UserMerchantVO> list = null;
//		try {
//			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CURRENCY( )}");
//			resultSet = (ResultSet)pstmt.executeQuery();
//			if (resultSet != null) {
//				list = new ArrayList<UserMerchantVO>();
//				while (resultSet.next()) {
//					UserMerchantVO currencyVO = new UserMerchantVO();
//					currencyVO.setNumericCode(resultSet.getString("Curr_NumericCode"));
//					currencyVO.setCountryNumeric(resultSet.getString("Coun_Numeric"));
//					currencyVO.setCountryName(resultSet.getString("Coun_Name"));
//					currencyVO.setName(resultSet.getString("Curr_Name"));
//					currencyVO.setAlphabeticCode(resultSet.getString("Curr_AlphabeticCode"));
//					currencyVO.setMinorUnit(resultSet.getString("Curr_MinorUnit"));
//					list.add(currencyVO);
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new UserMerchantDAOException(e);
//		} finally {
//			PsRs(pstmt, resultSet,connection);
//		}
//		return list;
		return null;
	}

	public int insert(UserMerchantVO userMerchantVO) throws UserMerchantDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_USER_MERCHANT(?,?)}");
			cstmt.setString(1,userMerchantVO.getUserId());
			cstmt.setString(2,userMerchantVO.getMerchantId());
			status = cstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserMerchantDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public ArrayList<UserMerchantVO> search(UserMerchantVO userMerchantVO) throws UserMerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<UserMerchantVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANTS_USER( ? )}");
			pstmt.setString(1,userMerchantVO.getUserId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<UserMerchantVO>();
				while (resultSet.next()) {
					UserMerchantVO userMerchantVOAUX = new UserMerchantVO();
					userMerchantVOAUX.setMerchantId(resultSet.getString("Merc_ID"));
					userMerchantVOAUX.setMerchantVO(new MerchantVO());
					userMerchantVOAUX.getMerchantVO().setName(resultSet.getString("Merc_Name"));
					list.add(userMerchantVOAUX);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserMerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet, connection);
		}
		return list;
	}
	
	public UserMerchantVO searchMerchantUser(UserMerchantVO userMerchantVO) throws UserMerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_USER( ? )}");
			pstmt.setString(1,userMerchantVO.getUserId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					userMerchantVO.setMerchantId(resultSet.getString("Merc_ID"));
					userMerchantVO.setMerchantVO(new MerchantVO());
					userMerchantVO.getMerchantVO().setName(resultSet.getString("Merc_Name"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserMerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet, connection);
		}
		return userMerchantVO;
	}

	public int rechargeAdministratorAccess( UserMerchantVO userMerchantVO) throws UserMerchantDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_USER_MERCHANT_ADMINISTRATOR(?)}");
			cstmt.setString(1,userMerchantVO.getUserId());
			status = cstmt.executeUpdate();
		} catch (SQLException e) {
			throw new UserMerchantDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

}
