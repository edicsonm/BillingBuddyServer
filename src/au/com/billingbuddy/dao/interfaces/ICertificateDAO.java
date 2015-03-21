package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;
import au.com.billingbuddy.exceptions.objects.CertificateDAOException;
import au.com.billingbuddy.vo.objects.CertificateVO;

public interface ICertificateDAO {
	public int insert(CertificateVO certificateVO) throws CertificateDAOException;
	public int updateStatus(CertificateVO certificateVO) throws CertificateDAOException;
	public CertificateVO searchDetailMerchant(CertificateVO certificateVO) throws CertificateDAOException;
	public CertificateVO searchDetailBB(CertificateVO certificateVO) throws CertificateDAOException;
	public ArrayList<CertificateVO> search(CertificateVO certificateVO) throws CertificateDAOException;
}
