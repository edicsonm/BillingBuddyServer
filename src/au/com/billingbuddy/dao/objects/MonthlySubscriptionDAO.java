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
import au.com.billingbuddy.dao.interfaces.IMonthlySubscriptionDAO;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.MonthlySubscriptionDAOException;
import au.com.billingbuddy.vo.objects.MonthlySubscriptionVO;

public class MonthlySubscriptionDAO extends MySQLConnection implements IMonthlySubscriptionDAO {

	public MonthlySubscriptionDAO() throws MySQLConnectionException{
		super();
	}
	
	public MonthlySubscriptionDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int update(MonthlySubscriptionVO monthlySubscriptionVO) throws MonthlySubscriptionDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_INDUSTRY(?,?,?)}");
			/*cstmt.setString(1,industryVO.getDescription());
			cstmt.setString(2,industryVO.getStatus());
			cstmt.setString(3,industryVO.getId());
			status = cstmt.executeUpdate();
			industryVO.setId(cstmt.getString(3));*/
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MonthlySubscriptionDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public ArrayList<MonthlySubscriptionVO> search() throws MonthlySubscriptionDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<MonthlySubscriptionVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_INDUSTRY()}");
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<MonthlySubscriptionVO>();
				while (resultSet.next()) {
					MonthlySubscriptionVO monthlySubscriptionVO = new MonthlySubscriptionVO();
					/*industryVO.setId(resultSet.getString("Indu_ID"));
					industryVO.setDescription(resultSet.getString("Indu_Description"));
					industryVO.setStatus(resultSet.getString("Indu_Status"));
					industryVO.setCreationTime(resultSet.getString("Indu_CreateTime"));*/
					list.add(monthlySubscriptionVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MonthlySubscriptionDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}

}
