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
import au.com.billingbuddy.dao.interfaces.IBinDAO;
import au.com.billingbuddy.exceptions.objects.BinDAOException;
import au.com.billingbuddy.exceptions.objects.CardDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.BinVO;
import au.com.billingbuddy.vo.objects.VO;

public class BinDAO extends MySQLConnection implements IBinDAO {

	public BinDAO() throws MySQLConnectionException{
		super();
	}
	
	public BinDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}
	
	public int insert(BinVO binVO) throws BinDAOException {
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
	}

	public int update() throws BinDAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int delete() throws BinDAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public VO searchByID(String ID) throws BinDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<BinVO> search() throws BinDAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
