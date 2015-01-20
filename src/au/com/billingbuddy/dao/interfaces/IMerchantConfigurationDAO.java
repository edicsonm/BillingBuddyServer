package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import au.com.billingbuddy.exceptions.objects.MerchantConfigurationDAOException;
import au.com.billingbuddy.exceptions.objects.MerchantDAOException;
import au.com.billingbuddy.vo.objects.MerchantConfigurationVO;

public interface IMerchantConfigurationDAO {
	public int insert(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException, MySQLIntegrityConstraintViolationException;
	public int update(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException;
	public int delete(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException;
	public MerchantConfigurationVO searchDetail(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException;
	public ArrayList<MerchantConfigurationVO> search() throws MerchantConfigurationDAOException;
	public ArrayList<MerchantConfigurationVO> search(MerchantConfigurationVO merchantConfigurationVO) throws MerchantConfigurationDAOException;
}
