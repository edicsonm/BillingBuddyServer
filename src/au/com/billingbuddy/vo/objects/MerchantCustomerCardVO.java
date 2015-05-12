package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class MerchantCustomerCardVO extends VO implements Serializable {
	
	private static final long serialVersionUID = -4135997181401905822L;
	
	private String merchantId;
	private String customerId;
	private String cardId;
	
	private MerchantVO merchantVO;
	private CustomerVO customerVO;
	private CardVO cardVO;
	private MerchantCustomerVO merchantCustomerVO;
	
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
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
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
	public CardVO getCardVO() {
		return cardVO;
	}
	public void setCardVO(CardVO cardVO) {
		this.cardVO = cardVO;
	}
	public MerchantCustomerVO getMerchantCustomerVO() {
		return merchantCustomerVO;
	}
	public void setMerchantCustomerVO(MerchantCustomerVO merchantCustomerVO) {
		this.merchantCustomerVO = merchantCustomerVO;
	}
	
}
