package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import au.com.billingbuddy.exceptions.objects.SubscriptionsToProcessDAOException;
import au.com.billingbuddy.vo.objects.SubscriptionsToProcessVO;

public interface IDailySubscriptionDAO {
	public int update(SubscriptionsToProcessVO subscriptionsToProcessVO) throws SubscriptionsToProcessDAOException;
	public HashMap<String,String> execute() throws SubscriptionsToProcessDAOException;
//	public int updateUnPaid(SubscriptionsToProcessVO subscriptionsToProcessVO) throws SubscriptionsToProcessDAOException;
	public ArrayList<SubscriptionsToProcessVO> search() throws SubscriptionsToProcessDAOException;
//	public ArrayList<SubscriptionsToProcessVO> searchUnPaid() throws SubscriptionsToProcessDAOException;
//	public int searchUnPaidBloked() throws SubscriptionsToProcessDAOException;
}
