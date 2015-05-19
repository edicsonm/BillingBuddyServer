package au.com.billingbuddy.dao.objects;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.connection.objects.MySQLConnection;
import au.com.billingbuddy.connection.objects.MySQLErrorLogConnection;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.interfaces.IErrorLogDAO;
import au.com.billingbuddy.exceptions.objects.ErrorLogDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.vo.objects.ErrorLogVO;

public class ErrorLogDAO extends MySQLErrorLogConnection implements IErrorLogDAO {
	
	ConfigurationSystem configurationSystem = ConfigurationSystem.getInstance();
	
	public ErrorLogDAO() throws MySQLConnectionException{
		super();
	}
	
	public ErrorLogDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(ErrorLogVO errorLogVO) throws ErrorLogDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_ERROR_LOG( ?,?,?,?)}");
			cstmt.setString(1,errorLogVO.getProcessName());
			cstmt.setString(2,errorLogVO.getType());
			cstmt.setString(3,errorLogVO.getInformation());
			cstmt.setString(4,"0");
			status = cstmt.executeUpdate();
			errorLogVO.setId(cstmt.getString(4));
		} catch (SQLException e) {
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update() throws ErrorLogDAOException {
		return 0;
	}

	public int delete() throws ErrorLogDAOException {
		return 0;
	}

	public ArrayList<ErrorLogVO> search() throws ErrorLogDAOException {
		return null;
	}

	public ArrayList<ErrorLogVO> search(ErrorLogVO errorLogVO) throws ErrorLogDAOException {
		return null;
	}
	
	/*public int insert(BinVO binVO) throws BinDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_BIN( ?, ? , ?, ?, ?, ?, ?, ?, ?)}");
			cstmt.setString(1, binVO.getBin());
			cstmt.setString(2, binVO.getBrand());
			cstmt.setString(3, binVO.getSuBrand());
			cstmt.setString(4, binVO.getCountryCode());
			cstmt.setString(5, binVO.getCountryName());
			cstmt.setString(6, binVO.getBankName());
			cstmt.setString(7, binVO.getCardType());
			cstmt.setString(8, binVO.getCardCategory());
			cstmt.setString(9, null);
			status = cstmt.executeUpdate();
			binVO.setId(cstmt.getString(9));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BinDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public BinVO searchBin(BinVO binVO) throws BinDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_BIN( ? )}");
			pstmt.setString(1, binVO.getBin());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					binVO.setId(resultSet.getString("Bin_Id"));
					binVO.setBin(resultSet.getString("Bin_Bin"));
					binVO.setBrand(resultSet.getString("Bin_Brand"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BinDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return binVO;
	}*/

}
