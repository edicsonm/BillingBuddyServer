package au.com.billingbuddy.business.objects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import au.com.billigbuddy.utils.ErrorManager;
import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.objects.SubscriptionsToProcessDAO;
import au.com.billingbuddy.dao.objects.ErrorLogDAO;
import au.com.billingbuddy.dao.objects.SubmittedProcessLogDAO;
import au.com.billingbuddy.exceptions.objects.SubscriptionsToProcessDAOException;
import au.com.billingbuddy.exceptions.objects.ErrorLogDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.MySQLTransactionException;
import au.com.billingbuddy.exceptions.objects.SubmittedProcessLogDAOException;
import au.com.billingbuddy.exceptions.objects.SubscriptionsMDTRException;
import au.com.billingbuddy.vo.objects.SubscriptionsToProcessVO;
import au.com.billingbuddy.vo.objects.ErrorLogVO;
import au.com.billingbuddy.vo.objects.SubmittedProcessLogVO;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;


public class ProcessSubscriptionsMDTR {
	
	private static ProcessSubscriptionsMDTR instance = null;
	/*private static ConfigurationSystem configurationSystem = ConfigurationSystem.getInstance();
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();*/
	
	private long initialTime;
	private long finalTime;
	
	/*private long initialTimeIndividual;
	private long finalTimeIndividual;*/
	
	private boolean writeInErrorLog = false;
	private String logFileName;
	
	private Map<String, Thread> hashThreadsProcessSubscription = new HashMap<String, Thread>();
	private SubmittedProcessLogVO submittedProcessLogVO = new SubmittedProcessLogVO();
	private MySQLTransaction mySQLTransaction = null;
	private boolean answer = true;
	private boolean unpaids =  false;
	private boolean noUpdated =  false;
	
	private int numberUnpaids =  0;
	private int numberNoUpdated =  0;
	private int numberUpdated =  0;
	private int numberCharged =  0;
	private int numberSended =  0;
	private String processName = "ProccesDailySubscriptions";
	
	private boolean errorFileExist =  false;
	private ArrayList<SubscriptionsToProcessVO> listSubscriptionsToProcess = null;
	
	private boolean swErrorOnlevel1 = false;
	
	private static final Logger logger = LogManager.getLogger(ProcessSubscriptionsMDTR.class);
	/*"trace", "debug", "info", "warn", "error" and "fatal"*/
    
	public static synchronized ProcessSubscriptionsMDTR getInstance() {
		if (instance == null) {
			instance = new ProcessSubscriptionsMDTR();
		}
		return instance;
	}
	
	private ProcessSubscriptionsMDTR() {
		Stripe.apiKey = ConfigurationSystem.getKey("apiKey");
	}
	
