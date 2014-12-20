package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.vo.objects.TransactionVO;
import au.com.billingbuddy.vo.objects.VO;

public interface ITransactionDAO {
	public int insert(TransactionVO transactionVO) throws TransactionDAOException;
	public int update(TransactionVO transactionVO) throws TransactionDAOException;
	public int delete() throws TransactionDAOException;
	public VO searchByID(String ID) throws TransactionDAOException;
	public ArrayList<TransactionVO> search() throws TransactionDAOException;
	public ArrayList<TransactionVO> search(TransactionVO transactionVO) throws TransactionDAOException;
}
