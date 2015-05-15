package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.MonthlySubscriptionDAOException;
import au.com.billingbuddy.vo.objects.MonthlySubscriptionVO;

public interface IMonthlySubscriptionDAO {
	public int update(MonthlySubscriptionVO monthlySubscriptionVO) throws MonthlySubscriptionDAOException;
	public ArrayList<MonthlySubscriptionVO> search() throws MonthlySubscriptionDAOException;
}
