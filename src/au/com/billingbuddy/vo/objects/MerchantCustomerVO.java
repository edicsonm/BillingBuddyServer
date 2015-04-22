package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class MerchantCustomerVO extends VO implements Serializable {

	private static final long serialVersionUID = 4460131983558935397L;
	
	private String id;
	private String merchantId;
	private String customerId;
	
	private MerchantVO merchantVO;
	private CustomerVO customerVO;

	public MerchantCustomerVO() {
	}

	public MerchantCustomerVO(String userId) {
		setUserId(userId);
	}
	
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

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public MerchantVO getMerchantVO() {
		return merchantVO;
	}

	public void setMerchantVO(MerchantVO merchantVO) {
		this.merchantVO = merchantVO;
	}

	public CustomerVO getCustomerVO() {
		return customerVO;
	}

	public void setCustomerVO(CustomerVO customerVO) {
		this.customerVO = customerVO;
	}

}
