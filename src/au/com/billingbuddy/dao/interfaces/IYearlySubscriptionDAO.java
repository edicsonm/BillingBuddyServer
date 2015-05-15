package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.YearlySubscriptionDAOException;
import au.com.billingbuddy.vo.objects.YearlySubscriptionVO;

public interface IYearlySubscriptionDAO {
	public int update(YearlySubscriptionVO yearlySubscriptionVO) throws YearlySubscriptionDAOException;
	public ArrayList<YearlySubscriptionVO> search() throws YearlySubscriptionDAOException;
}
