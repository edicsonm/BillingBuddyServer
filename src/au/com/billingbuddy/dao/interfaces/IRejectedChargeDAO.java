package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.RejectedChargeDAOException;
import au.com.billingbuddy.vo.objects.RejectedChargeVO;
import au.com.billingbuddy.vo.objects.VO;

public interface IRejectedChargeDAO {
	public int insert(RejectedChargeVO rejectedChargesVO) throws RejectedChargeDAOException;
	public VO searchByID(String ID) throws RejectedChargeDAOException;
	public ArrayList<RejectedChargeVO> search() throws RejectedChargeDAOException;
	public ArrayList<RejectedChargeVO> search(RejectedChargeVO rejectedChargesVO) throws RejectedChargeDAOException;
}
