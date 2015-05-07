package au.com.billingbuddy.common.objects;

import java.util.Calendar;
import java.util.HashMap;

import com.stripe.model.Charge;

import au.com.billigbuddy.utils.ErrorManager;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.objects.CardDAO;
import au.com.billingbuddy.dao.objects.ChargeDAO;
import au.com.billingbuddy.dao.objects.CustomerDAO;
import au.com.billingbuddy.dao.objects.MerchantCustomerDAO;
import au.com.billingbuddy.exceptions.objects.CardDAOException;
import au.com.billingbuddy.exceptions.objects.ChargeDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.MySQLTransactionException;
import au.com.billingbuddy.exceptions.objects.SaveInformationTransactionThreadException;
import au.com.billingbuddy.exceptions.objects.TransactionMDTRException;
import au.com.billingbuddy.vo.objects.CardVO;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.CustomerVO;
import au.com.billingbuddy.vo.objects.MerchantCustomerVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

public class SaveInformationTransactionThread extends Thread {
	
	private String threadName;
	private HashMap<String, Thread> threads = new HashMap<String, Thread>();
	private TransactionVO transactionVO;
	private ChargeVO chargeVO;
	private Charge charge;
	private long initialTime;
	private long finalTime;
	private boolean execute = true;
	
	public SaveInformationTransactionThread(String name, TransactionVO transactionVO, Charge charge){
		System.out.println("Creating " +  threadName );
		this.threadName = name;
		this.transactionVO = transactionVO;
		this.chargeVO = transactionVO.getChargeVO();
		this.charge = charge;
   }

