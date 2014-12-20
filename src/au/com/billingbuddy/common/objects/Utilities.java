package au.com.billingbuddy.common.objects;

import com.stripe.model.Charge;
import com.stripe.model.Dispute;

import au.com.billingbuddy.vo.objects.StripeChargeVO;

public class Utilities {
	public static boolean isNullOrEmpty(String valor) {
		if (valor == null)
			return true;
		if (valor.length() == 0)
			return true;
		else
			return false;
	}
	
	public static void copyChargeToStripeChargeVO(StripeChargeVO stripeChargeVO, Charge charge){
		
		stripeChargeVO.setAmount(String.valueOf(charge.getAmount()));
		stripeChargeVO.setAmountRefunded(String.valueOf(charge.getAmountRefunded()));
		stripeChargeVO.setBalanceTransaction(charge.getBalanceTransaction());
		stripeChargeVO.setCaptured(booleanToString(charge.getCaptured()));
		stripeChargeVO.setCurrency(charge.getCurrency());
		stripeChargeVO.setDescription(charge.getDescription());
		stripeChargeVO.setFailureCode(charge.getFailureCode());
		stripeChargeVO.setFailureMessage(charge.getFailureMessage());
		stripeChargeVO.setIdStripe(charge.getId());
		stripeChargeVO.setInvoice(charge.getInvoice());
		stripeChargeVO.setLiveMode(booleanToString(charge.getLivemode()));
		stripeChargeVO.setPaid(booleanToString(charge.getPaid()));
		stripeChargeVO.setRefunded(booleanToString(charge.getRefunded()));
		stripeChargeVO.setStatementDescription(charge.getStatementDescription());
		stripeChargeVO.setObject("");
		stripeChargeVO.setIdCard("0");
//		charge.getCard();
//		charge.getCreated();
//		charge.getCustomer();
//		charge.getDispute();
//		charge.getDisputed();
//		charge.getMetadata()
//		charge.getRefunds();
		
	}
	
	public static String booleanToString(boolean val){
		if(val)return "0";
		return "1";
	}
	
	
}