	public synchronized boolean proccesDailySubscriptions() throws SubscriptionsMDTRException {
		
		logger.trace("/*****************************************************************************************************/");
		logger.trace("/*********************************INICIANDO PROCESO DE SUBSCRIPCIONES*********************************/");
		logger.trace("/*****************************************************************************************************/");
		
		logger.info("/*****************************************************************************************************/");
		logger.info("/*********************************INICIANDO PROCESO DE SUBSCRIPCIONES*********************************/");
		logger.info("/*****************************************************************************************************/");
		
		setLogFileName(ConfigurationSystem.getKey("urlSaveErrorFilesSaveInformationSubscriptions") + processName + " - "+ Calendar.getInstance().getTime());
		
		try {
			initialTime = Calendar.getInstance().getTimeInMillis();
			mySQLTransaction = new MySQLTransaction();
			
			submittedProcessLogVO.setProcessName(processName);
			submittedProcessLogVO.setStartTime(Calendar.getInstance().getTime().toString());
			submittedProcessLogVO.setStatusProcess("OnExecution");
			SubmittedProcessLogDAO submittedProcessLogDAO = new SubmittedProcessLogDAO();
			submittedProcessLogDAO.insert(submittedProcessLogVO);
			
			SubscriptionsToProcessDAO subscriptionsToProcessDAO = new SubscriptionsToProcessDAO(mySQLTransaction);
			listSubscriptionsToProcess = subscriptionsToProcessDAO.search();
			
			if(listSubscriptionsToProcess != null && listSubscriptionsToProcess.size() > 0) {
				for (int position = 0; position<listSubscriptionsToProcess.size(); position++) {
					
				/*for (Iterator<DailySubscriptionVO> iterator = listSubscriptionsToProcess.iterator(); iterator .hasNext();) {*/
//					initialTimeIndividual = Calendar.getInstance().getTimeInMillis();
					/*DailySubscriptionVO dailySubscriptionVO = iterator.next();*/
					
					SubscriptionsToProcessVO subscriptionsToProcessVO = listSubscriptionsToProcess.get(position);
					
					int first = listSubscriptionsToProcess.indexOf(subscriptionsToProcessVO);
					int last = listSubscriptionsToProcess.lastIndexOf(subscriptionsToProcessVO);
					
					logger.debug(position+" "+subscriptionsToProcessVO.getSubscriptionId() + " first: " + first);
					logger.debug("  "+subscriptionsToProcessVO.getSubscriptionId() + " last: " + last);
					
					ArrayList<SubscriptionsToProcessVO> listSubscriptionsToProcessAUX = new ArrayList<SubscriptionsToProcessVO>(listSubscriptionsToProcess.subList(first, last + 1));
					position = last;
					
					ProcessSubscription processSubscription = new ProcessSubscription(listSubscriptionsToProcessAUX, subscriptionsToProcessDAO, mySQLTransaction);
					processSubscription.setName(subscriptionsToProcessVO.getSubscriptionId());
					processSubscription.start();
					hashThreadsProcessSubscription.put(processSubscription.getName(), processSubscription);
//					
					/*hashMapCharge.put("amount", Utilities.currencyToStripe(dailySubscriptionVO.getAmount(), dailySubscriptionVO.getCurrency()));
					hashMapCharge.put("currency", dailySubscriptionVO.getCurrency());
					hashMapCharge.put("description", "Charge for test@example.com");
			
					Map<String, Object> hashMapCard = new HashMap<String, Object>();
					hashMapCard.put("number", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getNumber());
					hashMapCard.put("exp_month", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpMonth());
					hashMapCard.put("exp_year", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpYear());
					hashMapCard.put("cvc", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getCvv());
					hashMapCard.put("name", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getName());
					hashMapCharge.put("card", hashMapCard);
					try {
						Charge charge = Charge.create(hashMapCharge);
//						System.out.println("Se optiene autorizacion numero : " + charge.getId());
						dailySubscriptionVO.setStatus("Charged");
						dailySubscriptionVO.setAuthorizerCode(charge.getId());
						dailySubscriptionVO.setAuthorizerReason(null);
					} catch (CardException e) {
						unpaids = true;
//						System.out.println("Error: " + e.getMessage());
						dailySubscriptionVO.setStatus("Unpaid");
						dailySubscriptionVO.setAuthorizerCode(null);
						dailySubscriptionVO.setAuthorizerReason(e.getMessage());
						answer = false;
					}
					try {
						if(dailySubscriptionDAO.update(dailySubscriptionVO)== 0) {
//							System.out.println("No fue posible actualizar la informacion del pago. Suspender todo el proceso. " + "Generar informe con la informacion del error");
							JSONObject errorDetails = new JSONObject();
							errorDetails.put("Dasu_Status", dailySubscriptionVO.getStatus());
							errorDetails.put("Dasu_AuthorizerCode", dailySubscriptionVO.getAuthorizerCode());
							errorDetails.put("Dasu_AuthorizerReason", dailySubscriptionVO.getAuthorizerReason());
							errorDetails.put("Dasu_ID", dailySubscriptionVO.getId());
							errorDetails.put("Subs_ID", dailySubscriptionVO.getSubscriptionId());
							errorDetails.put("AutorizationID", dailySubscriptionVO.getAuthorizerCode());
							errorDetails.put("CALL_DailySubscriptionDAO", dailySubscriptionDAO.getCallString());
							writeError(errorDetails);
							answer = false;
							noUpdated =  true;
							errorFileExist = true;
						}
					} catch (DailySubscriptionDAOException e) {
//						System.out.println("No fue posible actualizar la informacion del pago. Suspender todo el proceso. " + "Generar informe con la informacion del error");
						JSONObject errorDetails = new JSONObject();
						errorDetails.put("Dasu_Status", dailySubscriptionVO.getStatus());
						errorDetails.put("Dasu_AuthorizerCode", dailySubscriptionVO.getAuthorizerCode());
						errorDetails.put("Dasu_AuthorizerReason", dailySubscriptionVO.getAuthorizerReason());
						errorDetails.put("Dasu_ID", dailySubscriptionVO.getId());
						errorDetails.put("Subs_ID", dailySubscriptionVO.getSubscriptionId());
						errorDetails.put("AutorizationID", dailySubscriptionVO.getAuthorizerCode());
						errorDetails.put("CALL_DailySubscriptionDAO", dailySubscriptionDAO.getCallString());
						writeError(errorDetails);
						answer = false;
						errorFileExist = true;
						new SubscriptionsMDTRException(e, dailySubscriptionDAO.getCallString());
					}*/
//					finalTimeIndividual = Calendar.getInstance().getTimeInMillis();
//					System.out.println("Tiempo total para procesar una subscripcion: " + (finalTimeIndividual-initialTimeIndividual) + " ms.");
				}
				logger.info("Todos los hilos fueron creados, se llama al proceso de monitoreo de los hilos creados");
				logger.trace("Todos los hilos fueron creados, se llama al proceso de monitoreo de los hilos creados");
				
				MonitorProcessSubscription monitorProcessSubscription = new MonitorProcessSubscription(hashThreadsProcessSubscription);
				monitorProcessSubscription.start();
//				finalTime = Calendar.getInstance().getTimeInMillis();
//				System.out.println("Tiempo total para procesar todas las subscripciones: " + (finalTime-initialTime) + " ms.");
			
			} else {

				finalTime = Calendar.getInstance().getTimeInMillis();
				logger.info("Tiempo total para procesar todas las subscripciones: " + (finalTime-initialTime) + " ms.");
				logger.trace("Tiempo total para procesar todas las subscripciones: " + (finalTime-initialTime) + " ms.");
				try {
					
					submittedProcessLogDAO = new SubmittedProcessLogDAO();
					submittedProcessLogVO.setEndTime(Calendar.getInstance().getTime().toString());
					
					if(answer) submittedProcessLogVO.setStatusProcess("Success");
					else submittedProcessLogVO.setStatusProcess("Error");
					
					JSONObject informationDetails = new JSONObject();
					
					JSONObject information = new JSONObject();
					information.put("unpaids", unpaids);
					if(unpaids){
						information.put("Recomendation","Run the \"Reprocess Process\" to resend the transactions to our processor.");
						information.put("Information", "There are subscripcions that our procesor could not charge to some card holders. The uncharged transactions information are available in the tables Reprocess_X ");
						informationDetails.put("InformationUnpaids", information);
						informationDetails.put("ReprocessUnpaids", unpaids);
					}
					
					
					information = new JSONObject();
					information.put("noUpdated", noUpdated);
					if(noUpdated){
						information.put("InformationNoUpdated", "There are subscripcions that were charged by our processor but the information was not updated in our systems.");
						information.put("Recomendation","Check system logs to determine the causes of the error.");
						informationDetails.put("InformationNoUpdated", information);
					}
					
					information = new JSONObject();
					information.put("errorFileExist", errorFileExist);
					if(errorFileExist){
						information.put("Information", "Was created a file that content information about the subscripcions that could not Update.");
						information.put("Recomendation","Execute the recovery process to update the correct field in our data base.");
						information.put("FileLocation",getLogFileName());
						informationDetails.put("ReprocessErrorFile", errorFileExist);
						informationDetails.put("InformationErrorFileExist", information);
					}
					
					information = new JSONObject();
					information.put("Total Time", ((finalTime-initialTime) + " ms."));
					information.put("Total Transactions to process", listSubscriptionsToProcess.size());
					information.put("Total Transactions no updates on Data Base", numberNoUpdated);
					information.put("Total Transactions updates on Data Base", numberUpdated);
					information.put("Total Transactions unpaids", numberUnpaids);
					information.put("Total Transactions chargeds", numberCharged);
					information.put("Total Transactions sended to our procesor", numberSended);
					information.put("Total Transactions keeped on file ", numberSended);
					informationDetails.put("Resume ProcessExecution", information);
					
					/**/
					System.out.println("Imprimiendo resumen wqwe qweqwe ... ");
					System.out.println( "Total de numberNoUpdated: " + numberNoUpdated);
					System.out.println( "Total de numberUpdated: " + numberUpdated);
					System.out.println( "Total de numberUnpaids: " + numberUnpaids);
					System.out.println( "Total de numberCharged: " + numberCharged);
					
					logger.info("Imprimiendo resumen wqwe qweqwe ... ");
					logger.info( "Total de numberNoUpdated: " + numberNoUpdated);
					logger.info( "Total de numberUpdated: " + numberUpdated);
					logger.info( "Total de numberUnpaids: " + numberUnpaids);
					logger.info( "Total de numberCharged: " + numberCharged);
					/**/
					
					submittedProcessLogVO.setInformation(informationDetails.toJSONString());
					submittedProcessLogDAO.update(submittedProcessLogVO);
					mySQLTransaction.commit();
					
				} catch (MySQLConnectionException e) {
					e.printStackTrace();
				} catch (MySQLTransactionException e) {
					e.printStackTrace();
				} catch (SubmittedProcessLogDAOException e) {
					e.printStackTrace();
				} finally{
					try {
						if(mySQLTransaction != null){
							mySQLTransaction.close();
						}
					} catch (MySQLTransactionException e) {
						e.printStackTrace();
					}
				}
			
			}
			
			/*submittedProcessLogDAO = new SubmittedProcessLogDAO();
			submittedProcessLogVO.setEndTime(Calendar.getInstance().getTime().toString());
			if(answer) submittedProcessLogVO.setStatusProcess("Success");
			else submittedProcessLogVO.setStatusProcess("Error");
			
			JSONObject informationDetails = new JSONObject();
			
			JSONObject information = new JSONObject();
			information.put("unpaids", unpaids);
			if(unpaids){
				information.put("Recomendation","Run the \"Reprocess Process\" to resend the transactions to our processor.");
				information.put("Information", "There are subscripcions that our procesor could not charge to some card holders. The uncharges transactions information are available in the tables Reprocess_X ");
			}
			informationDetails.put("InformationUnpaids", information);
			
			information = new JSONObject();
			information.put("noUpdated", noUpdated);
			if(noUpdated){
				information.put("InformationNoUpdated", "There are subscripcions that were charged by our processor but the information was not updated in our systems.");
				information.put("Recomendation","Check system logs to determine the causes of the error.");
			}
			informationDetails.put("InformationNoUpdated", information);
			
			information = new JSONObject();
			information.put("errorFileExist", errorFileExist);
			if(errorFileExist){
				information.put("Information", "Was created a file that content information about the subscripcions that could not Update.");
				information.put("Recomendation","Execute the recovery process to update the correct field in our data base.");
			}
			informationDetails.put("InformationErrorFileExist", information);
			informationDetails.put("Processing Time", ((finalTime-initialTime) + " ms."));
			
			submittedProcessLogVO.setInformation(informationDetails.toJSONString());
			submittedProcessLogDAO.update(submittedProcessLogVO);
			mySQLTransaction.commit();*/
		} catch (MySQLConnectionException e) {
			throw new SubscriptionsMDTRException(e);
//		} catch (AuthenticationException e) {
//			e.printStackTrace();
//		} catch (APIConnectionException e) {
//			e.printStackTrace();
//		} catch (APIException e) {
//			e.printStackTrace();
//		} catch (InvalidRequestException e) {
//			e.printStackTrace();
		} catch (SubscriptionsToProcessDAOException e) {
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.DailySubscriptionDAOException");
			throw subscriptionsMDTRException;
		} catch (MySQLTransactionException e) {
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.MySQLTransactionException");
			throw subscriptionsMDTRException;
		} catch (SubmittedProcessLogDAOException e) {
			throw new SubscriptionsMDTRException(e);
//		} finally{
//			try {
//				if(mySQLTransaction != null){
//					mySQLTransaction.close();
//				}
//			} catch (MySQLTransactionException e) {
//				e.printStackTrace();
//			}
		}
		return answer;
	}
	
