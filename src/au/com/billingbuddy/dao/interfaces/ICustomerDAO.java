package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.CustomerDAOException;
import au.com.billingbuddy.vo.objects.CustomerVO;
import au.com.billingbuddy.vo.objects.VO;

public interface ICustomerDAO {
	public int insert(CustomerVO customerVO) throws CustomerDAOException;
	public CustomerVO searchBin(CustomerVO customerVO) throws CustomerDAOException;
	public int update() throws CustomerDAOException;
	public int delete() throws CustomerDAOException;
	public VO searchByID(String ID) throws CustomerDAOException;
	public ArrayList<CustomerVO> search() throws CustomerDAOException;
}
