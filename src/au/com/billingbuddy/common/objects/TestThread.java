package au.com.billingbuddy.common.objects;

public class TestThread {

	public static void main(String[] args) {
		new TestThread("uno","dos","tres");
//		SaveInformationTransactionThread R1 = new SaveInformationTransactionThread("Thread-1");
//		R1.start();
//
//		SaveInformationTransactionThread R2 = new SaveInformationTransactionThread("Thread-2");
//		R2.start();
	}
	
	public TestThread(String ... valores){
		System.out.println(valores.toString());
	}

}
