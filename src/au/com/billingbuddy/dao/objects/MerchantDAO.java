package au.com.billingbuddy.dao.objects;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.connection.objects.MySQLConnection;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.interfaces.IMerchantDAO;
import au.com.billingbuddy.exceptions.objects.MerchantDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.BusinessTypeVO;
import au.com.billingbuddy.vo.objects.CertificateVO;
import au.com.billingbuddy.vo.objects.CountryVO;
import au.com.billingbuddy.vo.objects.IndustryVO;
import au.com.billingbuddy.vo.objects.MerchantVO;

public class MerchantDAO extends MySQLConnection implements IMerchantDAO {

	public MerchantDAO() throws MySQLConnectionException {
		super();
	}
	
	public MerchantDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(MerchantVO merchantVO) throws MerchantDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_MERCHANT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,merchantVO.getBusinessTypeId());
			cstmt.setString(2,merchantVO.getIndustryId());
			cstmt.setString(3,merchantVO.getCountryNumericMerchant());
			cstmt.setString(4,merchantVO.getCountryNumericPersonalInformation());
			cstmt.setString(5,merchantVO.getName());
			cstmt.setString(6,merchantVO.getTradingName());
			cstmt.setString(7,merchantVO.getLegalPhysicalAddress());
			cstmt.setString(8,merchantVO.getStatementAddress());
			cstmt.setString(9,merchantVO.getTaxFileNumber());
			cstmt.setString(10,merchantVO.getCityBusinessInformation());
			cstmt.setString(11,merchantVO.getPostCodeBusinessInformation());
			cstmt.setString(12,merchantVO.getIssuedBusinessID());
			cstmt.setString(13,merchantVO.getIssuedPersonalID());
			cstmt.setString(14,merchantVO.getTypeAccountApplication());
			cstmt.setString(15,merchantVO.getEstimatedAnnualSales());
			cstmt.setString(16,merchantVO.getFirstName());
			cstmt.setString(17,merchantVO.getLastName());
			cstmt.setString(18,merchantVO.getPhoneNumber());
			cstmt.setString(19,merchantVO.getFaxNumber());
			cstmt.setString(20,merchantVO.getEmailAddress());
			cstmt.setString(21,merchantVO.getAlternateEmailAddress());
			cstmt.setString(22,merchantVO.getCityPersonalInformation());
			cstmt.setString(23,merchantVO.getPostCodePersonalInformation());
			cstmt.setString(24,"0");
			status = cstmt.executeUpdate();
			merchantVO.setId(cstmt.getString(24));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}
	
	public int insertBasicInformation(MerchantVO merchantVO) throws MerchantDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_MERCHANT_BASIC(?,?,?,?,?)}");
			cstmt.setString(1,merchantVO.getName());
			cstmt.setString(2,merchantVO.getFirstName());
			cstmt.setString(3,merchantVO.getLastName());
			cstmt.setString(4,merchantVO.getEmailAddress());
			cstmt.setString(5,"0");
			status = cstmt.executeUpdate();
			merchantVO.setId(cstmt.getString(5));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int update(MerchantVO merchantVO) throws MerchantDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_UPDATE_MERCHANT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,merchantVO.getBusinessTypeId());
			cstmt.setString(2,merchantVO.getIndustryId());
			cstmt.setString(3,merchantVO.getCountryNumericMerchant());
			cstmt.setString(4,merchantVO.getCountryNumericPersonalInformation());
			cstmt.setString(5,merchantVO.getName());
			cstmt.setString(6,merchantVO.getTradingName());
			cstmt.setString(7,merchantVO.getLegalPhysicalAddress());
			cstmt.setString(8,merchantVO.getStatementAddress());
			cstmt.setString(9,merchantVO.getTaxFileNumber());
			cstmt.setString(10,merchantVO.getCityBusinessInformation());
			cstmt.setString(11,merchantVO.getPostCodeBusinessInformation());
			cstmt.setString(12,merchantVO.getIssuedBusinessID());
			cstmt.setString(13,merchantVO.getIssuedPersonalID());
			cstmt.setString(14,merchantVO.getTypeAccountApplication());
			cstmt.setString(15,merchantVO.getEstimatedAnnualSales());
			cstmt.setString(16,merchantVO.getFirstName());
			cstmt.setString(17,merchantVO.getLastName());
			cstmt.setString(18,merchantVO.getPhoneNumber());
			cstmt.setString(19,merchantVO.getFaxNumber());
			cstmt.setString(20,merchantVO.getEmailAddress());
			cstmt.setString(21,merchantVO.getAlternateEmailAddress());
			cstmt.setString(22,merchantVO.getCityPersonalInformation());
			cstmt.setString(23,merchantVO.getPostCodePersonalInformation());
			cstmt.setString(24,merchantVO.getAverageTicketSize());
			cstmt.setString(25,merchantVO.getMonthlyProcessingVolume());
			cstmt.setString(26,merchantVO.getFirstQuestion());
			cstmt.setString(27,merchantVO.getSecondQuestion());
			cstmt.setString(28,merchantVO.getThirdQuestion());
			cstmt.setString(29,merchantVO.getId());
			status = cstmt.executeUpdate();
			merchantVO.setId(cstmt.getString(29));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int delete(MerchantVO merchantVO) throws MerchantDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_DELETE_MERCHANT(?)}");
			cstmt.setString(1,merchantVO.getId());
			status = cstmt.executeUpdate();
			merchantVO.setId(cstmt.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}
	
	public int changeStatusMerchant(MerchantVO merchantVO) throws MerchantDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_CHANGE_STATUS_MERCHANT(? , ?)}");
			cstmt.setString(1,merchantVO.getStatus());
			cstmt.setString(2,merchantVO.getId());
			status = cstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public MerchantVO searchDetail(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_DETAIL( ? )}");
			pstmt.setString(1,merchantVO.getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			merchantVO = null;
			if (resultSet != null) {
				while (resultSet.next()) {
					merchantVO = new MerchantVO();
					merchantVO.setId(resultSet.getString("Merc_ID"));
					merchantVO.setCountryNumericMerchant(resultSet.getString("Coun_NumericMerchant"));
					merchantVO.setCountryVOBusiness(new CountryVO());
					merchantVO.getCountryVOBusiness().setName(resultSet.getString("Coun_Name"));
					merchantVO.setName(resultSet.getString("Merc_Name"));
					
					merchantVO.setFirstName(resultSet.getString("Merc_FirstName"));
					merchantVO.setLastName(resultSet.getString("Merc_LastName"));
					merchantVO.setEmailAddress(resultSet.getString("Merc_EmailAddress"));
					
					merchantVO.setCertificateVO(new CertificateVO());
					merchantVO.getCertificateVO().setMerchantId(resultSet.getString("Merc_ID"));
					merchantVO.getCertificateVO().setId(resultSet.getString("Cert_ID"));
					merchantVO.getCertificateVO().setPasswordBBKeyStore(resultSet.getString("Cert_PasswordBBKeyStore"));
					merchantVO.getCertificateVO().setPasswordBBKey(resultSet.getString("Cert_PasswordBBKey"));
					merchantVO.getCertificateVO().setAliasBB(resultSet.getString("Cert_AliasBB"));
					merchantVO.getCertificateVO().setAliasMerchant(resultSet.getString("Cert_AliasMerchant"));
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return merchantVO;
	}
	
	public MerchantVO searchDetailUpdateProfile(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_DETAILS_UPDATE_PROFILE( ? )}");
			pstmt.setString(1,merchantVO.getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					merchantVO = new MerchantVO();
					merchantVO.setId(resultSet.getString("Merc_ID"));
					merchantVO.setBusinessTypeId(resultSet.getString("Buty_ID"));
					merchantVO.setIndustryId(resultSet.getString("Indu_ID"));
					merchantVO.setCountryNumericMerchant(resultSet.getString("Coun_NumericMerchant"));
					merchantVO.setCountryNumericPersonalInformation(resultSet.getString("Coun_NumericPersonalInformation"));
					merchantVO.setName(resultSet.getString("Merc_Name"));
					merchantVO.setTradingName(resultSet.getString("Merc_TradingName"));
					merchantVO.setLegalPhysicalAddress(resultSet.getString("Merc_LegalPhysicalAddress"));
					merchantVO.setStatementAddress(resultSet.getString("Merc_StatementAddress"));
					merchantVO.setTaxFileNumber(resultSet.getString("Merc_TaxFileNumber"));
					merchantVO.setCityBusinessInformation(resultSet.getString("Merc_CityBusinessInformation"));
					merchantVO.setPostCodeBusinessInformation(resultSet.getString("Merc_PostCodeBusinessInformation"));
					merchantVO.setIssuedBusinessID(resultSet.getString("Merc_IssuedBusinessID"));
					merchantVO.setIssuedPersonalID(resultSet.getString("Merc_IssuedPersonalID"));
					merchantVO.setTypeAccountApplication(resultSet.getString("Merc_TypeAccountApplication"));
					merchantVO.setEstimatedAnnualSales(resultSet.getString("Merc_EstimatedAnnualSales"));
					
					merchantVO.setFirstName(resultSet.getString("Merc_FirstName"));
					merchantVO.setLastName(resultSet.getString("Merc_LastName"));
					merchantVO.setEmailAddress(resultSet.getString("Merc_EmailAddress"));					
					
					merchantVO.setPhoneNumber(resultSet.getString("Merc_PhoneNumber"));
					merchantVO.setFaxNumber(resultSet.getString("Merc_FaxNumber"));
					merchantVO.setAlternateEmailAddress(resultSet.getString("Merc_AlternateEmailAddress"));
					merchantVO.setCityPersonalInformation(resultSet.getString("Merc_CityPersonalInformation"));
					merchantVO.setPostCodePersonalInformation(resultSet.getString("Merc_PostCodePersonalInformation"));
					
					merchantVO.setAverageTicketSize(resultSet.getString("Merc_AverageTicketSize"));
					merchantVO.setMonthlyProcessingVolume(resultSet.getString("Merc_MonthlyProcessingVolume"));
					merchantVO.setFirstQuestion(resultSet.getString("Merc_FirstQuestion"));
					merchantVO.setSecondQuestion(resultSet.getString("Merc_SecondQuestion"));
					merchantVO.setThirdQuestion(resultSet.getString("Merc_ThirdQuestion"));
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return merchantVO;
	}

	public ArrayList<MerchantVO> search(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<MerchantVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT( ? )}");
			pstmt.setString(1,merchantVO.getUserId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<MerchantVO>();
				while (resultSet.next()) {
					MerchantVO merchantVOAUX = new MerchantVO();
					merchantVOAUX.setId(resultSet.getString("Merc_ID"));
					
					merchantVOAUX.setBusinessTypeId(resultSet.getString("Buty_ID"));
					merchantVOAUX.setBusinessTypeVO(new BusinessTypeVO());
					merchantVOAUX.getBusinessTypeVO().setDescription(resultSet.getString("Buty_Description"));
					
					merchantVOAUX.setIndustryId(resultSet.getString("Indu_ID"));
					merchantVOAUX.setIndustryVO(new IndustryVO());
					merchantVOAUX.getIndustryVO().setDescription(resultSet.getString("Indu_Description"));
					
					merchantVOAUX.setCountryNumericMerchant(resultSet.getString("Coun_NumericMerchant"));
					merchantVOAUX.setCountryNumericPersonalInformation(resultSet.getString("Coun_NumericPersonalInformation"));
					
					merchantVOAUX.setCountryVOBusiness(new CountryVO());
					merchantVOAUX.getCountryVOBusiness().setName(resultSet.getString("CountryBusiness"));
					
					merchantVOAUX.setCountryVOPersonalInformation(new CountryVO());
					merchantVOAUX.getCountryVOPersonalInformation().setName(resultSet.getString("CountryPersonalInformation"));
					
					merchantVOAUX.setName(resultSet.getString("Merc_Name"));
					merchantVOAUX.setStatus(resultSet.getString("Merc_Status"));
					merchantVOAUX.setCreateTime(resultSet.getString("Merc_CreateTime"));
					merchantVOAUX.setTradingName(resultSet.getString("Merc_TradingName"));
					merchantVOAUX.setLegalPhysicalAddress(resultSet.getString("Merc_LegalPhysicalAddress"));
					merchantVOAUX.setStatementAddress(resultSet.getString("Merc_StatementAddress"));
					merchantVOAUX.setTaxFileNumber(resultSet.getString("Merc_TaxFileNumber"));
					merchantVOAUX.setCityBusinessInformation(resultSet.getString("Merc_CityBusinessInformation"));
					merchantVOAUX.setPostCodeBusinessInformation(resultSet.getString("Merc_PostCodeBusinessInformation"));
					merchantVOAUX.setIssuedBusinessID(resultSet.getString("Merc_IssuedBusinessID"));
					merchantVOAUX.setIssuedPersonalID(resultSet.getString("Merc_IssuedPersonalID"));
					merchantVOAUX.setTypeAccountApplication(resultSet.getString("Merc_TypeAccountApplication"));
					merchantVOAUX.setEstimatedAnnualSales(resultSet.getString("Merc_EstimatedAnnualSales"));
					merchantVOAUX.setFirstName(resultSet.getString("Merc_FirstName"));
					merchantVOAUX.setLastName(resultSet.getString("Merc_LastName"));
					merchantVOAUX.setPhoneNumber(resultSet.getString("Merc_PhoneNumber"));
					merchantVOAUX.setFaxNumber(resultSet.getString("Merc_FaxNumber"));
					merchantVOAUX.setEmailAddress(resultSet.getString("Merc_EmailAddress"));
					merchantVOAUX.setAlternateEmailAddress(resultSet.getString("Merc_AlternateEmailAddress"));
					merchantVOAUX.setCityPersonalInformation(resultSet.getString("Merc_CityPersonalInformation"));
					merchantVOAUX.setPostCodePersonalInformation(resultSet.getString("Merc_PostCodePersonalInformation"));
					list.add(merchantVOAUX);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}
	
	public ArrayList<MerchantVO> searchAll(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<MerchantVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_ALL_MERCHANT( ? )}");
			pstmt.setString(1,merchantVO.getUserId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<MerchantVO>();
				while (resultSet.next()) {
					MerchantVO merchantVOAUX = new MerchantVO();
					merchantVOAUX.setId(resultSet.getString("Merc_ID"));
					
					merchantVOAUX.setBusinessTypeId(resultSet.getString("Buty_ID"));
					merchantVOAUX.setBusinessTypeVO(new BusinessTypeVO());
					merchantVOAUX.getBusinessTypeVO().setDescription(resultSet.getString("Buty_Description"));
					
					merchantVOAUX.setIndustryId(resultSet.getString("Indu_ID"));
					merchantVOAUX.setIndustryVO(new IndustryVO());
					merchantVOAUX.getIndustryVO().setDescription(resultSet.getString("Indu_Description"));
					
					merchantVOAUX.setCountryNumericMerchant(resultSet.getString("Coun_NumericMerchant"));
					merchantVOAUX.setCountryNumericPersonalInformation(resultSet.getString("Coun_NumericPersonalInformation"));
					
					merchantVOAUX.setCountryVOBusiness(new CountryVO());
					merchantVOAUX.getCountryVOBusiness().setName(resultSet.getString("CountryBusiness"));
					
					merchantVOAUX.setCountryVOPersonalInformation(new CountryVO());
					merchantVOAUX.getCountryVOPersonalInformation().setName(resultSet.getString("CountryPersonalInformation"));
					
					merchantVOAUX.setName(resultSet.getString("Merc_Name"));
					merchantVOAUX.setStatus(resultSet.getString("Merc_Status"));
					merchantVOAUX.setCreateTime(resultSet.getString("Merc_CreateTime"));
					merchantVOAUX.setTradingName(resultSet.getString("Merc_TradingName"));
					merchantVOAUX.setLegalPhysicalAddress(resultSet.getString("Merc_LegalPhysicalAddress"));
					merchantVOAUX.setStatementAddress(resultSet.getString("Merc_StatementAddress"));
					merchantVOAUX.setTaxFileNumber(resultSet.getString("Merc_TaxFileNumber"));
					merchantVOAUX.setCityBusinessInformation(resultSet.getString("Merc_CityBusinessInformation"));
					merchantVOAUX.setPostCodeBusinessInformation(resultSet.getString("Merc_PostCodeBusinessInformation"));
					merchantVOAUX.setIssuedBusinessID(resultSet.getString("Merc_IssuedBusinessID"));
					merchantVOAUX.setIssuedPersonalID(resultSet.getString("Merc_IssuedPersonalID"));
					merchantVOAUX.setTypeAccountApplication(resultSet.getString("Merc_TypeAccountApplication"));
					merchantVOAUX.setEstimatedAnnualSales(resultSet.getString("Merc_EstimatedAnnualSales"));
					merchantVOAUX.setFirstName(resultSet.getString("Merc_FirstName"));
					merchantVOAUX.setLastName(resultSet.getString("Merc_LastName"));
					merchantVOAUX.setPhoneNumber(resultSet.getString("Merc_PhoneNumber"));
					merchantVOAUX.setFaxNumber(resultSet.getString("Merc_FaxNumber"));
					merchantVOAUX.setEmailAddress(resultSet.getString("Merc_EmailAddress"));
					merchantVOAUX.setAlternateEmailAddress(resultSet.getString("Merc_AlternateEmailAddress"));
					merchantVOAUX.setCityPersonalInformation(resultSet.getString("Merc_CityPersonalInformation"));
					merchantVOAUX.setPostCodePersonalInformation(resultSet.getString("Merc_PostCodePersonalInformation"));
					list.add(merchantVOAUX);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}
	
	public ArrayList<MerchantVO> searchAllFilter(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<MerchantVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_ALL_MERCHANT_FILTER( ?, ?, ?, ?, ?, ?, ? )}");
			pstmt.setString(1,merchantVO.getName());
			pstmt.setString(2,merchantVO.getCountryNumericMerchant());
			pstmt.setString(3,merchantVO.getBusinessTypeId());
			pstmt.setString(4,merchantVO.getIndustryId());
			pstmt.setString(5,merchantVO.getStatus());
			pstmt.setString(6,merchantVO.getMatch());
			pstmt.setString(7,merchantVO.getUserId());
			
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<MerchantVO>();
				while (resultSet.next()) {
					MerchantVO merchantVOAUX = new MerchantVO();
					merchantVOAUX.setId(resultSet.getString("Merc_ID"));
					
					merchantVOAUX.setBusinessTypeId(resultSet.getString("Buty_ID"));
					merchantVOAUX.setBusinessTypeVO(new BusinessTypeVO());
					merchantVOAUX.getBusinessTypeVO().setDescription(resultSet.getString("Buty_Description"));
					
					merchantVOAUX.setIndustryId(resultSet.getString("Indu_ID"));
					merchantVOAUX.setIndustryVO(new IndustryVO());
					merchantVOAUX.getIndustryVO().setDescription(resultSet.getString("Indu_Description"));
					
					merchantVOAUX.setCountryNumericMerchant(resultSet.getString("Coun_NumericMerchant"));
					merchantVOAUX.setCountryNumericPersonalInformation(resultSet.getString("Coun_NumericPersonalInformation"));
					
					merchantVOAUX.setCountryVOBusiness(new CountryVO());
					merchantVOAUX.getCountryVOBusiness().setName(resultSet.getString("CountryBusiness"));
					
					merchantVOAUX.setCountryVOPersonalInformation(new CountryVO());
					merchantVOAUX.getCountryVOPersonalInformation().setName(resultSet.getString("CountryPersonalInformation"));
					
					merchantVOAUX.setName(resultSet.getString("Merc_Name"));
					merchantVOAUX.setStatus(resultSet.getString("Merc_Status"));
					merchantVOAUX.setCreateTime(resultSet.getString("Merc_CreateTime"));
					merchantVOAUX.setTradingName(resultSet.getString("Merc_TradingName"));
					merchantVOAUX.setLegalPhysicalAddress(resultSet.getString("Merc_LegalPhysicalAddress"));
					merchantVOAUX.setStatementAddress(resultSet.getString("Merc_StatementAddress"));
					merchantVOAUX.setTaxFileNumber(resultSet.getString("Merc_TaxFileNumber"));
					merchantVOAUX.setCityBusinessInformation(resultSet.getString("Merc_CityBusinessInformation"));
					merchantVOAUX.setPostCodeBusinessInformation(resultSet.getString("Merc_PostCodeBusinessInformation"));
					merchantVOAUX.setIssuedBusinessID(resultSet.getString("Merc_IssuedBusinessID"));
					merchantVOAUX.setIssuedPersonalID(resultSet.getString("Merc_IssuedPersonalID"));
					merchantVOAUX.setTypeAccountApplication(resultSet.getString("Merc_TypeAccountApplication"));
					merchantVOAUX.setEstimatedAnnualSales(resultSet.getString("Merc_EstimatedAnnualSales"));
					merchantVOAUX.setFirstName(resultSet.getString("Merc_FirstName"));
					merchantVOAUX.setLastName(resultSet.getString("Merc_LastName"));
					merchantVOAUX.setPhoneNumber(resultSet.getString("Merc_PhoneNumber"));
					merchantVOAUX.setFaxNumber(resultSet.getString("Merc_FaxNumber"));
					merchantVOAUX.setEmailAddress(resultSet.getString("Merc_EmailAddress"));
					merchantVOAUX.setAlternateEmailAddress(resultSet.getString("Merc_AlternateEmailAddress"));
					merchantVOAUX.setCityPersonalInformation(resultSet.getString("Merc_CityPersonalInformation"));
					merchantVOAUX.setPostCodePersonalInformation(resultSet.getString("Merc_PostCodePersonalInformation"));
					list.add(merchantVOAUX);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		System.out.println("list.size(): " + list.size());
		return list;
	}

//	public ArrayList<MerchantVO> search() throws MerchantDAOException {
//		Connection connection = this.connection;
//		ResultSet resultSet = null; 
//		PreparedStatement pstmt = null;
//		ArrayList<MerchantVO> list = null;
//		try {
//			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT()}");
//			resultSet = (ResultSet)pstmt.executeQuery();
//			if (resultSet != null) {
//				list = new ArrayList<MerchantVO>();
//				while (resultSet.next()) {
//					MerchantVO merchantVO = new MerchantVO();
//					merchantVO.setId(resultSet.getString("Merc_ID"));
//					merchantVO.setCountryNumericMerchant(resultSet.getString("Coun_NumericMerchant"));
//					merchantVO.setCountryVOBusiness(new CountryVO());
//					merchantVO.getCountryVOBusiness().setName(resultSet.getString("Coun_Name"));
//					merchantVO.setName(resultSet.getString("Merc_Name"));
//					list.add(merchantVO);
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			throw new MerchantDAOException(e);
//		} finally {
//			PsRs(pstmt, resultSet,connection);
//		}
//		return list;
//	}

	public MerchantVO verifyRestrictionByAmount(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_VERIFY_MERCHANT_RESTRICTION_BY_AMOUNT(?, ?)}");
			pstmt.setString(1,merchantVO.getId());
			pstmt.setString(2,merchantVO.getTimeUnit());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					merchantVO.setAmountTransactions(resultSet.getString("Amount_Transactions"));
					merchantVO.setSince(resultSet.getString("Since"));
					merchantVO.setTo(resultSet.getString("To"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return merchantVO;
	}

	public MerchantVO verifyRestrictionByTransactions(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_VERIFY_MERCHANT_RESTRICTION_BY_TRANSACTIONS(?, ?)}");
			pstmt.setString(1,merchantVO.getId());
			pstmt.setString(2,merchantVO.getTimeUnit());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					merchantVO.setNumberTransactions(resultSet.getString("Number_Transactions"));
					merchantVO.setSince(resultSet.getString("Since"));
					merchantVO.setTo(resultSet.getString("To"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return merchantVO;
	}

	public ArrayList<MerchantVO> searchMerchantsToConfigure(MerchantVO merchantVO) throws MerchantDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<MerchantVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_TO_CONFIGURE( ? )}");
			pstmt.setString(1,merchantVO.getUserId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<MerchantVO>();
				while (resultSet.next()) {
					MerchantVO merchantVOAUX = new MerchantVO();
					merchantVOAUX.setId(resultSet.getString("Merc_ID"));
					
					merchantVOAUX.setBusinessTypeId(resultSet.getString("Buty_ID"));
					merchantVOAUX.setBusinessTypeVO(new BusinessTypeVO());
					merchantVOAUX.getBusinessTypeVO().setDescription(resultSet.getString("Buty_Description"));
					
					merchantVOAUX.setIndustryId(resultSet.getString("Indu_ID"));
					merchantVOAUX.setIndustryVO(new IndustryVO());
					merchantVOAUX.getIndustryVO().setDescription(resultSet.getString("Indu_Description"));
					
					merchantVOAUX.setCountryNumericMerchant(resultSet.getString("Coun_NumericMerchant"));
					merchantVOAUX.setCountryNumericPersonalInformation(resultSet.getString("Coun_NumericPersonalInformation"));
					
					merchantVOAUX.setCountryVOBusiness(new CountryVO());
					merchantVOAUX.getCountryVOBusiness().setName(resultSet.getString("CountryBusiness"));
					
					merchantVOAUX.setCountryVOPersonalInformation(new CountryVO());
					merchantVOAUX.getCountryVOPersonalInformation().setName(resultSet.getString("CountryPersonalInformation"));
					
					merchantVOAUX.setName(resultSet.getString("Merc_Name"));
					merchantVOAUX.setStatus(resultSet.getString("Merc_Status"));
					merchantVOAUX.setCreateTime(resultSet.getString("Merc_CreateTime"));
					merchantVOAUX.setTradingName(resultSet.getString("Merc_TradingName"));
					merchantVOAUX.setLegalPhysicalAddress(resultSet.getString("Merc_LegalPhysicalAddress"));
					merchantVOAUX.setStatementAddress(resultSet.getString("Merc_StatementAddress"));
					merchantVOAUX.setTaxFileNumber(resultSet.getString("Merc_TaxFileNumber"));
					merchantVOAUX.setCityBusinessInformation(resultSet.getString("Merc_CityBusinessInformation"));
					merchantVOAUX.setPostCodeBusinessInformation(resultSet.getString("Merc_PostCodeBusinessInformation"));
					merchantVOAUX.setIssuedBusinessID(resultSet.getString("Merc_IssuedBusinessID"));
					merchantVOAUX.setIssuedPersonalID(resultSet.getString("Merc_IssuedPersonalID"));
					merchantVOAUX.setTypeAccountApplication(resultSet.getString("Merc_TypeAccountApplication"));
					merchantVOAUX.setEstimatedAnnualSales(resultSet.getString("Merc_EstimatedAnnualSales"));
					merchantVOAUX.setFirstName(resultSet.getString("Merc_FirstName"));
					merchantVOAUX.setLastName(resultSet.getString("Merc_LastName"));
					merchantVOAUX.setPhoneNumber(resultSet.getString("Merc_PhoneNumber"));
					merchantVOAUX.setFaxNumber(resultSet.getString("Merc_FaxNumber"));
					merchantVOAUX.setEmailAddress(resultSet.getString("Merc_EmailAddress"));
					merchantVOAUX.setAlternateEmailAddress(resultSet.getString("Merc_AlternateEmailAddress"));
					merchantVOAUX.setCityPersonalInformation(resultSet.getString("Merc_CityPersonalInformation"));
					merchantVOAUX.setPostCodePersonalInformation(resultSet.getString("Merc_PostCodePersonalInformation"));
					list.add(merchantVOAUX);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}


}
