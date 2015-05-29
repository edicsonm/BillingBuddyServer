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
import java.util.Set;

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
import au.com.billingbuddy.loggers.LoggerDeclined;
import au.com.billingbuddy.loggers.LoggerProcessed;
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
	
	private HashMap<String, ProcessSubscription> hashThreadsProcessSubscription = new HashMap<String, ProcessSubscription>();
	private SubmittedProcessLogVO submittedProcessLogVO = new SubmittedProcessLogVO();
	private MySQLTransaction mySQLTransaction = null;
	private String processName = "ProcessSubscriptions";
	
	private long initialTime;
	private long finalTime;
	
	private long initialTimeThreadsCreation;
	private long finalTimeThreadsCreation;
	
	private boolean writeInErrorLog = false;
	private String logFileName;
	
	private boolean answer = true;
	private boolean unpaids =  false;
	private boolean noUpdated =  false;
	
	private int numberUnpaids =  0;
	private int numberNoUpdated =  0;
	private int numberUpdated =  0;
	private int numberCharged =  0;
	private int numberSent =  0;
	private int numberTransactionsToProcess = 0;
	
	private boolean swErrorOnlevel1 = false;
	private boolean errorFileExist =  false;
	private ArrayList<SubscriptionsToProcessVO> listSubscriptionsToProcess = null;
	
	/*"trace", "debug", "info", "warn", "error" and "fatal"*/
	private static final Logger logger = LogManager.getLogger(ProcessSubscriptionsMDTR.class);
	private static final Logger loggerProcessed = LoggerProcessed.logger;
	private static final Logger loggerDeclined = LoggerDeclined.logger;
    
	public static synchronized ProcessSubscriptionsMDTR getInstance() {
		if (instance == null) {
			instance = new ProcessSubscriptionsMDTR();
		}
		return instance;
	}
	
	private ProcessSubscriptionsMDTR() {
		Stripe.apiKey = ConfigurationSystem.getKey("apiKey");
	}
	
	
	public HashMap<String,String> executeSubscriptionsToProcess() throws SubscriptionsMDTRException {
		HashMap<String,String> resp = null;
		initVariables();
		try {
			SubscriptionsToProcessDAO subscriptionsToProcessDAO = new SubscriptionsToProcessDAO();
			logger.info("/*****************************************************************************************************/");
			logger.info("/**********************INICIANDO EL PROCESO DE PROC_EXECUTE_SUBSCRIPTIONS_PROCESS *********************/");
			logger.info("/******************************************************************************************************/");
			resp = subscriptionsToProcessDAO.execute();
			if(resp != null){
				Set<String> set = resp.keySet();
				for (String key : set) {
					logger.info(key+" --> "+resp.get(key));
				}
				logger.info("/*****************************************************************************************************/");
				logger.info("/************************TERMINA EL PROCESO DE PROC_EXECUTE_SUBSCRIPTIONS_PROCESS*********************/");
				logger.info("/*****************************************************************************************************/");
				if(resp.get("P_ERROR_CODE").equalsIgnoreCase("000")){
					try {
						boolean respuesta = instance.proccesSubscriptions();
						if (!respuesta){//Se presento algun error
							if(instance.isWriteInErrorLog()){
								logger.info("Se presentaron errores, la informacion se encuentra almacenada en el archivo " + instance.getLogFileName());
								logger.info("Verifique las causas de los errores y ejecute el proceso de recuperacion de errores.");
							}
						}
					} catch (SubscriptionsMDTRException e) {
						logger.info(e.getMessage());
						logger.error(e);
					}
				}else{
					try {
						JSONObject informationDetails = new JSONObject();
						informationDetails.putAll(resp);
						submittedProcessLogVO.setProcessName(processName);
						submittedProcessLogVO.setStartTime(Calendar.getInstance().getTime().toString());
						submittedProcessLogVO.setStatusProcess("Error");
						submittedProcessLogVO.setInformation(informationDetails.toJSONString());
						SubmittedProcessLogDAO submittedProcessLogDAO = new SubmittedProcessLogDAO();
						submittedProcessLogDAO.insert(submittedProcessLogVO);
					} catch (SubmittedProcessLogDAOException e) {
						throw new SubscriptionsMDTRException(e);
					}
					logger.info("Se presentaron errores");
					logger.info(resp.get("P_ERROR_TEXT"));
				}
			}else{
				logger.info("/*****************************************************************************************************/");
				logger.info("/**************************TERMINA EL PROCESO DE EXECUTESUBSCRIPTIONSTOPROCESS************************/");
				logger.info("/*****************************************************************************************************/");
			}
		} catch (SubscriptionsToProcessDAOException e) {
			logger.info(e.getMessage());
			logger.error(e);
		} catch (MySQLConnectionException e) {
			logger.info(e.getMessage());
			logger.error(e);
		}
		return resp;
	}
	
	private void initVariables() {
		numberUnpaids =  0;
		numberNoUpdated =  0;
		numberUpdated =  0;
		numberCharged =  0;
		numberSent =  0;
		numberTransactionsToProcess = 0;
		swErrorOnlevel1 = false;
		errorFileExist =  false;
	}

	public synchronized boolean proccesSubscriptions() throws SubscriptionsMDTRException {
		
		logger.info("/*****************************************************************************************************/");
		logger.info("/*********************************INICIANDO EL PROCESO DE SUBSCRIPCIONES******************************/");
		logger.info("/*****************************************************************************************************/");
		
		setLogFileName(ConfigurationSystem.getKey("urlSaveErrorFilesSaveInformationSubscriptions") + processName + " - "+ Calendar.getInstance().getTime());
		loggerProcessed.info("Iniciando trazas sobre el procesamiento " + getLogFileName());
		loggerDeclined.info("Iniciando trazas sobre el procesamiento " + getLogFileName());
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
				numberTransactionsToProcess = listSubscriptionsToProcess.size();
				initialTimeThreadsCreation = Calendar.getInstance().getTimeInMillis();
				for (int position = 0; position<listSubscriptionsToProcess.size(); position++) {
					
					SubscriptionsToProcessVO subscriptionsToProcessVO = listSubscriptionsToProcess.get(position);
					
					int first = listSubscriptionsToProcess.indexOf(subscriptionsToProcessVO);
					int last = listSubscriptionsToProcess.lastIndexOf(subscriptionsToProcessVO);
					
					logger.trace(position+" "+subscriptionsToProcessVO.getSubscriptionId() + " first: " + first+"  "+subscriptionsToProcessVO.getSubscriptionId() + " last: " + last);
					
					ArrayList<SubscriptionsToProcessVO> listSubscriptionsToProcessAUX = new ArrayList<SubscriptionsToProcessVO>(listSubscriptionsToProcess.subList(first, last + 1));
					position = last;
					
					ProcessSubscription processSubscription = new ProcessSubscription(listSubscriptionsToProcessAUX, subscriptionsToProcessDAO, mySQLTransaction);
					processSubscription.setName(subscriptionsToProcessVO.getSubscriptionId());
					processSubscription.start();
					hashThreadsProcessSubscription.put(processSubscription.getName(), processSubscription);
//					
//					finalTimeIndividual = Calendar.getInstance().getTimeInMillis();
//					System.out.println("Tiempo total para procesar una subscripcion: " + (finalTimeIndividual-initialTimeIndividual) + " ms.");
				}
				finalTimeThreadsCreation = Calendar.getInstance().getTimeInMillis();
				logger.info("Todos los hilos fueron creados en "+(finalTimeThreadsCreation-initialTimeThreadsCreation) + " ms. Se llama al proceso de monitoreo de los hilos creados");
				
				MonitorProcessSubscription monitorProcessSubscription = new MonitorProcessSubscription(hashThreadsProcessSubscription);
				monitorProcessSubscription.start();
			
			} else {
				
				finalTime = Calendar.getInstance().getTimeInMillis();
				logger.info("No se encontraron subscripciones para procesar, el tiempo de ejecucion fue de " + (finalTime-initialTime) + " ms.");
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
					information.put("Total Transactions to process", numberTransactionsToProcess);
					information.put("Total Transactions no updates on Data Base", numberNoUpdated);
					information.put("Total Transactions updates on Data Base", numberUpdated);
					information.put("Total Transactions unpaids", numberUnpaids);
					information.put("Total Transactions chargeds", numberCharged);
					information.put("Total Transactions sent to our procesor", numberSent);
					information.put("Total Transactions keeped on file ", numberSent);
					informationDetails.put("Resume ProcessExecution", information);
					
					logger.info("Imprimiendo resumen de las transacciones procesadas cuando no se encuentra ninguna");
					logger.info( "Total de numberNoUpdated: " + numberNoUpdated);
					logger.info( "Total de numberUpdated: " + numberUpdated);
					logger.info( "Total de numberUnpaids: " + numberUnpaids);
					logger.info( "Total de numberCharged: " + numberCharged);
					
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
		} catch (MySQLConnectionException e) {
			logger.error(e);
			logger.info("/*****************************************************************************************************/");
			logger.info("/*********************************TERMINA EL PROCESO DE SUBSCRIPCIONES********************************/");
			logger.info("/*****************************************************************************************************/");
			throw new SubscriptionsMDTRException(e);
		} catch (SubscriptionsToProcessDAOException e) {
			logger.error(e);
			logger.info("/*****************************************************************************************************/");
			logger.info("/*********************************TERMINA EL PROCESO DE SUBSCRIPCIONES********************************/");
			logger.info("/*****************************************************************************************************/");
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.DailySubscriptionDAOException");
			throw subscriptionsMDTRException;
		} catch (MySQLTransactionException e) {
			logger.error(e);
			logger.info("/*****************************************************************************************************/");
			logger.info("/*********************************TERMINA EL PROCESO DE SUBSCRIPCIONES********************************/");
			logger.info("/*****************************************************************************************************/");
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.MySQLTransactionException");
			throw subscriptionsMDTRException;
		} catch (SubmittedProcessLogDAOException e) {
			logger.error(e);
			logger.info("/*****************************************************************************************************/");
			logger.info("/*********************************TERMINA EL PROCESO DE SUBSCRIPCIONES********************************/");
			logger.info("/*****************************************************************************************************/");
			throw new SubscriptionsMDTRException(e);
		}
		return answer;
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
		logger.info("/*****************************************************************************************************/");
		logger.info("/**********************INICIANDO PROCESO DE REPROCESAMIENTO DE ARCHIVO********************************/");
		logger.info("/*****************************************************************************************************/");
		
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
				System.out.println(linea);
				Object obj = JSONValue.parse(linea);
				JSONObject jSONObject = (JSONObject) obj;
				subscriptionsToProcessVO = new SubscriptionsToProcessVO();
				subscriptionsToProcessVO.setStatus(jSONObject.get("Supr_Status").toString());
				subscriptionsToProcessVO.setAuthorizerCode(jSONObject.get("Supr_AuthorizerCode")!= null? jSONObject.get("Supr_AuthorizerCode").toString():null);
				subscriptionsToProcessVO.setAuthorizerReason(jSONObject.get("Supr_AuthorizerReason") != null ? jSONObject.get("Supr_AuthorizerReason").toString():null);
				subscriptionsToProcessVO.setProcessAttempt(Integer.parseInt(jSONObject.get("Supr_ProcessAttempt").toString()));
				subscriptionsToProcessVO.setId(jSONObject.get("Supr_ID").toString());
				if(jSONObject.get("ReprocessTRX") != null && jSONObject.get("ReprocessTRX").toString().equalsIgnoreCase("true")){
					try {
						processed ++;
						int resp = subscriptionsToProcessDAO.update(subscriptionsToProcessVO);
						logger.debug("Reprocesando la subscripcion " + subscriptionsToProcessVO.getId()+",la respuesta obtenida es " + resp);
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
			logger.info("/*****************************************************************************************************/");
			logger.info("/**********************TERMINANDO PROCESO DE REPROCESAMIENTO DE ARCHIVO*******************************/");
			logger.info("/*****************************************************************************************************/");
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
		HashMap<String, ProcessSubscription> hashProcessSubscription = new HashMap<String, ProcessSubscription>();
		
		public MonitorProcessSubscription(HashMap<String, ProcessSubscription> hashProcessSubscription){
			this.hashProcessSubscription = hashProcessSubscription;
		}		
		
		@Override
		public void run() {
			while (!hashProcessSubscription.isEmpty()) {
					logger.info("Esperando la finalizacion de " + hashProcessSubscription.size() + " hilos ... " + "(numberSent: " + numberSent + ") (numberCharged: " +numberCharged+") (numberUnpaids: "  + numberUnpaids+")");
					try {
						this.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			finalTime = Calendar.getInstance().getTimeInMillis();
			logger.info("Termina el monitoreo de los hilos creados para procesar todas las subscripciones en " + (finalTime-initialTime) + " ms.");
		}
	}
	
	class ProcessSubscription extends Thread {
		
		private Map<String, Object> hashMapCharge = new HashMap<String, Object>();
		private ArrayList<SubscriptionsToProcessVO> listSubscriptionsToProcess;
		private SubscriptionsToProcessDAO subscriptionsToProcessDAO;
		private SubmittedProcessLogDAO submittedProcessLogDAO;
		private MySQLTransaction mySQLTransaction;
		private int level = 0;
		private boolean unpaidSubGroup = false;
		private boolean executeThread = true;
		
		public ProcessSubscription(ArrayList<SubscriptionsToProcessVO> listSubscriptionsToProcess, SubscriptionsToProcessDAO subscriptionsToProcessDAO, MySQLTransaction mySQLTransaction){
			this.listSubscriptionsToProcess = listSubscriptionsToProcess;
			this.subscriptionsToProcessDAO = subscriptionsToProcessDAO;
			this.mySQLTransaction = mySQLTransaction;
		}
		
		@Override
		public void run() {
			logger.trace("El hilo " + this.getName() + " tiene " + listSubscriptionsToProcess.size() + " elementos para procesar");
			while(executeThread){
				try {
					for (SubscriptionsToProcessVO subscriptionsToProcessVO : listSubscriptionsToProcess) {
						logger.trace("Ejecutando " + subscriptionsToProcessVO.getId() + " de la subscripcion "  + this.getName());
						hashMapCharge.put("amount", Utilities.currencyToStripe(subscriptionsToProcessVO.getAmount(), subscriptionsToProcessVO.getCurrency()));
						hashMapCharge.put("currency", subscriptionsToProcessVO.getCurrency());
						hashMapCharge.put("description", "Charge for test@example.com");
				
						Map<String, Object> hashMapCard = new HashMap<String, Object>();
						hashMapCard.put("number", subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().getNumber());
						hashMapCard.put("exp_month", subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().getExpMonth());
						hashMapCard.put("exp_year", subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().getExpYear());
						hashMapCard.put("cvc", subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().getCvv());
						hashMapCard.put("name", subscriptionsToProcessVO.getMerchantCustomerCardVO().getCardVO().getName());
						hashMapCharge.put("card", hashMapCard);
						try {
							subscriptionsToProcessVO.setStatus("Sending");
							subscriptionsToProcessVO.setAuthorizerCode(null);
							subscriptionsToProcessVO.setAuthorizerReason(null);
							if (subscriptionsToProcessDAO.update(subscriptionsToProcessVO) != 0) {
								logger.debug("XXXXXXXXXXXXXXXXXXXXXXXXXX Envia la subscripcion numero "+subscriptionsToProcessVO.getId());
								level = 1;
								try {
									sent();
									Charge charge = Charge.create(hashMapCharge);
									subscriptionsToProcessVO.setStatus("Charged");
									subscriptionsToProcessVO.setAuthorizerCode(charge.getId());
									subscriptionsToProcessVO.setAuthorizerReason(null);
									charged();
									logger.debug("La subscripcion "+subscriptionsToProcessVO.getId()+ " fue Charged.");
//									LoggerProcessed.logger.info("La subscripcion "+subscriptionsToProcessVO.getId()+ " fue Charged.");
									loggerProcessed.info("La subscripcion "+subscriptionsToProcessVO.getId()+ " fue Charged.");
								} catch (CardException e) {
									unpaid();
									unpaids = true;
									unpaidSubGroup =  true;
									subscriptionsToProcessVO.setProcessAttempt(subscriptionsToProcessVO.getProcessAttempt() + 1);
									subscriptionsToProcessVO.setStatus("Unpaid");
									subscriptionsToProcessVO.setAuthorizerCode(null);
									subscriptionsToProcessVO.setAuthorizerReason(e.getMessage());
									logger.debug("La subscripcion "+subscriptionsToProcessVO.getId()+ " fue decline.");
									loggerDeclined.info("La subscripcion "+subscriptionsToProcessVO.getId()+ " fue decline.");
								}
								subscriptionsToProcessVO.setErrorCode("1");//User esta variable para simular errores, Con este valor no actualiza
//								subscriptionsToProcessVO.setErrorCode("2");//User esta variable para simular errores, con este valor lanza una exception
								if(subscriptionsToProcessDAO.update(subscriptionsToProcessVO) == 0) {
//									System.out.println("No fue posible actualizar la informacion del pago. Suspender todo el proceso. " + "Generar informe con la informacion del error");
									JSONObject errorDetails = new JSONObject();
									errorDetails.put("Supr_Status", subscriptionsToProcessVO.getStatus());
									errorDetails.put("Supr_AuthorizerCode", subscriptionsToProcessVO.getAuthorizerCode());
									errorDetails.put("Supr_AuthorizerReason", subscriptionsToProcessVO.getAuthorizerReason());
									errorDetails.put("Supr_ProcessAttempt", subscriptionsToProcessVO.getProcessAttempt());
									errorDetails.put("Supr_ID", subscriptionsToProcessVO.getId());
									errorDetails.put("Subs_ID", subscriptionsToProcessVO.getSubscriptionId());
									errorDetails.put("AutorizationID", subscriptionsToProcessVO.getAuthorizerCode());
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
							}else{
								logger.debug("No actualiza el registro para la subscripcion "+subscriptionsToProcessVO.getId()+ ".");
							}
						} catch (SubscriptionsToProcessDAOException e) {
							logger.debug("No fue posible actualizar la informacion del pago. Suspender todo el proceso. " + "Generar informe con la informacion del error " + subscriptionsToProcessDAO.getCallString());
							JSONObject errorDetails = new JSONObject();
							errorDetails.put("Supr_Status", subscriptionsToProcessVO.getStatus());
							errorDetails.put("Supr_Status", subscriptionsToProcessVO.getStatus());
							errorDetails.put("Supr_AuthorizerCode", subscriptionsToProcessVO.getAuthorizerCode());
							errorDetails.put("Supr_AuthorizerReason", subscriptionsToProcessVO.getAuthorizerReason());
							errorDetails.put("Supr_ProcessAttempt", subscriptionsToProcessVO.getProcessAttempt());
							errorDetails.put("Supr_ID", subscriptionsToProcessVO.getId());
							errorDetails.put("Subs_ID", subscriptionsToProcessVO.getSubscriptionId());
							errorDetails.put("AutorizationID", subscriptionsToProcessVO.getAuthorizerCode());
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
							break;
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
				executeThread = false;
			}
			
			hashThreadsProcessSubscription.remove(this.getName());
			
			if (hashThreadsProcessSubscription.isEmpty()) {

				finalTime = Calendar.getInstance().getTimeInMillis();
				logger.info("A Tiempo total para procesar todas las subscripciones: " + (finalTime-initialTime) + " ms.");
				System.out.println("Tiempo total para procesar todas las subscripciones: " + (finalTime-initialTime) + " ms.");
				try {
					
					submittedProcessLogDAO = new SubmittedProcessLogDAO();
					submittedProcessLogVO.setEndTime(Calendar.getInstance().getTime().toString());
					
					if(answer) submittedProcessLogVO.setStatusProcess("Success");
					else submittedProcessLogVO.setStatusProcess("Error");
					
					JSONObject informationDetails = new JSONObject();
					
					JSONObject information = new JSONObject();
//					information.put("unpaids", unpaids);
//					if(unpaids){
//						information.put("Recomendation","Run the \"Reprocess Process\" to resend the transactions to our processor.");
//						information.put("Information", "There are subscripcions that our procesor could not charge to some card holders. The uncharges transactions information are available in the tables Reprocess_X ");
//						informationDetails.put("InformationUnpaids", information);
//						informationDetails.put("ReprocessUnpaids", unpaids);
//					}
					
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
					information.put("Total Transactions to process", numberTransactionsToProcess);
					information.put("Total Transactions no updates on Data Base", numberNoUpdated);
					information.put("Total Transactions updates on Data Base", numberUpdated);
					information.put("Total Transactions keeped on file ", numberNoUpdated);
					information.put("Total Transactions unpaids", numberUnpaids);
					information.put("Total Transactions chargeds", numberCharged);
					information.put("Total Transactions sent to our procesor", numberSent);
					informationDetails.put("Resume ProcessExecution", information);
					
					logger.info("Imprimiendo resumen FINAL ... ");
					logger.info("Total Time " + ((finalTime-initialTime) + " ms."));
					logger.info("Total Transactions to process " +  numberTransactionsToProcess);
					logger.info("Total Transactions no updates on Data Base "+ numberNoUpdated);
					logger.info("Total Transactions updates on Data Base "+ numberUpdated);
					logger.info("Total Transactions keeped on file "+ numberNoUpdated);
					logger.info("Total Transactions unpaids "+ numberUnpaids);
					logger.info("Total Transactions chargeds "+ numberCharged);
					logger.info("Total Transactions sent to our procesor "+ numberSent);
					
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
				logger.info("/*****************************************************************************************************/");
				logger.info("/*********************************TERMINA EL PROCESO DE SUBSCRIPCIONES********************************/");
				logger.info("/*****************************************************************************************************/");
			}
		}
		
		public void cancel() {
			executeThread = false;
		}
	}
	
	private synchronized void sent(){
		numberSent ++;
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

	public HashMap<String, ProcessSubscription> getHashThreadsProcessSubscription() {
		return hashThreadsProcessSubscription;
	}
	
	public void printThreads() {
		System.out.println("Imprimiendo hilos .... ");
		Set<String> set = hashThreadsProcessSubscription.keySet();
		for (String key : set) {
			ProcessSubscription processSubscription = hashThreadsProcessSubscription.get(key);
			System.out.println("Nombre hilo " + processSubscription.getName());
		}
	}
	
	public void destroyThread(String name) {
		System.out.println("Intentando cancelar hijo .... " + name);
		ProcessSubscription processSubscription = hashThreadsProcessSubscription.get(name);
		if(processSubscription != null ) processSubscription.cancel();
		else System.out.println("Hilo no encontrado ...");
	}

}
