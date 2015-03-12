package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.SubscriptionDAOException;
import au.com.billingbuddy.vo.objects.SubscriptionVO;

public interface ISubscriptionDAO {
	public int insert(SubscriptionVO subscriptionVO) throws SubscriptionDAOException;
	public int update(SubscriptionVO subscriptionVO) throws SubscriptionDAOException;
	public int delete(SubscriptionVO subscriptionVO) throws SubscriptionDAOException;
	public SubscriptionVO searchByID(String ID) throws SubscriptionDAOException;
	public ArrayList<SubscriptionVO> search() throws SubscriptionDAOException;
}
