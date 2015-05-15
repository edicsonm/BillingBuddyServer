package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.DailySubscriptionDAOException;
import au.com.billingbuddy.vo.objects.DailySubscriptionVO;

public interface IDailySubscriptionDAO {
	public int update(DailySubscriptionVO dailySubscriptionVO) throws DailySubscriptionDAOException;
	public ArrayList<DailySubscriptionVO> search() throws DailySubscriptionDAOException;
}
