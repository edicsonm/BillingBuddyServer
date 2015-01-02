package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.ChargeDAOException;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.VO;

public interface IChargeDAO {
	public int insert(ChargeVO chargeVO) throws ChargeDAOException;
	public ChargeVO searchBin(ChargeVO chargeVO) throws ChargeDAOException;
	public int update() throws ChargeDAOException;
	public int delete() throws ChargeDAOException;
	public VO searchByID(String ID) throws ChargeDAOException;
	public ArrayList<ChargeVO> search() throws ChargeDAOException;
	public ArrayList<ChargeVO> search(ChargeVO chargeVO) throws ChargeDAOException;
}
