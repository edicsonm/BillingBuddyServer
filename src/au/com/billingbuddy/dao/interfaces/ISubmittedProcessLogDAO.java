package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.SubmittedProcessLogDAOException;
import au.com.billingbuddy.vo.objects.SubmittedProcessLogVO;

public interface ISubmittedProcessLogDAO {
	public int insert(SubmittedProcessLogVO submittedProcessLogVO) throws SubmittedProcessLogDAOException;
	public int update(SubmittedProcessLogVO submittedProcessLogVO) throws SubmittedProcessLogDAOException;
	public ArrayList<SubmittedProcessLogVO> search() throws SubmittedProcessLogDAOException;
	public ArrayList<SubmittedProcessLogVO> search(SubmittedProcessLogVO submittedProcessLogVO) throws SubmittedProcessLogDAOException;
	public SubmittedProcessLogVO searchByID(SubmittedProcessLogVO submittedProcessLogVO) throws SubmittedProcessLogDAOException;
	
}
