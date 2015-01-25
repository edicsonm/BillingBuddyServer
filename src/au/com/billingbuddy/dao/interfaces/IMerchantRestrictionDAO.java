package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.MerchantDAOException;
import au.com.billingbuddy.exceptions.objects.MerchantRestrictionDAOException;
import au.com.billingbuddy.vo.objects.MerchantRestrictionVO;
import au.com.billingbuddy.vo.objects.MerchantVO;

public interface IMerchantRestrictionDAO {
	public int insert(MerchantRestrictionVO merchantRestrictionVO) throws MerchantRestrictionDAOException;
	public int update(MerchantRestrictionVO merchantRestrictionVO) throws MerchantRestrictionDAOException;
	public int delete(MerchantRestrictionVO merchantRestrictionVO) throws MerchantRestrictionDAOException;
	public ArrayList<MerchantRestrictionVO> searchDetails(MerchantRestrictionVO merchantRestrictionVO) throws MerchantRestrictionDAOException;
	public MerchantRestrictionVO searchByID(String ID) throws MerchantRestrictionDAOException;
	public ArrayList<MerchantRestrictionVO> search() throws MerchantRestrictionDAOException;
}
