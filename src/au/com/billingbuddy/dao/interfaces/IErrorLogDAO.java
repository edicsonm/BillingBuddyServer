package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.ErrorLogDAOException;
import au.com.billingbuddy.vo.objects.ErrorLogVO;

public interface IErrorLogDAO {
	public int insert(ErrorLogVO errorLogVO) throws ErrorLogDAOException;
	public int update() throws ErrorLogDAOException;
	public int delete() throws ErrorLogDAOException;
	public ArrayList<ErrorLogVO> search() throws ErrorLogDAOException;
	public ArrayList<ErrorLogVO> search(ErrorLogVO errorLogVO) throws ErrorLogDAOException;
}
