package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.PlanDAOException;
import au.com.billingbuddy.vo.objects.PlanVO;

public interface IPlanDAO {
	public int insert(PlanVO planVO) throws PlanDAOException;
	public int update(PlanVO planVO) throws PlanDAOException;
	public int delete(PlanVO planVO) throws PlanDAOException;
	public PlanVO searchByID(String ID) throws PlanDAOException;
	public ArrayList<PlanVO> search() throws PlanDAOException;
}
