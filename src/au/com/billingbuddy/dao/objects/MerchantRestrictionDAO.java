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
import au.com.billingbuddy.dao.interfaces.IMerchantRestrictionDAO;
import au.com.billingbuddy.exceptions.objects.MerchantRestrictionDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.MerchantRestrictionVO;
import au.com.billingbuddy.vo.objects.MerchantVO;

public class MerchantRestrictionDAO extends MySQLConnection implements IMerchantRestrictionDAO {

	public MerchantRestrictionDAO() throws MySQLConnectionException{
		super();
	}
	
	public MerchantRestrictionDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(MerchantRestrictionVO merchantRestrictionVO) throws MerchantRestrictionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_MERCHANT_RESTRICTION(?,?,?,?,?)}");
			cstmt.setString(1,merchantRestrictionVO.getMerchantId());
			cstmt.setString(2,merchantRestrictionVO.getValue());
			cstmt.setString(3,merchantRestrictionVO.getConcept());
			cstmt.setString(4,merchantRestrictionVO.getTimeUnit());
			cstmt.setString(5,"0");
			status = cstmt.executeUpdate();
			merchantRestrictionVO.setId(cstmt.getString(5));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantRestrictionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(MerchantRestrictionVO merchantRestrictionVO) throws MerchantRestrictionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_MERCHANT_RESTRICTION(?,?,?,?,?)}");
			cstmt.setString(1,merchantRestrictionVO.getMerchantId());
			cstmt.setString(2,merchantRestrictionVO.getValue());
			cstmt.setString(3,merchantRestrictionVO.getConcept());
			cstmt.setString(4,merchantRestrictionVO.getTimeUnit());
			cstmt.setString(5,merchantRestrictionVO.getId());
			status = cstmt.executeUpdate();
			merchantRestrictionVO.setId(cstmt.getString(5));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantRestrictionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int delete(MerchantRestrictionVO merchantRestrictionVO) throws MerchantRestrictionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_DELETE_MERCHANT_RESTRICTION(?)}");
			cstmt.setString(1,merchantRestrictionVO.getId());
			status = cstmt.executeUpdate();
			merchantRestrictionVO.setId(cstmt.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantRestrictionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public MerchantRestrictionVO searchByID(String ID) throws MerchantRestrictionDAOException {
		return null;
	}

	public ArrayList<MerchantRestrictionVO> search() throws MerchantRestrictionDAOException {
		return null;
	}

	public ArrayList<MerchantRestrictionVO> search(MerchantRestrictionVO merchantRestrictionVO) throws MerchantRestrictionDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<MerchantRestrictionVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_RESTRICTION()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<MerchantRestrictionVO>();
				while (resultSet.next()) {
					merchantRestrictionVO = new MerchantRestrictionVO();
					merchantRestrictionVO.setId(resultSet.getString("Mere_ID"));
					merchantRestrictionVO.setMerchantId(resultSet.getString("Merc_ID"));
					merchantRestrictionVO.setValue(resultSet.getString("Mere_Value"));
					merchantRestrictionVO.setConcept(resultSet.getString("Mere_Concept"));
					merchantRestrictionVO.setTimeUnit(resultSet.getString("Mere_TimeUnit"));
					merchantRestrictionVO.setMerchantVO(new MerchantVO());
					merchantRestrictionVO.getMerchantVO().setName(resultSet.getString("Merc_Name"));
					list.add(merchantRestrictionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantRestrictionDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}


}
