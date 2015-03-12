package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class RejectedChargeVO extends VO  implements Serializable {

	private static final long serialVersionUID = -1805763458018531869L;
	
	private String id;
	private String transactionId;
	private String amount;
	private String currency;
	private String failureCode;
	private String failureMessage;
	private String cardNumber;
	private String expMonth;
	private String expYear;
	private String cardHolderName;
	
	public String getId() {
		return id;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public String getAmount() {
		return amount;
	}
	public String getCurrency() {
		return currency;
	}
	public String getFailureCode() {
		return failureCode;
	}
	public String getFailureMessage() {
		return failureMessage;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public String getExpMonth() {
		return expMonth;
	}
	public String getExpYear() {
		return expYear;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}
	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}
	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	
	
}
