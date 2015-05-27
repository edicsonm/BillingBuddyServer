import java.io.IOException;

import au.com.billingbuddy.business.objects.ProcessSubscriptionsMDTR;
import au.com.billingbuddy.exceptions.objects.SubscriptionsMDTRException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Ejecutando metodo principal ... ");
		new Main().ejecutarSubscripciones();
	}
	
	public void ejecutarSubscripciones(){
		ProcessSubscriptionsMDTR instance = ProcessSubscriptionsMDTR.getInstance();
		try {
			boolean respuesta = instance.proccesDailySubscriptions();
			if (!respuesta){//Se presento algun error
				if(instance.isWriteInErrorLog()){
					System.out.println("Se presentaron errores, la informacion se encuentra almacenada en el archivo " + instance.getLogFileName());
					System.out.println("Verifique las causas de los errores y ejecute el proceso de recuperacion de errores.");
//					processFile(instance.getLogFileName());
//					instance.reprocessFile(instance.getLogFileName());
				}
			}
		} catch (SubscriptionsMDTRException e) {
		}
	}
}
