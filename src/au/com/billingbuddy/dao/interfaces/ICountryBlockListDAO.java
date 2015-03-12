package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.CountryBlockListDAOException;
import au.com.billingbuddy.vo.objects.CountryBlockListVO;

public interface ICountryBlockListDAO {
	public int insert(CountryBlockListVO countryBlockListVO) throws CountryBlockListDAOException;
	public int update(CountryBlockListVO countryBlockListVO) throws CountryBlockListDAOException;
	public CountryBlockListVO searchByID(String ID) throws CountryBlockListDAOException;
	public ArrayList<CountryBlockListVO> search() throws CountryBlockListDAOException;
}
