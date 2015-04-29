package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class SubscriptionVO extends VO implements Serializable {

	private static final long serialVersionUID = -3649491090597337979L;
	private String id;
	private String planId;
	private String customerId;
	private String discountId;
	private String cancelAtPeriodEnd;
	private String quantity;
	private String start;
	private String status;
	private String applicationFeePercent;
	private String canceledAt;
	private String currentPeriodStart;
	private String currentPeriodEnd;
	private String trialEnd;
	private String endedAt;
	private String trialStart;
	private String taxPercent;
	private String creationTime;

	private PlanVO planVO;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDiscountId() {
		return discountId;
	}

	public void setDiscountId(String discountId) {
		this.discountId = discountId;
	}

	public String getCancelAtPeriodEnd() {
		return cancelAtPeriodEnd;
	}

	public void setCancelAtPeriodEnd(String cancelAtPeriodEnd) {
		this.cancelAtPeriodEnd = cancelAtPeriodEnd;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApplicationFeePercent() {
		return applicationFeePercent;
	}

	public void setApplicationFeePercent(String applicationFeePercent) {
		this.applicationFeePercent = applicationFeePercent;
	}

	public String getCanceledAt() {
		return canceledAt;
	}

	public void setCanceledAt(String canceledAt) {
		this.canceledAt = canceledAt;
	}

	public String getCurrentPeriodStart() {
		return currentPeriodStart;
	}

	public void setCurrentPeriodStart(String currentPeriodStart) {
		this.currentPeriodStart = currentPeriodStart;
	}

	public String getTrialEnd() {
		return trialEnd;
	}

	public void setTrialEnd(String trialEnd) {
		this.trialEnd = trialEnd;
	}

	public String getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(String endedAt) {
		this.endedAt = endedAt;
	}

	public String getTrialStart() {
		return trialStart;
	}

	public void setTrialStart(String trialStart) {
		this.trialStart = trialStart;
	}

	public String getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(String taxPercent) {
		this.taxPercent = taxPercent;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getCurrentPeriodEnd() {
		return currentPeriodEnd;
	}

	public void setCurrentPeriodEnd(String currentPeriodEnd) {
		this.currentPeriodEnd = currentPeriodEnd;
	}

	public PlanVO getPlanVO() {
		return planVO;
	}

	public void setPlanVO(PlanVO planVO) {
		this.planVO = planVO;
	}
	
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof SubscriptionVO))
			return false;
		SubscriptionVO objetoVO = (SubscriptionVO) obj;
		return (this.getId().equalsIgnoreCase(objetoVO.getId()));
    }

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