	@Override
	public void run() {
		System.out.println("Running " +  threadName );
		while(execute){
			CardVO cardVOAUX = chargeVO.getCardVO();
	        CardVO cardVO = chargeVO.getCardVO();
	        MySQLTransaction mySQLTransaction = null;
			try {
				
				mySQLTransaction = new MySQLTransaction();
				mySQLTransaction.start();
				
				CardDAO cardDAO = new CardDAO(mySQLTransaction);
				cardVOAUX = cardDAO.searchCardByNumber(cardVOAUX);
				
				if(cardVOAUX == null) {//El cliente NO existe, se debe registrar todo
					System.out.println("El cliente NO existe, se debe registrar todo");
					//Validar el customer haciendo un busqueda por el email proporcionado, si no existe se registra.
					System.out.println("Entra por el if ...");
			        CustomerVO customerVO = transactionVO.getCardVO().getCustomerVO();
			        CustomerDAO customerDAO = new CustomerDAO(mySQLTransaction);
			        initialTime = Calendar.getInstance().getTimeInMillis();
			        customerDAO.insert(customerVO);
					if(customerVO != null && customerVO.getId() != null) {
						finalTime = Calendar.getInstance().getTimeInMillis();
						System.out.println("Tiempo total para copiar registrar un Customer: " + (finalTime-initialTime) + " ms. " + customerVO.getId());
						
						MerchantCustomerVO merchantCustomerVO = new MerchantCustomerVO();
			        	MerchantCustomerDAO merchantCustomerDAO = new MerchantCustomerDAO(mySQLTransaction);
			        	merchantCustomerVO.setCustomerId(customerVO.getId());
			        	merchantCustomerVO.setMerchantId(transactionVO.getMerchantId());
			        	merchantCustomerDAO.insert(merchantCustomerVO);
			        	if(merchantCustomerVO != null && merchantCustomerVO.getId() != null){
			        		
			        		System.out.println("merchantCustomerVO.getId(): " + merchantCustomerVO.getId());
				        	cardVO.setCustomerId(customerVO.getId());
				        	initialTime = Calendar.getInstance().getTimeInMillis();
							if(cardDAO.insert(cardVO) != 0){
								finalTime = Calendar.getInstance().getTimeInMillis();
								System.out.println("Tiempo total para copiar registrar una Card: " + (finalTime-initialTime) + " ms.");
								
								chargeVO.setCardId(cardVO.getId());
					        	chargeVO.setTransactionId(transactionVO.getId());
					        	ChargeDAO chargeDAO = new ChargeDAO(mySQLTransaction);
								initialTime = Calendar.getInstance().getTimeInMillis();
								if(chargeDAO.insert(chargeVO) != 0){
									finalTime = Calendar.getInstance().getTimeInMillis();
									System.out.println("Tiempo total para copiar registrar un Charge: " + (finalTime-initialTime) + " ms.");
									System.out.println("chargeVO.getId(): " + chargeVO.getId());
									mySQLTransaction.commit();
								}else{
									mySQLTransaction.rollback();
									ErrorManager.manageErrorPaymentPage(transactionVO.getId(), "ProcessorMDTR.chargePayment.ChargeDAOException.SaveCharge", charge.toString());
								}
							} else {
								mySQLTransaction.rollback();
					        	System.out.println("#################################################################");
					        	System.out.println("No fue posible registrar la tarjeta .... ");
					        	System.out.println("#################################################################");
					        	ErrorManager.manageErrorSaveInformationTransaction(transactionVO.getId(), "SaveInformationTransactionThread.saveInformationTransaction.CardDAOException.SaveCard", charge.toString());
					        }
			        	}else{
			        		System.out.println("#################################################################");
				        	System.out.println("No fue posible asociar el customer al merchant.... ");
				        	System.out.println("#################################################################");
				        	ErrorManager.manageErrorSaveInformationTransaction(transactionVO.getId(), "SaveInformationTransactionThread.saveInformationTransaction.MerchantCustomerDAOException.SaveMerchantCustomerVO", charge.toString());
				        	mySQLTransaction.rollback();
			        	}
			        	
					}else{
						mySQLTransaction.rollback();
						System.out.println("#################################################################");
			        	System.out.println("No fue posible registrar el Customer .... ");
			        	System.out.println("#################################################################");
			        	ErrorManager.manageErrorSaveInformationTransaction(transactionVO.getId(), "SaveInformationTransactionThread.saveInformationTransaction.CustomerDAOException.SaveCustomer", charge.toString());
					}
		        }else{
		        	
		        	System.out.println("El cliente EXISTE" + cardVOAUX.getId());
		        	cardVO.setId(cardVOAUX.getId());
		        	chargeVO.setCardId(cardVO.getId());
		        	chargeVO.setTransactionId(transactionVO.getId());
		        	
		        	System.out.println("chargeVO.getTransactionId(): " + chargeVO.getTransactionId());
					System.out.println("chargeVO.getStripeId(): " + chargeVO.getStripeId());
					System.out.println("chargeVO.getInvoiceId(): " + chargeVO.getInvoiceId());
					System.out.println("chargeVO.getAddressId(): " + chargeVO.getAddressId());
					
		        	ChargeDAO chargeDAO = new ChargeDAO(mySQLTransaction);
					initialTime = Calendar.getInstance().getTimeInMillis();
					if(chargeDAO.insert(chargeVO) != 0) {
						ErrorManager.manageErrorSaveInformationTransaction("SaveInformationTransactionThread.saveInformationTransaction.ChargeDAOException.SaveCharge", transactionVO.getId(), charge.toString());
					}
					
					finalTime = Calendar.getInstance().getTimeInMillis();
					System.out.println("Tiempo total para copiar registrar un Charge: " + (finalTime-initialTime) + " ms.");
					System.out.println("chargeVO.getId(): " + chargeVO.getId());
					mySQLTransaction.commit();
					
		        }
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
				SaveInformationTransactionThreadException saveInformationTransactionThreadException = new SaveInformationTransactionThreadException(e, transactionVO.getId(), "TransactionMDTR.saveInformationTransaction.MySQLTransactionException", charge.toString());
				saveInformationTransactionThreadException.setErrorCode("SaveInformationTransactionThread.saveInformationTransaction.MySQLTransactionException");
			} catch (MySQLConnectionException e) {
				e.printStackTrace();
			} catch (CardDAOException e) {
				e.printStackTrace();
				SaveInformationTransactionThreadException saveInformationTransactionThreadException = new SaveInformationTransactionThreadException(e, transactionVO.getId(), "TransactionMDTR.saveInformationTransaction.CardDAOException", charge.toString());
				saveInformationTransactionThreadException.setErrorCode("SaveInformationTransactionThreadException.saveInformationTransaction.CardDAOException");
			} catch (ChargeDAOException e) {
				e.printStackTrace();
				SaveInformationTransactionThreadException saveInformationTransactionThreadException = new SaveInformationTransactionThreadException(e, transactionVO.getId(), "TransactionMDTR.saveInformationTransaction.ChargeDAOException", charge.toString());
				saveInformationTransactionThreadException.setErrorCode("SaveInformationTransactionThreadException.saveInformationTransaction.ChargeDAOException");
			} catch (Exception e) {
				e.printStackTrace();
				SaveInformationTransactionThreadException saveInformationTransactionThreadException = new SaveInformationTransactionThreadException(e, transactionVO.getId(), "TransactionMDTR.saveInformationTransaction.Exception", charge.toString());
				saveInformationTransactionThreadException.setErrorCode("SaveInformationTransactionThreadException.saveInformationTransaction.Exception");
			}finally{
				transactionVO.setChargeVO(chargeVO);
				try {
					if(mySQLTransaction != null){
						mySQLTransaction.close();
					}
				} catch (MySQLTransactionException e) {
					e.printStackTrace();
				}
				stopThread();
			}
		}
		
//	      try {
//	         for(int i = 40; i > 0; i--) {
//	            System.out.println("Thread: " + threadName + ", " + i);
//	            // Let the thread sleep for a while.
//	            Thread.sleep(50);
//	         }
//	     } catch (InterruptedException e) {
//	         System.out.println("Thread " +  threadName + " interrupted.");
//	     }
	     
	}
	
	public void stopThread(){
		System.out.println("Thread " +  threadName + " exiting.");
		execute = false;
	}
	
//	public void start () {
//      System.out.println("Starting " +  threadName );
//      if (thread == null){
//    	  thread = new Thread (this, threadName);
//    	  threads.put(threadName, thread);
//    	  thread.start ();
//      }
//   }

	public HashMap<String, Thread> getThreads() {
		return threads;
	}

	public void setThreads(HashMap<String, Thread> threads) {
		this.threads = threads;
	}

	@Override
	public synchronized void start() {
		System.out.println("Starting " +  threadName );
		super.start();
	}

//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		super.run();
//	}
}

