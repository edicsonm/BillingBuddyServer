package au.com.billingbuddy.business.objects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import au.com.billigbuddy.utils.ErrorManager;
import au.com.billingbuddy.common.objects.ConfigurationApplication;
import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.common.objects.Currency;
import au.com.billingbuddy.common.objects.Utilities;
import au.com.billingbuddy.connection.objects.MySQLTransaction;
import au.com.billingbuddy.dao.objects.ChargeDAO;
import au.com.billingbuddy.dao.objects.DailySubscriptionDAO;
import au.com.billingbuddy.dao.objects.ErrorLogDAO;
import au.com.billingbuddy.dao.objects.MerchantDocumentDAO;
import au.com.billingbuddy.dao.objects.RejectedChargeDAO;
import au.com.billingbuddy.dao.objects.SubmittedProcessLogDAO;
import au.com.billingbuddy.dao.objects.TransactionDAO;
import au.com.billingbuddy.exceptions.objects.ChargeDAOException;
import au.com.billingbuddy.exceptions.objects.DailySubscriptionDAOException;
import au.com.billingbuddy.exceptions.objects.ErrorLogDAOException;
import au.com.billingbuddy.exceptions.objects.FraudDetectionMDTRException;
import au.com.billingbuddy.exceptions.objects.MerchantDocumentDAOException;
import au.com.billingbuddy.exceptions.objects.MySQLConnectionException;
import au.com.billingbuddy.exceptions.objects.MySQLTransactionException;
import au.com.billingbuddy.exceptions.objects.ProcessorMDTRException;
import au.com.billingbuddy.exceptions.objects.RejectedChargeDAOException;
import au.com.billingbuddy.exceptions.objects.SubmittedProcessLogDAOException;
import au.com.billingbuddy.exceptions.objects.SubscriptionsMDTRException;
import au.com.billingbuddy.exceptions.objects.TransactionDAOException;
import au.com.billingbuddy.exceptions.objects.TransactionMDTRException;
import au.com.billingbuddy.vo.objects.ChargeVO;
import au.com.billingbuddy.vo.objects.DailySubscriptionVO;
import au.com.billingbuddy.vo.objects.ErrorLogVO;
import au.com.billingbuddy.vo.objects.MerchantDocumentVO;
import au.com.billingbuddy.vo.objects.RejectedChargeVO;
import au.com.billingbuddy.vo.objects.SubmittedProcessLogVO;
import au.com.billingbuddy.vo.objects.TransactionVO;

import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;


public class SubscriptionsMDTR_BK {
	
	private static SubscriptionsMDTR_BK instance = null;
	private static ConfigurationSystem configurationSystem = ConfigurationSystem.getInstance();
	private static ConfigurationApplication instanceConfigurationApplication = ConfigurationApplication.getInstance();
	
	private long initialTime;
	private long finalTime;
	
	private long initialTimeIndividual;
	private long finalTimeIndividual;
	
	private boolean writeInErrorLog = false;
	private String logFileName;
	
	public static synchronized SubscriptionsMDTR_BK getInstance() {
		if (instance == null) {
			instance = new SubscriptionsMDTR_BK();
		}
		return instance;
	}
	
	private SubscriptionsMDTR_BK() {
		Stripe.apiKey = ConfigurationSystem.getKey("apiKey");
	}
	
