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
import au.com.billingbuddy.dao.interfaces.IPlanDAO;
import au.com.billingbuddy.exceptions.objects.PlanDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.MerchantVO;
import au.com.billingbuddy.vo.objects.PlanVO;

public class PlanDAO extends MySQLConnection implements IPlanDAO {

	public PlanDAO() throws MySQLConnectionException{
		super();
	}
	
	public PlanDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(PlanVO planVO) throws PlanDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_PLAN(?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,planVO.getMerchantId());
			cstmt.setString(2,planVO.getAmount());
			cstmt.setString(3,planVO.getCreationTime());
			cstmt.setString(4,planVO.getCurrency());
			cstmt.setString(5,planVO.getInterval());
			cstmt.setString(6,planVO.getIntervalCount());
			cstmt.setString(7,planVO.getName());
			cstmt.setString(8,planVO.getTrialPeriodDays());
			cstmt.setString(9,planVO.getStatementDescriptor());
			cstmt.setString(10,"0");
			status = cstmt.executeUpdate();
			planVO.setId(cstmt.getString(10));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PlanDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(PlanVO planVO) throws PlanDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_PLAN(?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,planVO.getMerchantId());
			cstmt.setString(2,planVO.getAmount());
			cstmt.setString(3,planVO.getCreationTime());
			cstmt.setString(4,planVO.getCurrency());
			cstmt.setString(5,planVO.getInterval());
			cstmt.setString(6,planVO.getIntervalCount());
			cstmt.setString(7,planVO.getName());
			cstmt.setString(8,planVO.getTrialPeriodDays());
			cstmt.setString(9,planVO.getStatementDescriptor());
			cstmt.setString(10,planVO.getId());
			status = cstmt.executeUpdate();
			planVO.setId(cstmt.getString(10));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PlanDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int delete(PlanVO planVO) throws PlanDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_DELETE_PLAN(?)}");
			cstmt.setString(1,planVO.getId());
			status = cstmt.executeUpdate();
			planVO.setId(cstmt.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PlanDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public PlanVO searchByID(String ID) throws PlanDAOException {
		return null;
	}

	public ArrayList<PlanVO> search() throws PlanDAOException {
		return null;
	}

	public ArrayList<PlanVO> search(PlanVO planVO) throws PlanDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<PlanVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_PLAN(?)}");
			pstmt.setString(1, planVO.getUserId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<PlanVO>();
				while (resultSet.next()) {
					planVO = new PlanVO();
					planVO.setId(resultSet.getString("Plan_id"));
					planVO.setMerchantId(resultSet.getString("Merc_ID"));
					planVO.setMerchantVO(new MerchantVO());
					planVO.getMerchantVO().setName(resultSet.getString("Merc_Name"));
					planVO.setAmount(resultSet.getString("Plan_Amount"));
					planVO.setCreationTime(resultSet.getString("Plan_CreateTime"));
					planVO.setCurrency(resultSet.getString("Plan_Currency"));
					planVO.setInterval(resultSet.getString("Plan_Interval"));
					planVO.setIntervalCount(resultSet.getString("Plan_IntervalCount"));
					planVO.setName(resultSet.getString("Plan_Name"));
					planVO.setTrialPeriodDays(resultSet.getString("Plan_TrialPeriodDays"));
					planVO.setStatementDescriptor(resultSet.getString("Plan_StatementDescriptor"));
					list.add(planVO);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PlanDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}


}
