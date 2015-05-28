package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class SubscriptionsToProcessVO extends VO implements Serializable {
	
	private static final long serialVersionUID = -2661663659313510412L;
	private String id;
	private String subscriptionId;
	private String planId;
	private String merchantId;
	private String merchantCustomerCardId;
	private String quantity;
	private String amount;
	private String currency;
	private int processAttempt;
	private String currentPeriodStart;
	private String currentPeriodEnd;
	private String creationTime;
	private String authorizerCode;
	private String authorizerReason;
	
	private MerchantCustomerCardVO merchantCustomerCardVO;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getMerchantCustomerCardId() {
		return merchantCustomerCardId;
	}
	public void setMerchantCustomerCardId(String merchantCustomerCardId) {
		this.merchantCustomerCardId = merchantCustomerCardId;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrentPeriodStart() {
		return currentPeriodStart;
	}
	public void setCurrentPeriodStart(String currentPeriodStart) {
		this.currentPeriodStart = currentPeriodStart;
	}
	public String getCurrentPeriodEnd() {
		return currentPeriodEnd;
	}
	public void setCurrentPeriodEnd(String currentPeriodEnd) {
		this.currentPeriodEnd = currentPeriodEnd;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	
	/*@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof DailySubscriptionVO))
			return false;
		DailySubscriptionVO objetoVO = (DailySubscriptionVO) obj;
		return (this.getId().equalsIgnoreCase(objetoVO.getId()));
    }*/
	
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof SubscriptionsToProcessVO))
			return false;
		SubscriptionsToProcessVO objetoVO = (SubscriptionsToProcessVO) obj;
		return (this.getSubscriptionId().equalsIgnoreCase(objetoVO.getSubscriptionId()));
    }

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	public MerchantCustomerCardVO getMerchantCustomerCardVO() {
		return merchantCustomerCardVO;
	}
	public void setMerchantCustomerCardVO(
			MerchantCustomerCardVO merchantCustomerCardVO) {
		this.merchantCustomerCardVO = merchantCustomerCardVO;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getAuthorizerCode() {
		return authorizerCode;
	}
	public void setAuthorizerCode(String authorizerCode) {
		this.authorizerCode = authorizerCode;
	}
	public String getAuthorizerReason() {
		return authorizerReason;
	}
	public void setAuthorizerReason(String authorizerReason) {
		this.authorizerReason = authorizerReason;
	}
	public int getProcessAttempt() {
		return processAttempt;
	}
	public void setProcessAttempt(int processAttempt) {
		this.processAttempt = processAttempt;
	}

}
