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
import au.com.billingbuddy.dao.interfaces.IBusinessTypeDAO;
import au.com.billingbuddy.exceptions.objects.BusinessTypeDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.BusinessTypeVO;

public class BusinessTypeDAO extends MySQLConnection implements IBusinessTypeDAO {

	public BusinessTypeDAO() throws MySQLConnectionException{
		super();
	}
	
	public BusinessTypeDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(BusinessTypeVO businessTypeVO) throws BusinessTypeDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_BUSINESSTYPE(?,?)}");
			cstmt.setString(1,businessTypeVO.getDescription());
			cstmt.setString(2,"0");
			status = cstmt.executeUpdate();
			businessTypeVO.setId(cstmt.getString(2));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BusinessTypeDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(BusinessTypeVO businessTypeVO) throws BusinessTypeDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_BUSINESSTYPE(?,?,?)}");
			cstmt.setString(1,businessTypeVO.getDescription());
			cstmt.setString(2,businessTypeVO.getStatus());
			cstmt.setString(3,businessTypeVO.getId());
			status = cstmt.executeUpdate();
			businessTypeVO.setId(cstmt.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BusinessTypeDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public ArrayList<BusinessTypeVO> search() throws BusinessTypeDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<BusinessTypeVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_BUSINESSTYPE()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<BusinessTypeVO>();
				while (resultSet.next()) {
					BusinessTypeVO businessTypeVO = new BusinessTypeVO();
					businessTypeVO.setId(resultSet.getString("Buty_ID"));
					businessTypeVO.setDescription(resultSet.getString("Buty_Description"));
					businessTypeVO.setStatus(resultSet.getString("Buty_Status"));
					businessTypeVO.setCreationTime(resultSet.getString("Buty_CreateTime"));
					list.add(businessTypeVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BusinessTypeDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}

	public BusinessTypeVO searchByID(String ID) throws BusinessTypeDAOException {
		return null;
	}


}
