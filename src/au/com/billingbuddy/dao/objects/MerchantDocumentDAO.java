package au.com.billingbuddy.dao.objects;

import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.connection.objects.MySQLConnection;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.interfaces.IMerchantDocumentDAO;
import au.com.billingbuddy.exceptions.objects.MerchantDocumentDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.vo.objects.MerchantDocumentVO;

public class MerchantDocumentDAO extends MySQLConnection implements IMerchantDocumentDAO {

	public MerchantDocumentDAO() throws MySQLConnectionException{
		super();
	}
	
	public MerchantDocumentDAO(MySQLTransaction mySQLTransaction) throws MySQLConnectionException {
		super(mySQLTransaction);
	}
	
	public int insert(MerchantDocumentVO merchantDocumentVO) throws MerchantDocumentDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SAVE_MERCHANT_DOCUMENT( ? , ? , ? , ? , ? )}");
			cstmt.setString(1, merchantDocumentVO.getMerchantId());
			cstmt.setString(2, merchantDocumentVO.getName());
			cstmt.setString(3, merchantDocumentVO.getDescription());
			cstmt.setString(4, merchantDocumentVO.getSize());
			cstmt.setBlob(5, merchantDocumentVO.getFile());
			status = cstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDocumentDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public MerchantDocumentVO searchDocument(MerchantDocumentVO merchantDocumentVO) throws MerchantDocumentDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_DOCUMENT( ? )}");
			pstmt.setString(1, merchantDocumentVO.getId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					merchantDocumentVO.setFile(resultSet.getBlob("Medo_File"));
					merchantDocumentVO.setId(resultSet.getString("Medo_ID"));
					merchantDocumentVO.setName(resultSet.getString("Medo_Name"));
					merchantDocumentVO.setDescription(resultSet.getString("Medo_Description"));
					merchantDocumentVO.setSize(resultSet.getString("Medo_Size"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDocumentDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return merchantDocumentVO;
	}

	public int delete(MerchantDocumentVO merchantDocumentVO) throws MerchantDocumentDAOException {
		CallableStatement cstmt = null;
		int status = 0;
		try {
			cstmt = getConnection().prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_DELETE_MERCHANT_DOCUMENT( ? )}");
			cstmt.setString(1, merchantDocumentVO.getId());
			status = cstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerchantDocumentDAOException(e);
		} finally {
			Cs(cstmt, getConnection());
		}
		return status;
	}

	public ArrayList<MerchantDocumentVO> searchDocuments(MerchantDocumentVO merchantDocumentVO) throws MerchantDocumentDAOException {
		Connection connection = this.connection;
		ResultSet resultSet = null; 
		PreparedStatement pstmt = null;
		ArrayList<MerchantDocumentVO> list = null;
		try {
			pstmt = connection.prepareCall("{call "+ConfigurationSystem.getKey("schema")+".PROC_SEARCH_MERCHANT_DOCUMENTS( ? )}");
			pstmt.setString(1, merchantDocumentVO.getMerchantId());
			resultSet = (ResultSet)pstmt.executeQuery();
			if (resultSet != null) {
				list = new ArrayList<MerchantDocumentVO>();
				while (resultSet.next()) {
					merchantDocumentVO = new MerchantDocumentVO();
					merchantDocumentVO.setId(resultSet.getString("Medo_ID"));
					merchantDocumentVO.setName(resultSet.getString("Medo_Name"));
					merchantDocumentVO.setDescription(resultSet.getString("Medo_Description"));
					merchantDocumentVO.setSize(resultSet.getString("Medo_Size"));
					list.add(merchantDocumentVO);
				}
			}
		} catch (SQLException e) {
			throw new MerchantDocumentDAOException(e);
		} finally {
			PsRs(pstmt, resultSet,connection);
		}
		return list;
	}


}
