package au.com.billingbuddy.dao.objects;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.MySQLError;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.connection.objects.MySQLConnection;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.interfaces.IMerchantConfigurationDAO;
import au.com.billingbuddy.exceptions.objects.MerchantConfigurationDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.MerchantConfigurationVO;

public class MerchantConfigurationDAO extends MySQLConnection implements IMerchantConfigurationDAO {

	public MerchantConfigurationDAO() throws MySQLConnectionException{
		super();
	}
	
	public MerchantConfigurationDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_MERCHANT_CONFIGURATION(?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,merchantConfigurationVO.getMerchantId());
			cstmt.setString(2,merchantConfigurationVO.getUrlDeny());
			cstmt.setString(3,merchantConfigurationVO.getUrlApproved());
			cstmt.setString(4,merchantConfigurationVO.getPasswordKeyStore());
			cstmt.setString(5,merchantConfigurationVO.getPrivacyKeyStore());
			cstmt.setString(6,merchantConfigurationVO.getPasswordkey());
			cstmt.setString(7,merchantConfigurationVO.getKeyName());
			cstmt.setString(8,"0");
			status = cstmt.executeUpdate();
			merchantConfigurationVO.setId(cstmt.getString(8));
		} catch (MySQLIntegrityConstraintViolationException e) {
			throw new MerchantConfigurationDAOException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantConfigurationDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_MERCHANT_CONFIGURATION(?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,merchantConfigurationVO.getMerchantId());
			cstmt.setString(2,merchantConfigurationVO.getUrlDeny());
			cstmt.setString(3,merchantConfigurationVO.getUrlApproved());
			cstmt.setString(4,merchantConfigurationVO.getPasswordKeyStore());
			cstmt.setString(5,merchantConfigurationVO.getPrivacyKeyStore());
			cstmt.setString(6,merchantConfigurationVO.getPasswordkey());
			cstmt.setString(7,merchantConfigurationVO.getKeyName());
			cstmt.setString(8,merchantConfigurationVO.getId());
			status = cstmt.executeUpdate();
			merchantConfigurationVO.setId(cstmt.getString(8));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantConfigurationDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int delete(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException {
		return 0;
	}

	public MerchantConfigurationVO searchDetail(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_CONFIGURATION_DETAIL(?)}");
			pstmt.setString(1,merchantConfigurationVO.getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					merchantConfigurationVO = new MerchantConfigurationVO();
					merchantConfigurationVO.setId(resultSet.getString("Meco_ID"));
					merchantConfigurationVO.setMerchantId(resultSet.getString("Merc_ID"));
					merchantConfigurationVO.setUrlDeny(resultSet.getString("Meco_UrlDeny"));
					merchantConfigurationVO.setUrlApproved(resultSet.getString("Meco_UrlApproved"));
					merchantConfigurationVO.setPasswordKeyStore(resultSet.getString("Meco_PasswordKeyStore"));
					merchantConfigurationVO.setPrivacyKeyStore(resultSet.getString("Meco_PrivacyKeyStore"));
					merchantConfigurationVO.setPasswordkey(resultSet.getString("Meco_Passwordkey"));
					merchantConfigurationVO.setKeyName(resultSet.getString("Meco_keyName"));
				}
			}else{
				merchantConfigurationVO = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantConfigurationDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return merchantConfigurationVO;
	}

	public ArrayList<MerchantConfigurationVO> search() throws MerchantConfigurationDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<MerchantConfigurationVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_CONFIGURATION()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<MerchantConfigurationVO>();
				while (resultSet.next()) {
					MerchantConfigurationVO merchantConfigurationVO = new MerchantConfigurationVO();
					merchantConfigurationVO.setId(resultSet.getString("Meco_ID"));
					merchantConfigurationVO.setMerchantId(resultSet.getString("Merc_ID"));
					merchantConfigurationVO.setUrlDeny(resultSet.getString("Meco_UrlDeny"));
					merchantConfigurationVO.setUrlApproved(resultSet.getString("Meco_UrlApproved"));
					merchantConfigurationVO.setPasswordKeyStore(resultSet.getString("Meco_PasswordKeyStore"));
					merchantConfigurationVO.setPrivacyKeyStore(resultSet.getString("Meco_PrivacyKeyStore"));
					merchantConfigurationVO.setPasswordkey(resultSet.getString("Meco_Passwordkey"));
					merchantConfigurationVO.setKeyName(resultSet.getString("Meco_keyName"));
					list.add(merchantConfigurationVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantConfigurationDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
		
	}

	public ArrayList<MerchantConfigurationVO> search(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException {
		return null;
	}


}
