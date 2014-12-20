package au.com.billingbuddy.vo.objects;

public class StripeChargeVO {
	
	private String id;
	private String idStripe;
	private String object;
	private String created;
	private String liveMode;
	private String paid;
	private String amount;
	private String currency;
	private String refunded;
	private String captured;
	private String balanceTransaction;
	private String failureMessage;
	private String failureCode;
	private String amountRefunded;
	private String idCustomer;
	private String invoice;
	private String description;
	private String idDispute;
	private String dispute;
	private String metadata;
	private String statementDescription;
	private String receiptEmail;
	private String receiptNumber;
	private String shipping;
	private String idCard;

	private CustomerVO customerVO;
	private CardVO cardVO;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdStripe() {
		return idStripe;
	}

	public void setIdStripe(String idStripe) {
		this.idStripe = idStripe;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getLiveMode() {
		return liveMode;
	}

	public void setLiveMode(String liveMode) {
		this.liveMode = liveMode;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getRefunded() {
		return refunded;
	}

	public void setRefunded(String refunded) {
		this.refunded = refunded;
	}

	public String getCaptured() {
		return captured;
	}

	public void setCaptured(String captured) {
		this.captured = captured;
	}

	public String getBalanceTransaction() {
		return balanceTransaction;
	}

	public void setBalanceTransaction(String balanceTransaction) {
		this.balanceTransaction = balanceTransaction;
	}

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public String getFailureCode() {
		return failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	public String getAmountRefunded() {
		return amountRefunded;
	}

	public void setAmountRefunded(String amountRefunded) {
		this.amountRefunded = amountRefunded;
	}

	public String getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdDispute() {
		return idDispute;
	}

	public void setIdDispute(String idDispute) {
		this.idDispute = idDispute;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getStatementDescription() {
		return statementDescription;
	}

	public void setStatementDescription(String statementDescription) {
		this.statementDescription = statementDescription;
	}

	public String getReceiptEmail() {
		return receiptEmail;
	}

	public void setReceiptEmail(String receiptEmail) {
		this.receiptEmail = receiptEmail;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public String getShipping() {
		return shipping;
	}

	public void setShipping(String shipping) {
		this.shipping = shipping;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public CustomerVO getCustomerVO() {
		return customerVO;
	}

	public void setCustomerVO(CustomerVO customerVO) {
		this.customerVO = customerVO;
	}

	public CardVO getCardVO() {
		return cardVO;
	}

	public void setCardVO(CardVO cardVO) {
		this.cardVO = cardVO;
	}

	public String getDispute() {
		return dispute;
	}

	public void setDispute(String dispute) {
		this.dispute = dispute;
	}

}
