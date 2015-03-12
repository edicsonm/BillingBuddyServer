package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.IndustryDAOException;
import au.com.billingbuddy.vo.objects.IndustryVO;
import au.com.billingbuddy.vo.objects.VO;

public interface IIndustryDAO {
	public int insert(IndustryVO industryVO) throws IndustryDAOException;
	public int update(IndustryVO industryVO) throws IndustryDAOException;
	public IndustryVO searchByID(String ID) throws IndustryDAOException;
	public ArrayList<IndustryVO> search() throws IndustryDAOException;
}
