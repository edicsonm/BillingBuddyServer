package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;


import au.com.billingbuddy.exceptions.objects.MerchantCustomerDAOException;
import au.com.billingbuddy.vo.objects.MerchantCustomerVO;

public interface IMerchantCustomerDAO {
	
	public int insert(MerchantCustomerVO merchantCustomerVO) throws MerchantCustomerDAOException;
	public int changeStatus(MerchantCustomerVO merchantCustomerVO) throws MerchantCustomerDAOException;
	
	public ArrayList<MerchantCustomerVO> searchMerchantsCustomer(MerchantCustomerVO merchantCustomerVO) throws MerchantCustomerDAOException;
	public ArrayList<MerchantCustomerVO> searchCustomersMerchant(MerchantCustomerVO merchantCustomerVO) throws MerchantCustomerDAOException;
	
}
