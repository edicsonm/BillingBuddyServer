package au.com.billingbuddy.dao.objects;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.connection.objects.MySQLConnection;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.interfaces.ICustomerDAO;
import au.com.billingbuddy.exceptions.objects.CustomerDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CustomerVO;
import au.com.billingbuddy.vo.objects.VO;

public class CustomerDAO extends MySQLConnection implements ICustomerDAO{
	
	public CustomerDAO() throws MySQLConnectionException{
		super();
	}
	
	public CustomerDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}
	
	public int insert(CustomerVO customerVO) throws CustomerDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_CUSTOMER( ?, ? , ?, ?)}");
			cstmt.setString(1, customerVO.getCreateTime());
			cstmt.setString(2, customerVO.getEmail());
			cstmt.setString(3, customerVO.getPhoneNumber());
			cstmt.setString(4, "0");
			status = cstmt.executeUpdate();
			customerVO.setId(cstmt.getString(4));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CustomerDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public CustomerVO searchBin(CustomerVO customerVO)
			throws CustomerDAOException {
		return null;
	}

	public int update() throws CustomerDAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int delete() throws CustomerDAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public VO searchByID(String ID) throws CustomerDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<CustomerVO> search() throws CustomerDAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
