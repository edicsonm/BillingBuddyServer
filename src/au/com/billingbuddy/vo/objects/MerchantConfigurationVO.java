package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class MerchantConfigurationVO extends VO implements Serializable {

	private static final long serialVersionUID = -6627663553261307009L;

	private String id;
	private String merchantId;
	private String urlApproved;
	private String urlDeny;

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

	public String getUrlApproved() {
		return urlApproved;
	}

	public void setUrlApproved(String urlApproved) {
		this.urlApproved = urlApproved;
	}

	public String getUrlDeny() {
		return urlDeny;
	}

	public void setUrlDeny(String urlDeny) {
		this.urlDeny = urlDeny;
	}

}
