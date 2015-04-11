package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class ChargeVO extends VO implements Serializable {

	private static final long serialVersionUID = 8550005895002958030L;
	private String id;
	private String transactionId;
	private String stripeId;
	private String cardId;
	private String invoiceId;
	private String addressId;
	private String amount;
	private String captured;
	private String creationTime;
	private String currency;
	private String paid;
	private String refunded;
	private String failureCode;
	private String failureMessage;
	private String receiptMail;
	private String receiptNumber;
	private String processTime;
	
	private String balanceTransaction;
	private String amountRefunded;
	private String description;
	private String liveMode;
	private String invoice;
	private String statementDescription;
	private String object;
	
	private CardVO cardVO;
	private RefundVO refundVO;
	private TransactionVO transactionVO;

	public ChargeVO() {
	}
	
	public ChargeVO(String userId) {
		setUserId(userId);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCaptured() {
		return captured;
	}

	public void setCaptured(String captured) {
		this.captured = captured;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getRefunded() {
		return refunded;
	}

	public void setRefunded(String refunded) {
		this.refunded = refunded;
	}

	public String getFailureCode() {
		return failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public String getReceiptMail() {
		return receiptMail;
	}

	public void setReceiptMail(String receiptMail) {
		this.receiptMail = receiptMail;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public CardVO getCardVO() {
		return cardVO;
	}

	public void setCardVO(CardVO cardVO) {
		this.cardVO = cardVO;
	}

	public String getProcessTime() {
		return processTime;
	}

	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}

	public String getStripeId() {
		return stripeId;
	}

	public void setStripeId(String stripeId) {
		this.stripeId = stripeId;
	}

	public String getBalanceTransaction() {
		return balanceTransaction;
	}

	public void setBalanceTransaction(String balanceTransaction) {
		this.balanceTransaction = balanceTransaction;
	}

	public String getAmountRefunded() {
		return amountRefunded;
	}

	public void setAmountRefunded(String amountRefunded) {
		this.amountRefunded = amountRefunded;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLiveMode() {
		return liveMode;
	}

	public void setLiveMode(String liveMode) {
		this.liveMode = liveMode;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getStatementDescription() {
		return statementDescription;
	}

	public void setStatementDescription(String statementDescription) {
		this.statementDescription = statementDescription;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public RefundVO getRefundVO() {
		return refundVO;
	}

	public void setRefundVO(RefundVO refundVO) {
		this.refundVO = refundVO;
	}

	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof ChargeVO))
			return false;
		ChargeVO objetoVO = (ChargeVO) obj;
		return (this.getId().equalsIgnoreCase(objetoVO.getId()));
    }

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public TransactionVO getTransactionVO() {
		return transactionVO;
	}

	public void setTransactionVO(TransactionVO transactionVO) {
		this.transactionVO = transactionVO;
	}
	
}