	public void processSubscription(){
		
	}
	
	public ArrayList<SubscriptionsToProcessVO> listSubscriptionsToProcess() throws SubscriptionsMDTRException{
		ArrayList<SubscriptionsToProcessVO> listSubscriptionsToProcess = null;
		try {
			SubscriptionsToProcessDAO subscriptionsToProcessDAO = new SubscriptionsToProcessDAO();
			listSubscriptionsToProcess = subscriptionsToProcessDAO.search();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.listSubscriptionsToProcess.MySQLConnectionException");
			throw subscriptionsMDTRException;
		} catch (SubscriptionsToProcessDAOException e) {
			e.printStackTrace();
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.listSubscriptionsToProcess.SubscriptionsToProcessDAOException e");
			throw subscriptionsMDTRException;
		}
		return listSubscriptionsToProcess;
	}
	
	public synchronized void writeError(JSONObject errorDetails){
		try{
			writeInErrorLog = true;
			File file = new File(getLogFileName());
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fstream = new FileWriter(file, true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(errorDetails.toJSONString());
			out.newLine();
			out.close();
		}catch(IOException e){
    		e.printStackTrace();
    	}
	}
	
	public JSONObject reprocessFile(JSONObject jSONObjectParameters) throws SubscriptionsMDTRException {
		MySQLTransaction mySQLTransaction = null;
		File archivo = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		SubscriptionsToProcessVO subscriptionsToProcessVO = null;
		
		int unProcessed = 0;
		int processed = 0;
		int totalRegistries = 0;
		try {
			mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			SubscriptionsToProcessDAO subscriptionsToProcessDAO = new SubscriptionsToProcessDAO(mySQLTransaction);
			archivo = new File(jSONObjectParameters.get("fileName").toString());
			fileReader = new FileReader(archivo);
			bufferedReader = new BufferedReader(fileReader);
			String linea;
			while ((linea = bufferedReader.readLine()) != null) {
				totalRegistries ++ ;
				Object obj = JSONValue.parse(linea);
				JSONObject jSONObject = (JSONObject) obj;
				subscriptionsToProcessVO = new SubscriptionsToProcessVO();
				subscriptionsToProcessVO.setStatus(jSONObject.get("Dasu_Status").toString());
				subscriptionsToProcessVO.setAuthorizerCode(jSONObject.get("Dasu_AuthorizerCode")!= null? jSONObject.get("Dasu_AuthorizerCode").toString():null);
				subscriptionsToProcessVO.setAuthorizerReason(jSONObject.get("Dasu_AuthorizerReason") != null ? jSONObject.get("Dasu_AuthorizerReason").toString():null);
				subscriptionsToProcessVO.setId(jSONObject.get("Dasu_ID").toString());
				if(jSONObject.get("ReprocessTRX") != null && jSONObject.get("ReprocessTRX").toString().equalsIgnoreCase("true")){
					try {
						processed ++;
						subscriptionsToProcessDAO.update(subscriptionsToProcessVO);
//						System.out.println(dailySubscriptionVO.getId()+ ":" + dailySubscriptionDAO.update(dailySubscriptionVO));
					} catch (SubscriptionsToProcessDAOException e) {
						mySQLTransaction.rollback();
						throw new SubscriptionsMDTRException(e);
					}
				}else{
					unProcessed ++;
				}
			}
			
			if(totalRegistries == processed) {
				SubmittedProcessLogDAO submittedProcessLogDAO = new SubmittedProcessLogDAO(mySQLTransaction);
				submittedProcessLogVO.setId(jSONObjectParameters.get("idSubmittedProcessLog").toString());
				
				submittedProcessLogVO = submittedProcessLogDAO.searchByID(submittedProcessLogVO);				
				
				Object obj = JSONValue.parse(submittedProcessLogVO.getInformation());
				JSONObject jSONObjectInitial = (JSONObject) obj;
				jSONObjectInitial.remove("ReprocessErrorFile");
				
				JSONObject jSONObjectFinal = new JSONObject();
				jSONObjectFinal.put("Total number of registries", totalRegistries);
				jSONObjectFinal.put("Number of processed", processed);
				jSONObjectFinal.put("Number of unProcessed", unProcessed);
				jSONObjectFinal.put("Reprocessing date", Calendar.getInstance().getTime().toString());
				
				JSONObject information = new JSONObject();
				information.put("Initial Information", jSONObjectInitial);
				information.put("Final Information", jSONObjectFinal);
				submittedProcessLogVO.setInformation(information.toJSONString());
				submittedProcessLogVO.setStatusProcess("Success");
				submittedProcessLogDAO.update(submittedProcessLogVO);
				jSONObjectParameters.put("answer", true);
			}else {
				if(processed == 0) jSONObjectParameters.put("errorType", "noRegistriesUpdated");
				jSONObjectParameters.put("answer", false);
			}
			mySQLTransaction.commit();
//			System.out.println("Termina el proceso satisfactoriamente");
			jSONObjectParameters.put("unProcessed", unProcessed);
			jSONObjectParameters.put("processed", processed);
			jSONObjectParameters.put("totalRegistries", totalRegistries);
			
			/*System.out.println("unProcessed: " + unProcessed);
			System.out.println("processed: " + processed);
			System.out.println("totalRegistries: " + totalRegistries);*/
			return jSONObjectParameters;
		} catch (MySQLTransactionException e) {
			/*e.printStackTrace();*/
			throw new SubscriptionsMDTRException(e);
		} catch (MySQLConnectionException e) {
			/*e.printStackTrace();*/
			throw new SubscriptionsMDTRException(e);
		} catch (FileNotFoundException e) {
			/*e.printStackTrace();*/
			throw new SubscriptionsMDTRException(e);
		} catch (IOException e) {
			/*e.printStackTrace();*/
			throw new SubscriptionsMDTRException(e);
		} catch (SubmittedProcessLogDAOException e) {
			throw new SubscriptionsMDTRException(e);
		} finally {
			try {
				if (null != fileReader) {
					fileReader.close();
				}
			} catch (Exception e2) {
				throw new SubscriptionsMDTRException(e2);
			}
			try {
				if(mySQLTransaction != null){
					mySQLTransaction.close();
				}
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void manageError(JSONObject jSONObject,String processName, String fileName) {
		try {
			ErrorLogDAO errorLogDAO = new ErrorLogDAO();
			ErrorLogVO errorLogVO = new ErrorLogVO();
			errorLogVO.setProcessName(processName);
			errorLogVO.setInformation(jSONObject.toJSONString());
			if(errorLogDAO.insert(errorLogVO) == 0){
				ErrorManager.logDailySubscriptionErrorFile(fileName, jSONObject);
			}
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
		} catch (ErrorLogDAOException e) {
			e.printStackTrace();
		}
	}
	
	private void printTimes(long initialTime, long finalTime) {
		System.out.println("Time to obtain answer from maxmind: " + (finalTime-initialTime) + " ms.");
		logger.info("Time to obtain answer from maxmind: " + (finalTime-initialTime) + " ms.");
	}

	public boolean isWriteInErrorLog() {
		return writeInErrorLog;
	}

	public void setWriteInErrorLog(boolean writeInErrorLog) {
		this.writeInErrorLog = writeInErrorLog;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
	
	
	class MonitorProcessSubscription extends Thread {
		Map<String, Thread> hashProcessSubscription = new HashMap<String, Thread>();
		
		public MonitorProcessSubscription(Map<String, Thread> hashProcessSubscription){
			this.hashProcessSubscription = hashProcessSubscription;
		}		
		@Override
		public void run() {
			while (!hashProcessSubscription.isEmpty()) {
//					System.out.println("Esperando la finalizacion de " + hashProcessSubscription.size() + " hilos ...");
					logger.info("Esperando la finalizacion de " + hashProcessSubscription.size() + " hilos ... " + "(numberSended: " + numberSended + ") (numberCharged: " +numberCharged+") (numberUnpaids: "  + numberUnpaids+")");
//					System.out.println("Esperando la finalizacion de " + hashProcessSubscription.size() + " hilos ... " + "(numberSended: " + numberSended + ") (numberCharged: " +numberCharged+") (numberUnpaids: "  + numberUnpaids+")");
					try {
						this.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
//			System.out.println("Hilos finalizados .... " + "(numberSended: " + getNumberSended() + ") (numberCharged: " +getNumberCharged()+") (numberUnpaids: "  + getNumberUnpaids()+")");
			finalTime = Calendar.getInstance().getTimeInMillis();
			logger.info("Tiempo total para procesar todas las subscripciones: " + (finalTime-initialTime) + " ms.");
//			System.out.println("Tiempo total para procesar todas las subscripciones: " + (finalTime-initialTime) + " ms.");
		}
	}
	
	class ProcessSubscription extends Thread {
		
		private Map<String, Object> hashMapCharge = new HashMap<String, Object>();
		private ArrayList<SubscriptionsToProcessVO> listSubscriptionsToProcess;
//		private SubscriptionsToProcessVO subscriptionsToProcessVO;
		private SubscriptionsToProcessDAO subscriptionsToProcessDAO;
		private SubmittedProcessLogDAO submittedProcessLogDAO;
		private MySQLTransaction mySQLTransaction;
		private int level = 0;
		private boolean unpaidSubGroup = false;
		
		public ProcessSubscription(ArrayList<SubscriptionsToProcessVO> listSubscriptionsToProcess, SubscriptionsToProcessDAO subscriptionsToProcessDAO, MySQLTransaction mySQLTransaction){
			this.listSubscriptionsToProcess = listSubscriptionsToProcess;
			this.subscriptionsToProcessDAO = subscriptionsToProcessDAO;
			this.mySQLTransaction = mySQLTransaction;
		}
		
		@Override
		public void run() {
			logger.debug("El hilo " + this.getName() + " tiene " + listSubscriptionsToProcess.size() + " elementos para procesar");
			logger.trace("El hilo " + this.getName() + " tiene " + listSubscriptionsToProcess.size() + " elementos para procesar");
			/*System.out.println("El hilo " + this.getName() + " tiene " + listSubscriptionsToProcess.size() + " elementos para procesar");*/
			while(true){
				try {
					for (SubscriptionsToProcessVO dailySubscriptionVO : listSubscriptionsToProcess) {
//						System.out.println("Ejecutando " + this.getName() + " " + dailySubscriptionVO.getId());
						hashMapCharge.put("amount", Utilities.currencyToStripe(dailySubscriptionVO.getAmount(), dailySubscriptionVO.getCurrency()));
						hashMapCharge.put("currency", dailySubscriptionVO.getCurrency());
						hashMapCharge.put("description", "Charge for test@example.com");
				
						Map<String, Object> hashMapCard = new HashMap<String, Object>();
						hashMapCard.put("number", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getNumber());
						hashMapCard.put("exp_month", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpMonth());
						hashMapCard.put("exp_year", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpYear());
						hashMapCard.put("cvc", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getCvv());
						hashMapCard.put("name", dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getName());
						hashMapCharge.put("card", hashMapCard);
						try {
							dailySubscriptionVO.setStatus("Sending");
							dailySubscriptionVO.setAuthorizerCode(null);
							dailySubscriptionVO.setAuthorizerReason(null);
							if (subscriptionsToProcessDAO.update(dailySubscriptionVO) != 0) {
								level = 1;
								try {
									sended();
									Charge charge = Charge.create(hashMapCharge);
									dailySubscriptionVO.setProcessAttempt("0");
									dailySubscriptionVO.setStatus("Charged");
									dailySubscriptionVO.setAuthorizerCode(charge.getId());
									dailySubscriptionVO.setAuthorizerReason(null);
									charged();
	//								System.out.println("Charged: " +getNumberCharged());
								} catch (CardException e) {
									unpaid();
									unpaids = true;
									unpaidSubGroup =  true;
	//								System.out.println("Error: " + e.getMessage());
									dailySubscriptionVO.setProcessAttempt(String.valueOf(Integer.parseInt(dailySubscriptionVO.getProcessAttempt()) + 1));
									dailySubscriptionVO.setStatus("Unpaid");
									dailySubscriptionVO.setAuthorizerCode(null);
									dailySubscriptionVO.setAuthorizerReason(e.getMessage());
									System.out.println("Pago no aplicado " + this.getName() + " " + dailySubscriptionVO.getId() );
	//								answer = false;
								}
	//						}
	//							dailySubscriptionVO.setErrorCode("2");//User esta variable para simular errores
								if(subscriptionsToProcessDAO.update(dailySubscriptionVO) == 0) {
	//								System.out.println("No fue posible actualizar la informacion del pago. Suspender todo el proceso. " + "Generar informe con la informacion del error");
									JSONObject errorDetails = new JSONObject();
									errorDetails.put("Dasu_Status", dailySubscriptionVO.getStatus());
									errorDetails.put("Dasu_AuthorizerCode", dailySubscriptionVO.getAuthorizerCode());
									errorDetails.put("Dasu_AuthorizerReason", dailySubscriptionVO.getAuthorizerReason());
									errorDetails.put("Dasu_ID", dailySubscriptionVO.getId());
									errorDetails.put("Subs_ID", dailySubscriptionVO.getSubscriptionId());
									errorDetails.put("AutorizationID", dailySubscriptionVO.getAuthorizerCode());
									errorDetails.put("CALL_DailySubscriptionDAO", subscriptionsToProcessDAO.getCallString());
									errorDetails.put("ReprocessTRX",true);
									writeError(errorDetails);
									answer = false;
									noUpdated =  true;
									noUpdated();
									setSwErrorOnlevel1(true);
									errorFileExist = true;
								}else{
									updated();
								}
								if(unpaidSubGroup) break; //Rompe el ciclo despues de que consigue una subscripcion que no fue aprobada y es actualizada en las tablas de las subscripciones
							}
						} catch (SubscriptionsToProcessDAOException e) {
	//						System.out.println("No fue posible actualizar la informacion del pago. Suspender todo el proceso. " + "Generar informe con la informacion del error");
							JSONObject errorDetails = new JSONObject();
							errorDetails.put("Dasu_Status", dailySubscriptionVO.getStatus());
							errorDetails.put("Dasu_AuthorizerCode", dailySubscriptionVO.getAuthorizerCode());
							errorDetails.put("Dasu_AuthorizerReason", dailySubscriptionVO.getAuthorizerReason());
							errorDetails.put("Dasu_ID", dailySubscriptionVO.getId());
							errorDetails.put("Subs_ID", dailySubscriptionVO.getSubscriptionId());
							errorDetails.put("AutorizationID", dailySubscriptionVO.getAuthorizerCode());
							errorDetails.put("CALL_DailySubscriptionDAO", subscriptionsToProcessDAO.getCallString());
							if(level == 1) {
								/*Esto es para verificar que el archivo de errores contiene transacciones que deben ser cargadas en la BD.
								 *Esto es porque el error ocurre antes de enviar la transaccion, por lo que el registro no se debe modificar para que
								 *pueda ser reenviado como si fuese la primera vez*/
								setSwErrorOnlevel1(true);
								errorDetails.put("ReprocessTRX",true);
							}else{
								errorDetails.put("ReprocessTRX",false);
							}
							noUpdated();
							writeError(errorDetails);
							answer = false;
							errorFileExist = true;
							new SubscriptionsMDTRException(e, subscriptionsToProcessDAO.getCallString());
						}
					}
				} catch (AuthenticationException e) {
					e.printStackTrace();
				} catch (APIConnectionException e) {
					e.printStackTrace();
				} catch (APIException e) {
					e.printStackTrace();
				} catch (InvalidRequestException e) {
					e.printStackTrace();
				}
				break;
			}
			
			hashThreadsProcessSubscription.remove(this.getName());
			
			if (hashThreadsProcessSubscription.isEmpty()) {

				finalTime = Calendar.getInstance().getTimeInMillis();
				logger.info("Tiempo total para procesar todas las subscripciones: " + (finalTime-initialTime) + " ms.");
				System.out.println("Tiempo total para procesar todas las subscripciones: " + (finalTime-initialTime) + " ms.");
				try {
					
					submittedProcessLogDAO = new SubmittedProcessLogDAO();
					submittedProcessLogVO.setEndTime(Calendar.getInstance().getTime().toString());
					
					if(answer) submittedProcessLogVO.setStatusProcess("Success");
					else submittedProcessLogVO.setStatusProcess("Error");
					
					JSONObject informationDetails = new JSONObject();
					
					JSONObject information = new JSONObject();
					information.put("unpaids", unpaids);
					if(unpaids){
						information.put("Recomendation","Run the \"Reprocess Process\" to resend the transactions to our processor.");
						information.put("Information", "There are subscripcions that our procesor could not charge to some card holders. The uncharges transactions information are available in the tables Reprocess_X ");
						informationDetails.put("InformationUnpaids", information);
						informationDetails.put("ReprocessUnpaids", unpaids);
					}
					
					information = new JSONObject();
					information.put("noUpdated", noUpdated);
					if(noUpdated && swErrorOnlevel1){
						information.put("InformationNoUpdated", "There are subscripcions that were charged by our processor but the information was not updated in our systems.");
						information.put("Recomendation","Check system logs to determine the causes of the error.");
						informationDetails.put("InformationNoUpdated", information);
					}
					
					information = new JSONObject();
					information.put("errorFileExist", errorFileExist);
					if(errorFileExist && swErrorOnlevel1){
						information.put("Information", "Was created a file that content information about the subscripcions that could not Update.");
						information.put("Recomendation","Execute the recovery process to update the correct field in our data base.");
						information.put("FileLocation",getLogFileName());
						informationDetails.put("ReprocessErrorFile", errorFileExist);
						informationDetails.put("InformationErrorFileExist", information);
					}else if(errorFileExist && !swErrorOnlevel1){
						information.put("Information", "Was created a file that content information about the subscripcions that could not be sent to out processor.");
						information.put("FileLocation",getLogFileName());
						informationDetails.put("ReprocessErrorFile", false);
						informationDetails.put("InformationErrorFileExist", information);
					}
					
					information = new JSONObject();
					information.put("Total Time", ((finalTime-initialTime) + " ms."));
					information.put("Total Transactions to process", listSubscriptionsToProcess.size());
					information.put("Total Transactions no updates on Data Base", numberNoUpdated);
					information.put("Total Transactions updates on Data Base", numberUpdated);
					information.put("Total Transactions keeped on file ", numberNoUpdated);
					information.put("Total Transactions unpaids", numberUnpaids);
					information.put("Total Transactions chargeds", numberCharged);
					information.put("Total Transactions sended to our procesor", numberSended);
					informationDetails.put("Resume ProcessExecution", information);
					
					/**/
					logger.info("Imprimiendo resumen FINAL ... ");
					logger.info( "Total de numberNoUpdated: " + numberNoUpdated);
					logger.info( "Total de numberUpdated: " + numberUpdated);
					logger.info( "Total de numberUnpaids: " + numberUnpaids);
					logger.info( "Total de numberCharged: " + numberCharged);
					
					logger.info("Imprimiendo resumen FINAL ... ");
					logger.info( "Total de numberNoUpdated: " + numberNoUpdated);
					logger.info( "Total de numberUpdated: " + numberUpdated);
					logger.info( "Total de numberUnpaids: " + numberUnpaids);
					logger.info( "Total de numberCharged: " + numberCharged);
					/**/
					
					submittedProcessLogVO.setInformation(informationDetails.toJSONString());
					submittedProcessLogDAO.update(submittedProcessLogVO);
					mySQLTransaction.commit();
					
				} catch (MySQLConnectionException e) {
					e.printStackTrace();
				} catch (MySQLTransactionException e) {
					e.printStackTrace();
				} catch (SubmittedProcessLogDAOException e) {
					e.printStackTrace();
				} finally{
					try {
						if(mySQLTransaction != null){
							mySQLTransaction.close();
						}
					} catch (MySQLTransactionException e) {
						e.printStackTrace();
					}
				}
				logger.info("Este es un mensaje de salida");
			}
		}
	}
	
	private synchronized void sended(){
		numberSended ++;
	}
	
	private synchronized void charged(){
		numberCharged ++;
	}
	
	private synchronized void unpaid(){
		numberUnpaids ++;
	}
	
	private synchronized void noUpdated(){
		numberNoUpdated ++;
	}
	
	private synchronized void updated(){
		numberUpdated ++;
	}

	public synchronized void setSwErrorOnlevel1(boolean swErrorOnlevel1) {
		this.swErrorOnlevel1 = swErrorOnlevel1;
	}
	
}
