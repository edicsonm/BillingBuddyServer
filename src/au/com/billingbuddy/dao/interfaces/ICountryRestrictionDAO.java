package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.CountryRestrictionDAOException;
import au.com.billingbuddy.vo.objects.CountryRestrictionVO;

public interface ICountryRestrictionDAO {
	public int insert(CountryRestrictionVO countryRestrictionVO) throws CountryRestrictionDAOException;
	public int update(CountryRestrictionVO countryRestrictionVO) throws CountryRestrictionDAOException;
	public int delete(CountryRestrictionVO countryRestrictionVO) throws CountryRestrictionDAOException;
	public CountryRestrictionVO searchByID(String ID) throws CountryRestrictionDAOException;
	public ArrayList<CountryRestrictionVO> search() throws CountryRestrictionDAOException;
}
