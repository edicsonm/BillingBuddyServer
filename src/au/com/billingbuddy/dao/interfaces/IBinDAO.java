package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.BinDAOException;
import au.com.billingbuddy.vo.objects.BinVO;
import au.com.billingbuddy.vo.objects.VO;

public interface IBinDAO {
	public int insert(BinVO binVO) throws BinDAOException;
	public BinVO searchBin(BinVO binVO) throws BinDAOException;
	public int update() throws BinDAOException;
	public int delete() throws BinDAOException;
	public VO searchByID(String ID) throws BinDAOException;
	public ArrayList<BinVO> search() throws BinDAOException;
}
