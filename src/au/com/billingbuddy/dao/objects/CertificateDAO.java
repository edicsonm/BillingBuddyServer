package au.com.billingbuddy.dao.objects;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.MySQLError;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.connection.objects.MySQLConnection;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.interfaces.ICertificateDAO;
import au.com.billingbuddy.exceptions.objects.CertificateDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.CertificateVO;
import au.com.billingbuddy.vo.objects.MerchantVO;

public class CertificateDAO extends MySQLConnection implements ICertificateDAO {

	public CertificateDAO() throws MySQLConnectionException{
		super();
	}
	
	public CertificateDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}

	public int insert(CertificateVO certificateVO) throws CertificateDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			
			FileInputStream fileInputStreamKeyStoreBB = new FileInputStream(certificateVO.getFileKeyStoreBB());
			FileInputStream fileInputStreamKeyStoreMerchant = new FileInputStream(certificateVO.getFileKeyStoreMerchant());
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_CERTIFICATE(?,?,?,?,?,?,?,?)}");
			cstmt.setString(1,certificateVO.getMerchantId());
			cstmt.setString(2,certificateVO.getCommonName());
//			cstmt.setBlob(3,certificateVO.getBBKeyStore());
//			cstmt.setBlob(4,certificateVO.getMerchantKeyStore());
			cstmt.setBinaryStream(3,fileInputStreamKeyStoreBB,(int)certificateVO.getFileKeyStoreBB().length());
			cstmt.setBinaryStream(4,fileInputStreamKeyStoreMerchant,(int)certificateVO.getFileKeyStoreMerchant().length());
			cstmt.setString(5,certificateVO.getLog().toString());
			cstmt.setString(6,certificateVO.getInfoCertificateBB());
			cstmt.setString(7,certificateVO.getInfoCertificateMerchant());
			cstmt.setString(8,"0");
			status = cstmt.executeUpdate();
			certificateVO.setId(cstmt.getString(8));
		} catch (SQLException e) {
			throw new CertificateDAOException(e);
		} catch (FileNotFoundException e) {
			throw new CertificateDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public int updateStatus(CertificateVO certificateVO) throws CertificateDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_CHANGE_CERTIFICATE_STATUS(?,?)}");
			cstmt.setString(1,certificateVO.getStatus());
			cstmt.setString(2,certificateVO.getId());
			status = cstmt.executeUpdate();
			certificateVO.setId(cstmt.getString(2));
		} catch (SQLException e) {
			throw new CertificateDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public ArrayList<CertificateVO> search(CertificateVO CertificateVO) throws CertificateDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<CertificateVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CERTIFICATE(?)}");
			pstmt.setString(1, CertificateVO.getUserId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<CertificateVO>();
				while (resultSet.next()) {
					CertificateVO certificateVO = new CertificateVO();
					certificateVO.setId(resultSet.getString("Cert_ID"));
					certificateVO.setMerchantId(resultSet.getString("Merc_ID"));
					certificateVO.setMerchantVO(new MerchantVO());
					certificateVO.getMerchantVO().setName(resultSet.getString("Merc_Name"));					
					certificateVO.setCommonName(resultSet.getString("Cert_Name"));
					certificateVO.setStatus(resultSet.getString("Cert_Status"));
					certificateVO.setCreationTime(resultSet.getString("Cert_CreateTime"));
					certificateVO.setExpirationTime(resultSet.getString("Cert_ExpirationTime"));
					certificateVO.setLog(new StringBuffer(resultSet.getString("Cert_Log")));
					certificateVO.setInfoCertificateBB(resultSet.getString("Cert_BBKeyStoreInformation"));
					certificateVO.setInfoCertificateMerchant(resultSet.getString("Cert_MerchantKeyStoreInformation"));
					list.add(certificateVO);
				}
			}
		} catch (SQLException e) {
			throw new CertificateDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
		
	}

//	public ArrayList<CertificateVO> search(CertificateVO CertificateVO) throws CertificateDAOException {
//		return null;
//	}

	public CertificateVO searchDetailMerchant(CertificateVO certificateVO) throws CertificateDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CERTIFICATE_DETAIL_MERCHANT(?)}");
			pstmt.setString(1,certificateVO.getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				certificateVO = null;
				while (resultSet.next()) {
					certificateVO = new CertificateVO();
					certificateVO.setId(resultSet.getString("Cert_ID"));
					certificateVO.setMerchantId(resultSet.getString("Merc_ID"));
					certificateVO.setMerchantKeyStore(resultSet.getBlob("Cert_MerchantKeyStore"));
				}
			}
		} catch (SQLException e) {
			throw new CertificateDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return certificateVO;
	}
	
	public CertificateVO searchDetailBB(CertificateVO certificateVO) throws CertificateDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_CERTIFICATE_DETAIL_BB(?)}");
			pstmt.setString(1,certificateVO.getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				certificateVO = null;
				while (resultSet.next()) {
					certificateVO = new CertificateVO();
					certificateVO.setId(resultSet.getString("Cert_ID"));
					certificateVO.setMerchantId(resultSet.getString("Merc_ID"));
					certificateVO.setBBKeyStore(resultSet.getBlob("Cert_BBKeyStore"));
				}
			}
		} catch (SQLException e) {
			throw new CertificateDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return certificateVO;
	}


}
