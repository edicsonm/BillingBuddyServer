package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.BusinessTypeDAOException;
import au.com.billingbuddy.vo.objects.BusinessTypeVO;

public interface IBusinessTypeDAO {
	public int insert(BusinessTypeVO businessTypeVO) throws BusinessTypeDAOException;
	public int update(BusinessTypeVO businessTypeVO) throws BusinessTypeDAOException;
	public BusinessTypeVO searchByID(String ID) throws BusinessTypeDAOException;
	public ArrayList<BusinessTypeVO> search() throws BusinessTypeDAOException;
}
