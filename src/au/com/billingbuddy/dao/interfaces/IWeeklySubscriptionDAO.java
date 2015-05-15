package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.WeeklySubscriptionDAOException;
import au.com.billingbuddy.vo.objects.WeeklySubscriptionVO;

public interface IWeeklySubscriptionDAO {
	public int update(WeeklySubscriptionVO weeklySubscriptionVO) throws WeeklySubscriptionDAOException;
	public ArrayList<WeeklySubscriptionVO> search() throws WeeklySubscriptionDAOException;
}
