package au.com.billingbuddy.business.objects;

import au.com.billingbuddy.exceptions.objects.PaymentServerException;

public class PaymentEngine {

	public static void main(String[] args) {
		try {
			PaymentServer paymentServer = new PaymentServer();
		} catch (PaymentServerException e) {
			e.printStackTrace();
		}
	}

}
