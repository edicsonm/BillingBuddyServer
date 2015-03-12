package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class MerchantRestrictionVO extends VO implements Serializable {

	private static final long serialVersionUID = -1914870172965889812L;
	private String id;
	private String merchantId;
	private String value;
	private String concept;
	private String timeUnit;
	private MerchantVO merchantVO;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}
	public MerchantVO getMerchantVO() {
		return merchantVO;
	}
	public void setMerchantVO(MerchantVO merchantVO) {
		this.merchantVO = merchantVO;
	}

}
