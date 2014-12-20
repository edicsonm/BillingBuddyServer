package au.com.billingbuddy.business.objects;

import java.net.ServerSocket;

import au.com.billingbuddy.common.objects.ConfigurationSystem;
import au.com.billingbuddy.exceptions.objects.PaymentServerException;

public class PaymentServer {
	
//	protected String restrictionCountries;
	protected ServerSocket serverSocket;
	protected ServerSocket adminSocket;
	protected ThreadListener threadlistener;
	protected ThreadAdministration threadAdministration;
	
	PaymentServer() throws PaymentServerException {
		try {
			
//			restrictionCountries = ConfigurationSystem.getKey("restrictionCountries");
			serverSocket = new ServerSocket(Integer.parseInt(ConfigurationSystem.getKey("serverPort")));
			adminSocket = new ServerSocket(Integer.parseInt(ConfigurationSystem.getKey("adminPort")));
			
			threadlistener = new ThreadListener(this.serverSocket);
			threadAdministration = new ThreadAdministration(adminSocket, serverSocket);
			
			new Thread(threadlistener).start();
			new Thread(threadAdministration).start();
			
		} catch (Exception e) {
			throw new PaymentServerException(e);
		}
	}
	
	
}
