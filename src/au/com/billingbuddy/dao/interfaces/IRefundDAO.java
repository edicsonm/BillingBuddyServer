package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.RefundDAOException;
import au.com.billingbuddy.vo.objects.RefundVO;
import au.com.billingbuddy.vo.objects.VO;

public interface IRefundDAO {
	public int insert(RefundVO refundVO) throws RefundDAOException;
	public int update() throws RefundDAOException;
	public int delete() throws RefundDAOException;
	public VO searchByID(String ID) throws RefundDAOException;
	public ArrayList<RefundVO> search() throws RefundDAOException;
	public ArrayList<RefundVO> search(RefundVO refundVO) throws RefundDAOException;
}
