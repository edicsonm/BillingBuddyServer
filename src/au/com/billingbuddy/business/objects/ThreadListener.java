package au.com.billingbuddy.business.objects;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import au.com.billingbuddy.exceptions.objects.ThreadListenerException;

public class ThreadListener implements Runnable {

	private ServerSocket serverSocket;
	protected ExecutorService threadPool = Executors.newFixedThreadPool(100);
	
	public ThreadListener(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void run() {
		System.out.println("ThreadListener in execution, using port " + serverSocket.getLocalPort());
		while (true){
			 Socket connectionSocket = null;
			 try {
				connectionSocket = serverSocket.accept();
				threadPool.execute(new ThreadTransaction(connectionSocket));
			} catch (Exception e) {
				new ThreadListenerException(e);
			}       
		}
	}

}
