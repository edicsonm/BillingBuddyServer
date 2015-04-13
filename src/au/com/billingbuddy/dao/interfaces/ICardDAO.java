package au.com.billingbuddy.dao.interfaces;

import java.util.ArrayList;

import au.com.billingbuddy.exceptions.objects.CardDAOException;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.VO;

public interface ICardDAO {
	public int insert(CardVO CardVO) throws CardDAOException;
	public CardVO searchCard(CardVO CardVO) throws CardDAOException;
	public CardVO searchCardByNumber(CardVO CardVO) throws CardDAOException;
	public int update() throws CardDAOException;
	public int delete() throws CardDAOException;
	public VO searchByID(String ID) throws CardDAOException;
	public ArrayList<CardVO> search() throws CardDAOException;
}
