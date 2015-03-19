package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.exceptions.objects.UserMerchantDAOException;
import au.com.billingbuddy.vo.objects.TransactionVO;
import au.com.billingbuddy.vo.objects.UserMerchantVO;

public interface IUserMerchantDAO {
	public int insert(UserMerchantVO userMerchantVO) throws UserMerchantDAOException;
	public UserMerchantVO searchByID(UserMerchantVO userMerchantVO) throws UserMerchantDAOException;
	public ArrayList<UserMerchantVO> search() throws UserMerchantDAOException;
	public ArrayList<UserMerchantVO> search(UserMerchantVO userMerchantVO) throws UserMerchantDAOException;
	public int rechargeAdministratorAccess(UserMerchantVO userMerchantVO) throws UserMerchantDAOException;
}
