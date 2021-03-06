package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class MonthlySubscriptionVO extends VO implements Serializable {
	
	private static final long serialVersionUID = 6796229427633147871L;
	private String id;
	private String subscriptionId;
	private String planId;
	private String merchantCustomerCardId;
	private String quantity;
	private String amount;
	private String currentPeriodStart;
	private String currentPeriodEnd;
	private String creationTime;
	private String AuthorizerCode;
	private String AuthorizerReason;
	
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
	public String getAuthorizerCode() {
		return AuthorizerCode;
	}
	public void setAuthorizerCode(String authorizerCode) {
		AuthorizerCode = authorizerCode;
	}
	public String getAuthorizerReason() {
		return AuthorizerReason;
	}
	public void setAuthorizerReason(String authorizerReason) {
		AuthorizerReason = authorizerReason;
	}
	
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof MonthlySubscriptionVO))
			return false;
		MonthlySubscriptionVO objetoVO = (MonthlySubscriptionVO) obj;
		return (this.getId().equalsIgnoreCase(objetoVO.getId()));
    }

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
