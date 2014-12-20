package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.StripeChargeDAOException;
import au.com.billingbuddy.vo.objects.StripeChargeVO;
import au.com.billingbuddy.vo.objects.VO;

public interface IStripeChargeDAO {
	public int insert(StripeChargeVO stripeChargeVO) throws StripeChargeDAOException;
	public StripeChargeVO searchBin(StripeChargeVO stripeChargeVO) throws StripeChargeDAOException;
	public int update() throws StripeChargeDAOException;
	public int delete() throws StripeChargeDAOException;
	public VO searchByID(String ID) throws StripeChargeDAOException;
	public ArrayList<StripeChargeVO> search() throws StripeChargeDAOException;
}
