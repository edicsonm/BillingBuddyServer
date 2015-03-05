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
import au.com.billingbuddy.dao.interfaces.IIndustryDAO;
import au.com.billingbuddy.exceptions.objects.IndustryDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.IndustryVO;

public class IndustryDAO extends MySQLConnection implements IIndustryDAO {

	public IndustryDAO() throws MySQLConnectionException{
		super();
	}
	
	public IndustryDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(IndustryVO industryVO) throws IndustryDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_INDUSTRY(?,?)}");
			cstmt.setString(1,industryVO.getDescription());
			cstmt.setString(2,"0");
			status = cstmt.executeUpdate();
			industryVO.setId(cstmt.getString(2));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IndustryDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(IndustryVO industryVO) throws IndustryDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_INDUSTRY(?,?,?)}");
			cstmt.setString(1,industryVO.getDescription());
			cstmt.setString(2,industryVO.getStatus());
			cstmt.setString(3,industryVO.getId());
			status = cstmt.executeUpdate();
			industryVO.setId(cstmt.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IndustryDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public ArrayList<IndustryVO> search() throws IndustryDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<IndustryVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_INDUSTRY()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<IndustryVO>();
				while (resultSet.next()) {
					IndustryVO industryVO = new IndustryVO();
					industryVO.setId(resultSet.getString("Indu_ID"));
					industryVO.setDescription(resultSet.getString("Indu_Description"));
					industryVO.setStatus(resultSet.getString("Indu_Status"));
					industryVO.setCreationTime(resultSet.getString("Indu_CreateTime"));
					list.add(industryVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IndustryDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}

	public IndustryVO searchByID(String ID) throws IndustryDAOException {
		return null;
	}



}
