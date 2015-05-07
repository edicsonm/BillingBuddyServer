package au.com.billingbuddy.common.objects;

import java.util.HashMap;

import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

import com.stripe.model.Charge;

public class TransactionManager {
	
	private static TransactionManager instanceTransactionManager = null;
	private HashMap<String, Thread> threads = new HashMap<String, Thread>();
	
	public static synchronized TransactionManager getInstance() {
		if (instanceTransactionManager == null) {
			instanceTransactionManager = new TransactionManager();
		}
		return instanceTransactionManager;
	}
	
	private TransactionManager(){}
	
	public void saveInformationTransaction(String name, TransactionVO transactionVO, Charge charge){
		SaveInformationTransactionThread SaveInformationTransactionThread = new SaveInformationTransactionThread(name, transactionVO, charge);
		threads.put(name, SaveInformationTransactionThread);
		SaveInformationTransactionThread.start();
	}
	
	public static void main(String[] args) {

	}

}