	public synchronized boolean proccesDailySubscriptions() throws SubscriptionsMDTRException {
		boolean answer = true;
		Map<String, Object> hashMapCharge = new HashMap<String, Object>();
		setLogFileName(ConfigurationSystem.getKey("urlSaveErrorFilesSaveInformationSubscriptions") + "ProccesDailySubscriptions - "+ Calendar.getInstance().getTime());
		MySQLTransaction mySQLTransaction = null;
		boolean unpaids =  false;
		boolean noUpdated =  false;
		boolean errorFileExist =  false;
		try {
			mySQLTransaction = new MySQLTransaction();
//			mySQLTransaction.autoCommit(false); 
//			mySQLTransaction.start();
			
			SubmittedProcessLogVO submittedProcessLogVO = new SubmittedProcessLogVO();
			submittedProcessLogVO.setProcessName("ProccesDailySubscriptions");
			submittedProcessLogVO.setStartTime(Calendar.getInstance().getTime().toString());
			submittedProcessLogVO.setStatusProcess("OnExecution");
			SubmittedProcessLogDAO submittedProcessLogDAO = new SubmittedProcessLogDAO();
			submittedProcessLogDAO.insert(submittedProcessLogVO);
			
			DailySubscriptionDAO dailySubscriptionDAO = new DailySubscriptionDAO(mySQLTransaction);
			ArrayList<DailySubscriptionVO> listDailySubscriptions = dailySubscriptionDAO.search();
			
			if(listDailySubscriptions != null) {
				initialTime = Calendar.getInstance().getTimeInMillis();
				for (Iterator<DailySubscriptionVO> iterator = listDailySubscriptions.iterator(); iterator .hasNext();) {
					initialTimeIndividual = Calendar.getInstance().getTimeInMillis();
					DailySubscriptionVO dailySubscriptionVO = iterator.next();
					
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
							//No fue posible actualizar el status del pago de la subscripcion. Se genera log con los errores.
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
					}
					finalTimeIndividual = Calendar.getInstance().getTimeInMillis();
//					System.out.println("Tiempo total para procesar una subscripcion: " + (finalTimeIndividual-initialTimeIndividual) + " ms.");
				}
				finalTime = Calendar.getInstance().getTimeInMillis();
				System.out.println("Tiempo total para procesar todas las subscripciones: " + (finalTime-initialTime) + " ms.");
			}
			
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
			mySQLTransaction.commit();
		} catch (MySQLConnectionException e) {
			throw new SubscriptionsMDTRException(e);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIException e) {
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			e.printStackTrace();
		} catch (DailySubscriptionDAOException e) {
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.DailySubscriptionDAOException");
			throw subscriptionsMDTRException;
		} catch (MySQLTransactionException e) {
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.MySQLTransactionException");
			throw subscriptionsMDTRException;
		} catch (SubmittedProcessLogDAOException e) {
			throw new SubscriptionsMDTRException(e);
		} finally{
			try {
				if(mySQLTransaction != null){
					mySQLTransaction.close();
				}
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
			}
		}
		return answer;
	}
	
	public ArrayList<DailySubscriptionVO> listDailySubscriptions() throws SubscriptionsMDTRException{
		ArrayList<DailySubscriptionVO> listDailySubscriptions = null;
		try {
			DailySubscriptionDAO dailySubscriptionDAO = new DailySubscriptionDAO();
			listDailySubscriptions = dailySubscriptionDAO.search();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.processDailySubscriptions.MySQLConnectionException");
			throw subscriptionsMDTRException;
		} catch (DailySubscriptionDAOException e) {
			e.printStackTrace();
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.processDailySubscriptions.DailySubscriptionDAOException");
			throw subscriptionsMDTRException;
		}
		return listDailySubscriptions;
	}
	
	public void writeError(JSONObject errorDetails){
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
	
	public void reprocessFile(String fileName) throws SubscriptionsMDTRException {
		MySQLTransaction mySQLTransaction = null;
		File archivo = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		DailySubscriptionVO dailySubscriptionVO = null;
		try {
			mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			DailySubscriptionDAO dailySubscriptionDAO = new DailySubscriptionDAO(mySQLTransaction);
			archivo = new File(fileName);
			fileReader = new FileReader(archivo);
			bufferedReader = new BufferedReader(fileReader);
			String linea;
			while ((linea = bufferedReader.readLine()) != null) {
				Object obj = JSONValue.parse(linea);
				JSONObject jSONObject = (JSONObject) obj;
				dailySubscriptionVO = new DailySubscriptionVO();
				dailySubscriptionVO.setStatus(jSONObject.get("Dasu_Status").toString());
				dailySubscriptionVO.setAuthorizerCode(jSONObject.get("Dasu_AuthorizerCode")!= null? jSONObject.get("Dasu_AuthorizerCode").toString():null);
				dailySubscriptionVO.setAuthorizerReason(jSONObject.get("Dasu_AuthorizerReason") != null ? jSONObject.get("Dasu_AuthorizerReason").toString():null);
				dailySubscriptionVO.setId(jSONObject.get("Dasu_ID").toString());
				try {
					System.out.println(dailySubscriptionVO.getId()+ ":" + dailySubscriptionDAO.update(dailySubscriptionVO));
				} catch (DailySubscriptionDAOException e) {
					new SubscriptionsMDTRException(e);
				}
//				System.out.println("jSONObject: " + jSONObject.get("CALL_DailySubscriptionDAO"));
			}
			mySQLTransaction.commit();
		} catch (MySQLTransactionException e1) {
			e1.printStackTrace();
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fileReader) {
					fileReader.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
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
	
	public boolean proccesDailySubscriptionsBK() throws SubscriptionsMDTRException {
		boolean answer = false;
		ChargeVO chargeVO = null;
		TransactionVO transactionVO = new TransactionVO();
		Charge charge = new Charge();
		MySQLTransaction mySQLTransaction = null;
		Map<String, Object> hashMapCharge = new HashMap<String, Object>();
		String logFileName = "ProccesDailySubscriptions - "+ Calendar.getInstance().getTime().toString();
		try {
			transactionVO.setIp(InetAddress.getLocalHost().getHostAddress());
			transactionVO.setMaxmindId("00000000");
			transactionVO.setUserAgent("LocalUserAgent");
			mySQLTransaction = new MySQLTransaction();
			mySQLTransaction.start();
			TransactionDAO transactionDAO = new TransactionDAO(mySQLTransaction);
			DailySubscriptionDAO dailySubscriptionDAO = new DailySubscriptionDAO(mySQLTransaction);
			ChargeDAO chargeDAO = new ChargeDAO(mySQLTransaction);
			RejectedChargeDAO rejectedChargeDAO = new RejectedChargeDAO(mySQLTransaction);
			ArrayList<DailySubscriptionVO> listDailySubscriptions = listDailySubscriptions();
			if(listDailySubscriptions != null) {
				for (Iterator<DailySubscriptionVO> iterator = listDailySubscriptions.iterator(); iterator .hasNext();) {
					DailySubscriptionVO dailySubscriptionVO = iterator.next();
					transactionVO.setMerchantId(dailySubscriptionVO.getMerchantId());
					transactionVO.setOrderAmount(dailySubscriptionVO.getAmount());
					transactionVO.setOrderCurrency(dailySubscriptionVO.getCurrency());
					
					try {
						if(transactionDAO.insertTransactionSubscription(transactionVO) != 0) {//Registra la transaccion
							/*Aca se incluye todo el proceso para enviar la TRX al procesador(Stripe)*/
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
							initialTime = Calendar.getInstance().getTimeInMillis();
							
							try {
								charge = Charge.create(hashMapCharge);
//								printValues(charge);
		//						printTimes(initialTime, finalTime);		
								finalTime = Calendar.getInstance().getTimeInMillis();
								chargeVO = new ChargeVO();
								chargeVO.setCardVO(dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO());
								chargeVO.setTransactionId(transactionVO.getId());
								chargeVO.setProcessTime((finalTime-initialTime) + " ms.");
								
								long initialTime = Calendar.getInstance().getTimeInMillis();
								Utilities.copyChargeToChargeVO(chargeVO,charge);
								long finalTime = Calendar.getInstance().getTimeInMillis();
		//						System.out.println("Tiempo total para copiar los elementos de Stripe: " + (finalTime-initialTime) + " ms.");
								
		//						transactionVO.setCardVO(chargeVO.getCardVO());
		//						transactionVO.setChargeVO(chargeVO);
								
		//						printValues(charge);
		//				        printTimes(initialTime, finalTime);
						        
						        /** Hasta este punto a transaccion fue procesada en el Procesador (STRIPE), se debe proceder con el proceso de registro de la 
						        informacion asociada a la transaccion en la base de datos de BillingBuddy, si alguno de los procesos siguientes falla la informacion 
						        debe ser registrada en un log por seguridad para permitir un reprocesamiento de la informacion que falló.
						        **/
						        initialTime = Calendar.getInstance().getTimeInMillis();
						        
						        System.out.println("Se optiene autorizacion numero : " + chargeVO.getStripeId());
						        
								if(chargeDAO.insert(chargeVO) != 0) {
									finalTime = Calendar.getInstance().getTimeInMillis();
		//							System.out.println("Tiempo total para copiar registrar un Charge: " + (finalTime-initialTime) + " ms.");
									dailySubscriptionVO.setStatus("Charged");
									dailySubscriptionVO.setAuthorizerCode(chargeVO.getStripeId());
									if(dailySubscriptionDAO.update(dailySubscriptionVO) == 0) {
										System.out.println(dailySubscriptionDAO.getCallString());
										JSONObject attributeDetail = new JSONObject();
										attributeDetail.put("CALL", dailySubscriptionDAO.getCallString());
										JSONObject jSONObject = new JSONObject();
										jSONObject.put("ErrorDetails", attributeDetail);
										
										ErrorLogVO errorLogVO = new ErrorLogVO();
										errorLogVO.setProcessName("DailySubscriptions");
										errorLogVO.setInformation(jSONObject.toJSONString());
										
										ErrorLogDAO errorLogDAO = new ErrorLogDAO();
										if(errorLogDAO.insert(errorLogVO) == 0){
											//Generar archivo de error
											String fileName = dailySubscriptionVO.getId() +"-"+ dailySubscriptionVO.getSubscriptionId();
											ErrorManager.logDailySubscriptionErrorFile(fileName, jSONObject);
										}
									}
									
								} else {//El cargo no pudo ser registrado pero la transaccion ya fue autorizada, 
									  //se debe registrar la informacion para reprocesar la data.
									
									JSONObject attributeDetail = new JSONObject();
									attributeDetail.put("CALL", chargeDAO.getCallString());
									attributeDetail.put("ProcesorAnswer", charge.toString());
									JSONObject jSONObject = new JSONObject();
									jSONObject.put("ErrorDescription", "Unable to registred Charge");
									jSONObject.put("ErrorDetails", attributeDetail);
									String onErrorfileName = dailySubscriptionVO.getId() +"-"+ dailySubscriptionVO.getSubscriptionId();
									manageError(jSONObject,"DailySubscriptions", onErrorfileName);
									
//									ErrorLogVO errorLogVO = new ErrorLogVO();
//									errorLogVO.setProcessName("DailySubscriptions");
//									errorLogVO.setInformation(jSONObject.toJSONString());
//									
//									ErrorLogDAO errorLogDAO = new ErrorLogDAO();
//									if(errorLogDAO.insert(errorLogVO) == 0){
//										ErrorManager.logDailySubscriptionErrorFile((dailySubscriptionVO.getId() +"-"+ dailySubscriptionVO.getSubscriptionId()), jSONObject);
//									}
								}
							} catch (CardException e) {
								//En este momento se genera una transaccion rechazada
								try {
									System.out.println("Genera una transaccion rechazada");
									RejectedChargeVO rejectedChargeVO = new RejectedChargeVO();
									//rejectedChargeVO.setTransactionId(transactionVO.getId());
									rejectedChargeVO.setAmount(Utilities.currencyToStripe(dailySubscriptionVO.getAmount(), dailySubscriptionVO.getCurrency()));
									rejectedChargeVO.setCurrency(dailySubscriptionVO.getCurrency());
									rejectedChargeVO.setCardNumber(dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getNumber());
									rejectedChargeVO.setExpYear(dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpYear());
									rejectedChargeVO.setExpMonth(dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getExpMonth());
									rejectedChargeVO.setCardHolderName(dailySubscriptionVO.getMerchantCustomerCardVO().getCardVO().getName());
									rejectedChargeVO.setFailureCode(e.getCode());
									rejectedChargeVO.setFailureMessage(e.getMessage());
									rejectedChargeDAO.insert(rejectedChargeVO);
								}catch (RejectedChargeDAOException ex) {
									new SubscriptionsMDTRException(ex, rejectedChargeDAO.getCallString());
									break;
								}
								//En este momento debe actualizar el status del registro en la tabla DailySubscription
								dailySubscriptionVO.setStatus("Unpaid");
								dailySubscriptionVO.setAuthorizerReason(e.getMessage());
								dailySubscriptionDAO.update(dailySubscriptionVO);
							}
						} else {
							//No fue posible registrar la transaccion en las tablas, el pago nunca fue enviado.
							//Se debe actualizar el status del pago de la subscripcion como no procesado por error en Proceso en BB.
							dailySubscriptionVO.setStatus("BBError");
							dailySubscriptionDAO.update(dailySubscriptionVO);
//							if(dailySubscriptionDAO.update(dailySubscriptionVO) == 0) {
//								//No fue posible actualizar el status del pago de la subscripcion. Se genera log con los errores.
//								JSONObject attributeDetails = new JSONObject();
//								attributeDetails.put("Dasu_ID", dailySubscriptionVO.getId());
//								attributeDetails.put("Subs_ID", dailySubscriptionVO.getSubscriptionId());
//								attributeDetails.put("CALL_DailySubscriptionDAO", dailySubscriptionDAO.getCallString());
//								attributeDetails.put("CALL_transactionDAO", transactionDAO.getCallString());
//														
//								JSONObject jSONObject = new JSONObject();
//								jSONObject.put("ErrorDetails", attributeDetails);
//								
//								manageError(jSONObject,"DailySubscriptions", (dailySubscriptionVO.getId() +"-"+ dailySubscriptionVO.getSubscriptionId()));
//							}
						}
					} catch (TransactionDAOException e) {
						new SubscriptionsMDTRException(e, transactionDAO.getCallString());
						break;
					} catch (DailySubscriptionDAOException e) {
						new SubscriptionsMDTRException(e, dailySubscriptionDAO.getCallString());
						break;
					} catch (ChargeDAOException e) {
						new SubscriptionsMDTRException(e, chargeDAO.getCallString());
						break;
					} catch (AuthenticationException e) {
						SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e, transactionVO.getId(), "SubscriptionsMDTR.proccesDailySubscriptions.AuthenticationException", charge.toString());
						subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.AuthenticationException");
					} catch (InvalidRequestException e) {
						SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e, transactionVO.getId(), "SubscriptionsMDTR.proccesDailySubscriptions.InvalidRequestException", charge.toString());
						subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.InvalidRequestException");
					} catch (APIConnectionException e) {
						SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e, transactionVO.getId(), "SubscriptionsMDTR.proccesDailySubscriptions.APIConnectionException", charge.toString());
						subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.APIConnectionException");
					} catch (APIException e) {
						SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e, transactionVO.getId(), "ProcessorMDTR.chargePayment.APIException", charge.toString());
						subscriptionsMDTRException.setErrorCode("TransactionMDTR.chargePayment.APIException");
					} catch (ErrorLogDAOException e) {
						SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
						subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.ErrorLogDAOException");
					} 
				}
			}
//			transactionVO.setStatus(ConfigurationApplication.getKey("failure"));
//			transactionVO.setMessage(ConfigurationApplication.getKey("TransactionFacade.2"));
//			transactionVO.setErrorCode("TransactionFacade.2");
//			initialTime = Calendar.getInstance().getTimeInMillis();
//			answer =processDailySubscriptions();
//			finalTime = Calendar.getInstance().getTimeInMillis();
			System.out.println("Tiempo definitivo en procesar todas las subscripciones: " + finalTime);
			mySQLTransaction.commit();
			answer = true;
		} catch (MySQLConnectionException e) {
			e.printStackTrace();
		} catch (MySQLTransactionException e) {
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.MySQLTransactionException");
			throw subscriptionsMDTRException;
		} catch (UnknownHostException e) {
			SubscriptionsMDTRException subscriptionsMDTRException = new SubscriptionsMDTRException(e);
			subscriptionsMDTRException.setErrorCode("SubscriptionsMDTR.proccesDailySubscriptions.UnknownHostException");
			throw subscriptionsMDTRException;
		} finally{
			try {
				if(mySQLTransaction != null){
					mySQLTransaction.close();
				}
			} catch (MySQLTransactionException e) {
				e.printStackTrace();
			}
		}
		return answer;
	}
	
	
}
