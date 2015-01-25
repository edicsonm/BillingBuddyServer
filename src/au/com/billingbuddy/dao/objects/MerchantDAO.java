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
import au.com.billingbuddy.dao.interfaces.IMerchantDAO;
import au.com.billingbuddy.exceptions.objects.MerchantDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CountryVO;
import au.com.billingbuddy.vo.objects.MerchantVO;

public class MerchantDAO extends MySQLConnection implements IMerchantDAO {

	public MerchantDAO() throws MySQLConnectionException {
		super();
	}
	
	public MerchantDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(MerchantVO merchantVO) throws MerchantDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_MERCHANT(?,?,?)}");
			cstmt.setString(1,merchantVO.getCountryNumeric());
			cstmt.setString(2,merchantVO.getName());
			cstmt.setString(3,"0");
			status = cstmt.executeUpdate();
			merchantVO.setId(cstmt.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(MerchantVO merchantVO) throws MerchantDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_MERCHANT(?,?,?)}");
			cstmt.setString(1,merchantVO.getCountryNumeric());
			cstmt.setString(2,merchantVO.getName());
			cstmt.setString(3,merchantVO.getId());
			status = cstmt.executeUpdate();
			merchantVO.setId(cstmt.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int delete(MerchantVO merchantVO) throws MerchantDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_DELETE_MERCHANT(?)}");
			cstmt.setString(1,merchantVO.getId());
			status = cstmt.executeUpdate();
			merchantVO.setId(cstmt.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public MerchantVO searchDetail(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_DETAIL(?)}");
			pstmt.setString(1,merchantVO.getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					merchantVO = new MerchantVO();
					merchantVO.setId(resultSet.getString("Merc_ID"));
					merchantVO.setCountryNumeric(resultSet.getString("Coun_Numeric"));
					merchantVO.setCountryVO(new CountryVO());
					merchantVO.getCountryVO().setName(resultSet.getString("Coun_Name"));
					merchantVO.setName(resultSet.getString("Merc_Name"));
				}
			}else{
				merchantVO = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return merchantVO;
	}

	public ArrayList<MerchantVO> search() throws MerchantDAOException {
		return null;
	}

	public ArrayList<MerchantVO> search(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<MerchantVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<MerchantVO>();
				while (resultSet.next()) {
					merchantVO = new MerchantVO();
					merchantVO.setId(resultSet.getString("Merc_ID"));
					merchantVO.setCountryNumeric(resultSet.getString("Coun_Numeric"));
					merchantVO.setCountryVO(new CountryVO());
					merchantVO.getCountryVO().setName(resultSet.getString("Coun_Name"));
					merchantVO.setName(resultSet.getString("Merc_Name"));
					list.add(merchantVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}

	public MerchantVO verifyRestrictionByAmount(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_VERIFY_MERCHANT_RESTRICTION_BY_AMOUNT(?, ?)}");
			pstmt.setString(1,merchantVO.getId());
			pstmt.setString(2,merchantVO.getTimeUnit());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					merchantVO.setAmountTransactions(resultSet.getString("Amount_Transactions"));
					merchantVO.setSince(resultSet.getString("Since"));
					merchantVO.setTo(resultSet.getString("To"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return merchantVO;
	}

	public MerchantVO verifyRestrictionByTransactions(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_VERIFY_MERCHANT_RESTRICTION_BY_TRANSACTIONS(?, ?)}");
			pstmt.setString(1,merchantVO.getId());
			pstmt.setString(2,merchantVO.getTimeUnit());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					merchantVO.setNumberTransactions(resultSet.getString("Number_Transactions"));
					merchantVO.setSince(resultSet.getString("Since"));
					merchantVO.setTo(resultSet.getString("To"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return merchantVO;
	}


}
