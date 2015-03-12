package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.CreditCardRestrictionDAOException;
import au.com.billingbuddy.vo.objects.CreditCardRestrictionVO;

public interface ICreditCardRestrictionDAO {
	public int insert(CreditCardRestrictionVO creditCardRestrictionVO) throws CreditCardRestrictionDAOException;
	public int update(CreditCardRestrictionVO creditCardRestrictionVO) throws CreditCardRestrictionDAOException;
	public int delete(CreditCardRestrictionVO creditCardRestrictionVO) throws CreditCardRestrictionDAOException;
	public CreditCardRestrictionVO searchByID(String ID) throws CreditCardRestrictionDAOException;
	public ArrayList<CreditCardRestrictionVO> search() throws CreditCardRestrictionDAOException;
}
