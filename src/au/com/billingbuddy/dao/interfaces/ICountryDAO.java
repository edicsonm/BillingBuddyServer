package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.CountryDAOException;
import au.com.billingbuddy.vo.objects.CountryVO;

public interface ICountryDAO {
	public int insert(CountryVO countryVO) throws CountryDAOException;
	public int update(CountryVO countryVO) throws CountryDAOException;
	public int delete(CountryVO countryVO) throws CountryDAOException;
	public CountryVO searchByID(String ID) throws CountryDAOException;
	public ArrayList<CountryVO> search() throws CountryDAOException;
}
