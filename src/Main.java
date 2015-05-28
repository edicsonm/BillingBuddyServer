import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import au.com.billingbuddy.business.objects.ProcessSubscriptionFacade;
import au.com.billingbuddy.business.objects.ProcessSubscriptionsMDTR;
import au.com.billingbuddy.exceptions.objects.SubscriptionsMDTRException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Ejecutando metodo principal ... ");
		new Main().ejecutarSubscripciones();
	}
	
	public void ejecutarSubscripciones() {
		ProcessSubscriptionFacade instance = ProcessSubscriptionFacade.getInstance();
		instance.executeSubscriptionsToProcess();
	}
	
	public void ejecutarSubscripciones2() {
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
