package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class PlanVO extends VO implements Serializable {
	
	private static final long serialVersionUID = 491976966806876498L;

	private String id;
	private String merchantId;
	private String amount;
	private String creationTime;
	private String currency;
	private String interval;
	private String intervalCount;
	private String name;
	private String trialPeriodDays;
	private String statementDescriptor;
	
	private MerchantVO merchantVO;
	
	public PlanVO(){}
	
	public PlanVO(String id){
		this.id= id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getIntervalCount() {
		return intervalCount;
	}

	public void setIntervalCount(String intervalCount) {
		this.intervalCount = intervalCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTrialPeriodDays() {
		return trialPeriodDays;
	}

	public void setTrialPeriodDays(String trialPeriodDays) {
		this.trialPeriodDays = trialPeriodDays;
	}

	public String getStatementDescriptor() {
		return statementDescriptor;
	}

	public void setStatementDescriptor(String statementDescriptor) {
		this.statementDescriptor = statementDescriptor;
	}
	
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	
	public MerchantVO getMerchantVO() {
		return merchantVO;
	}

	public void setMerchantVO(MerchantVO merchantVO) {
		this.merchantVO = merchantVO;
	}
	
	@Override
    public boolean equals(Object obj) {
		if (!(obj instanceof PlanVO))
			return false;
		PlanVO objetoVO = (PlanVO) obj;
		return (this.getId().equalsIgnoreCase(objetoVO.getId()));
    }

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
