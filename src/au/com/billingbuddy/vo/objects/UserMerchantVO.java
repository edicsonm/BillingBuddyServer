package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class UserMerchantVO extends VO implements Serializable {

	private static final long serialVersionUID = 1115144993794843756L;

	private String userId;
	private String merchantId;

	private MerchantVO merchantVO;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
}
