package au.com.billingbuddy.business.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import au.com.billingbuddy.exceptions.objects.TransactionException;
import au.com.billingbuddy.vo.objects.ResponseVO;

public class ThreadTransaction implements Runnable {
	protected Socket connectionSocket;
	protected boolean isStripe;
	protected boolean isMaxmind;
	
	protected BufferedReader in = null;
	protected PrintWriter out = null;
	
	protected TransactionFacade transactionFacade = TransactionFacade.getInstance();
	
	public ThreadTransaction(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	public void run() {
		
	}

//	public void run() {
//		try {
//			System.out.println("Esperando informacion ....");
//			in = new BufferedReader(new InputStreamReader(this.connectionSocket.getInputStream()));
//			out = new PrintWriter(connectionSocket.getOutputStream(), true);
//
//			String context;
//			StringBuffer message = new StringBuffer("");
//			while ((context = in.readLine()) != null) {
//				if (context.equals("Q$#*_"))
//					break;
//				message.append(context);
//			}
//			System.out.println("message: " + message);
//			String messageResponse = transactionFacade.proccesMessage(message.toString());
//			out.println("Aca te envio la respuesta cliente ..." + messageResponse);
//			out.flush();
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			new TransactionException(e);
//		}
//
//	}

}
