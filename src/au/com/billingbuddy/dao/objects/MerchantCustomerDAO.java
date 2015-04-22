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
import au.com.billingbuddy.dao.interfaces.IMerchantCustomerDAO;
import au.com.billingbuddy.exceptions.objects.MerchantCustomerDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CustomerVO;
import au.com.billingbuddy.vo.objects.MerchantCustomerVO;
import au.com.billingbuddy.vo.objects.MerchantVO;

public class MerchantCustomerDAO extends MySQLConnection implements IMerchantCustomerDAO {

	public MerchantCustomerDAO() throws MySQLConnectionException{
		super();
	}
	
	public MerchantCustomerDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(MerchantCustomerVO merchantCustomerVO) throws MerchantCustomerDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_MERCHANT_CUSTOMER(?,?,?)}");
			cstmt.setString(1,merchantCustomerVO.getMerchantId());
			cstmt.setString(2,merchantCustomerVO.getCustomerId());
			cstmt.setString(3,"0");
			status = cstmt.executeUpdate();
			merchantCustomerVO.setId(cstmt.getString(3));
		} catch (SQLException e) {
			throw new MerchantCustomerDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}


	public int changeStatus(MerchantCustomerVO merchantCustomerVO) throws MerchantCustomerDAOException {
		return 0;
	}

	public ArrayList<MerchantCustomerVO> searchMerchantsCustomer(MerchantCustomerVO merchantCustomerVO) throws MerchantCustomerDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<MerchantCustomerVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANTS_CUSTOMER( ? )}");
			pstmt.setString(1,merchantCustomerVO.getCustomerId());
			pstmt.setString(2,merchantCustomerVO.getUserId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<MerchantCustomerVO>();
				while (resultSet.next()) {
					MerchantCustomerVO merchantCustomerVOAUX = new MerchantCustomerVO();
					merchantCustomerVOAUX.setId(resultSet.getString("Mecu_ID"));
					merchantCustomerVOAUX.setMerchantId(resultSet.getString("Merc_ID"));
					merchantCustomerVOAUX.setCustomerId(resultSet.getString("Cust_ID"));
					merchantCustomerVOAUX.setMerchantVO(new MerchantVO());
					merchantCustomerVOAUX.getMerchantVO().setName(resultSet.getString("Merc_Name"));
					merchantCustomerVOAUX.setCustomerVO(new CustomerVO());
					merchantCustomerVOAUX.getCustomerVO().setEmail(resultSet.getString("Cust_Email"));
					list.add(merchantCustomerVOAUX);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantCustomerDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}

	public ArrayList<MerchantCustomerVO> searchCustomersMerchant(MerchantCustomerVO merchantCustomerVO) throws MerchantCustomerDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<MerchantCustomerVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CUSTOMERS_MERCHANT( ? , ? )}");
			pstmt.setString(1,merchantCustomerVO.getMerchantId());
			pstmt.setString(2,merchantCustomerVO.getUserId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<MerchantCustomerVO>();
				while (resultSet.next()) {
					MerchantCustomerVO merchantCustomerVOAUX = new MerchantCustomerVO();
					merchantCustomerVOAUX.setId(resultSet.getString("Mecu_ID"));
					merchantCustomerVOAUX.setMerchantId(resultSet.getString("Merc_ID"));
					merchantCustomerVOAUX.setCustomerId(resultSet.getString("Cust_ID"));
					merchantCustomerVOAUX.setMerchantVO(new MerchantVO());
					merchantCustomerVOAUX.getMerchantVO().setName(resultSet.getString("Merc_Name"));
					merchantCustomerVOAUX.setCustomerVO(new CustomerVO());
					merchantCustomerVOAUX.getCustomerVO().setEmail(resultSet.getString("Cust_Email"));
					merchantCustomerVOAUX.getCustomerVO().setCreateTime(resultSet.getString("Cust_CreateTime"));
					list.add(merchantCustomerVOAUX);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantCustomerDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}

//	public int update(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException {
//		CallableStatement cstmt = null;
//		int status = 0;
//		try {
//			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_MERCHANT_CONFIGURATION(?,?,?)}");
//			cstmt.setString(1,merchantConfigurationVO.getUrlDeny());
//			cstmt.setString(2,merchantConfigurationVO.getUrlApproved());
//			cstmt.setString(3,merchantConfigurationVO.getId());
//			status = cstmt.executeUpdate();
//			merchantConfigurationVO.setId(cstmt.getString(3));
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new MerchantConfigurationDAOException(e);
//		} finally {
//			Cs(cstmt, getConnection());
//		}
//		return status;
//	}
//
//	public int delete(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException {
//		return 0;
//	}
//
//	public MerchantConfigurationVO searchDetail(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException {
//		Connection connection = this.connection;
//		ResultSet resultSet = null; 
//		PreparedStatement pstmt = null;
//		try {
//			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_CONFIGURATION_DETAIL(?)}");
//			pstmt.setString(1,merchantConfigurationVO.getId());
//			resultSet = (ResultSet)pstmt.executeQuery();
//			if (resultSet != null) {
//				while (resultSet.next()) {
//					merchantConfigurationVO = new MerchantConfigurationVO();
//					merchantConfigurationVO.setId(resultSet.getString("Meco_ID"));
//					merchantConfigurationVO.setMerchantId(resultSet.getString("Merc_ID"));
//					merchantConfigurationVO.setUrlDeny(resultSet.getString("Meco_UrlDeny"));
//					merchantConfigurationVO.setUrlApproved(resultSet.getString("Meco_UrlApproved"));
//				}
//			}else{
//				merchantConfigurationVO = null;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new MerchantConfigurationDAOException(e);
//		} finally {
//			PsRs(pstmt, resultSet,connection);
//		}
//		return merchantConfigurationVO;
//	}
//
//	public ArrayList<MerchantConfigurationVO> searchByUserID(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException {
//		Connection connection = this.connection;
//		ResultSet resultSet = null; 
//		PreparedStatement pstmt = null;
//		ArrayList<MerchantConfigurationVO> list = null;
//		try {
//			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_CONFIGURATION( ? )}");
//			pstmt.setString(1,merchantConfigurationVO.getUserId());
//			resultSet = (ResultSet)pstmt.executeQuery();
//			if (resultSet != null) {
//				list = new ArrayList<MerchantConfigurationVO>();
//				while (resultSet.next()) {
//					MerchantConfigurationVO merchantConfigurationVOAUX = new MerchantConfigurationVO();
//					merchantConfigurationVOAUX.setId(resultSet.getString("Meco_ID"));
//					merchantConfigurationVOAUX.setMerchantVO(new MerchantVO());
//					merchantConfigurationVOAUX.getMerchantVO().setName(resultSet.getString("Merc_Name"));
//					merchantConfigurationVOAUX.setMerchantId(resultSet.getString("Merc_ID"));
//					merchantConfigurationVOAUX.setUrlDeny(resultSet.getString("Meco_UrlDeny"));
//					merchantConfigurationVOAUX.setUrlApproved(resultSet.getString("Meco_UrlApproved"));
//					list.add(merchantConfigurationVOAUX);
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new MerchantConfigurationDAOException(e);
//		} finally {
//			PsRs(pstmt, resultSet,connection);
//		}
//		return list;
//		
//	}
//
//	public ArrayList<MerchantConfigurationVO> search(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException {
//		return null;
//	}
//
//	public MerchantConfigurationVO searchDetailByMerchantId(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException {
//		Connection connection = this.connection;
//		ResultSet resultSet = null; 
//		PreparedStatement pstmt = null;
//		try {
//			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_CONFIGURATION_DETAIL_BY_MERCHANT_ID(?)}");
//			pstmt.setString(1,merchantConfigurationVO.getMerchantId());
//			resultSet = (ResultSet)pstmt.executeQuery();
//			if (resultSet != null) {
//				merchantConfigurationVO = null;
//				while (resultSet.next()) {
//					merchantConfigurationVO = new MerchantConfigurationVO();
//					merchantConfigurationVO.setId(resultSet.getString("Meco_ID"));
//					merchantConfigurationVO.setMerchantId(resultSet.getString("Merc_ID"));
//					merchantConfigurationVO.setUrlDeny(resultSet.getString("Meco_UrlDeny"));
//					merchantConfigurationVO.setUrlApproved(resultSet.getString("Meco_UrlApproved"));
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new MerchantConfigurationDAOException(e);
//		} finally {
//			PsRs(pstmt, resultSet,connection);
//		}
//		return merchantConfigurationVO;
//	}


}
