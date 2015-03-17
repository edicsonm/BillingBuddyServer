package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.CurrencyDAOException;
import au.com.billingbuddy.vo.objects.CurrencyVO;

public interface ICurrencyDAO {
	public CurrencyVO searchByID(CurrencyVO currencyVO) throws CurrencyDAOException;
	public ArrayList<CurrencyVO> search() throws CurrencyDAOException;
}
