package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.MerchantDAOException;
import au.com.billingbuddy.vo.objects.MerchantVO;

public interface IMerchantDAO {
	public int insert(MerchantVO merchantVO) throws MerchantDAOException;
	public int update(MerchantVO merchantVO) throws MerchantDAOException;
	public int delete(MerchantVO merchantVO) throws MerchantDAOException;
	public MerchantVO searchByID(String ID) throws MerchantDAOException;
	public ArrayList<MerchantVO> search() throws MerchantDAOException;
}
