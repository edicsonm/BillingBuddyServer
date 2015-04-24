package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.MerchantDocumentDAOException;
import au.com.billingbuddy.vo.objects.MerchantDocumentVO;

public interface IMerchantDocumentDAO {
	public int insert(MerchantDocumentVO merchantDocumentVO) throws MerchantDocumentDAOException;
	public int delete(MerchantDocumentVO merchantDocumentVO) throws MerchantDocumentDAOException;
	public MerchantDocumentVO searchDocument(MerchantDocumentVO merchantDocumentVO) throws MerchantDocumentDAOException;
	public ArrayList<MerchantDocumentVO> searchDocuments(MerchantDocumentVO merchantDocumentVO) throws MerchantDocumentDAOException;

//	public CardVO searchCardByNumber(MerchantDocumentVO merchantDocumentVO) throws MerchantDocumentDAOException;
//	public int update(MerchantDocumentVO merchantDocumentVO) throws CardDAOException;
//	public VO searchByID(String ID) throws CardDAOException;
//	public ArrayList<CardVO> search() throws CardDAOException;
	
}
