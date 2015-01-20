package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class MerchantConfigurationVO extends VO implements Serializable {

	private static final long serialVersionUID = -6627663553261307009L;

	private String id;
	private String merchantId;
	private String urlApproved;
	private String urlDeny;
	private String passwordKeyStore;
	private String privacyKeyStore;
	private String Passwordkey;
	private String keyName;

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

	public String getPasswordKeyStore() {
		return passwordKeyStore;
	}

	public void setPasswordKeyStore(String passwordKeyStore) {
		this.passwordKeyStore = passwordKeyStore;
	}

	public String getPrivacyKeyStore() {
		return privacyKeyStore;
	}

	public void setPrivacyKeyStore(String privacyKeyStore) {
		this.privacyKeyStore = privacyKeyStore;
	}

	public String getPasswordkey() {
		return Passwordkey;
	}

	public void setPasswordkey(String passwordkey) {
		Passwordkey = passwordkey;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

}
