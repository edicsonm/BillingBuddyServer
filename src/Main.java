import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;

import au.com.billingbuddy.business.objects.ProcessSubscriptionFacade;
import au.com.billingbuddy.business.objects.ProcessSubscriptionsMDTR;
import au.com.billingbuddy.exceptions.objects.SubscriptionsMDTRException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Ejecutando metodo principal ... ");
		new Main().ejecutarSubscripciones();
		System.out.println("Finalizando metodo principal ... ");
//		new Main().reprocessFile();
	}
	
	public void ejecutarSubscripciones() {
		System.out.println("Iniciando llamado a la fachada ... ");
		ProcessSubscriptionFacade instance = ProcessSubscriptionFacade.getInstance();
		HashMap<String,String> resp = instance.executeSubscriptionsToProcess();
		if(resp != null){
			Set<String> set = resp.keySet();
			for (String key : set) {
				System.out.println(key+" --> "+resp.get(key));
			}
		}
		System.out.println("Finalizando llamado a la fachada ... ");
	}
	
	public void reprocessFile() {
		try {
			JSONObject attributeDetail = new JSONObject();
			attributeDetail.put("fileName", "/BBErrors/SaveInformationSubscriptions/ProccesDailySubscriptions - Fri May 29 11:46:23 AEST 2015");
			attributeDetail.put("idSubmittedProcessLog", "355");
			ProcessSubscriptionsMDTR instance = ProcessSubscriptionsMDTR.getInstance();
			instance.reprocessFile(attributeDetail);
		} catch (SubscriptionsMDTRException e) {
			e.printStackTrace();
		}
	}
	
	
	public void ejecutarSubscripciones2() throws SubscriptionsMDTRException {
		ProcessSubscriptionsMDTR instance = ProcessSubscriptionsMDTR.getInstance();
		HashMap<String,String>  resp = instance.executeSubscriptionsToProcess();
		if(resp != null){
			System.out.println("Ejecuto executeSubscriptionsToProcess()");
			/*Set<String> set = resp.keySet();
			for (String key : set) {
				System.out.println(resp.get(key));
			}*/
			try {
				boolean respuesta = instance.proccesSubscriptions();
				if (!respuesta){//Se presento algun error
					if(instance.isWriteInErrorLog()){
						System.out.println("Se presentaron errores, la informacion se encuentra almacenada en el archivo " + instance.getLogFileName());
						System.out.println("Verifique las causas de los errores y ejecute el proceso de recuperacion de errores.");
//						processFile(instance.getLogFileName());
//						instance.reprocessFile(instance.getLogFileName());
					}
				}
			} catch (SubscriptionsMDTRException e) {
				e.printStackTrace();
			}
		}
		
	}
}
