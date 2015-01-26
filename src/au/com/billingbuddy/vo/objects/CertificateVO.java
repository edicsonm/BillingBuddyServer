package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class CertificateVO extends VO implements Serializable {

	private static final long serialVersionUID = 5453993659224427175L;
	private String merchantId;
	private String commonName;
	private String organization;
	private String organizationUnit;
	private String country;
	private String passwordKeyStore;
	private String passwordKeyStoreConfirmation;
	private String privacyKeyStore;
	private String privacyKeyStoreConfirmation;
	private String passwordkey;
	private String passwordkeyConfirmation;
	private String keyName;
	private String aliasMerchant;
	
	private StringBuffer log;
	
	public String getMerchantId() {
		return merchantId;
	}

	public String getCommonName() {
		return commonName;
	}

	public String getOrganization() {
		return organization;
	}

	public String getOrganizationUnit() {
		return organizationUnit;
	}

	public String getCountry() {
		return country;
	}

	public String getPasswordKeyStore() {
		return passwordKeyStore;
	}

	public String getPasswordKeyStoreConfirmation() {
		return passwordKeyStoreConfirmation;
	}

	public String getPrivacyKeyStore() {
		return privacyKeyStore;
	}

	public String getPrivacyKeyStoreConfirmation() {
		return privacyKeyStoreConfirmation;
	}

	public String getPasswordkey() {
		return passwordkey;
	}

	public String getPasswordkeyConfirmation() {
		return passwordkeyConfirmation;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public void setOrganizationUnit(String organizationUnit) {
		this.organizationUnit = organizationUnit;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setPasswordKeyStore(String passwordKeyStore) {
		this.passwordKeyStore = passwordKeyStore;
	}

	public void setPasswordKeyStoreConfirmation(
			String passwordKeyStoreConfirmation) {
		this.passwordKeyStoreConfirmation = passwordKeyStoreConfirmation;
	}

	public void setPrivacyKeyStore(String privacyKeyStore) {
		this.privacyKeyStore = privacyKeyStore;
	}

	public void setPrivacyKeyStoreConfirmation(
			String privacyKeyStoreConfirmation) {
		this.privacyKeyStoreConfirmation = privacyKeyStoreConfirmation;
	}

	public void setPasswordkey(String passwordkey) {
		this.passwordkey = passwordkey;
	}

	public void setPasswordkeyConfirmation(String passwordkeyConfirmation) {
		this.passwordkeyConfirmation = passwordkeyConfirmation;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public StringBuffer getLog() {
		return log;
	}

	public void setLog(StringBuffer log) {
		this.log = log;
	}

	public String getAliasMerchant() {
		return aliasMerchant;
	}

	public void setAliasMerchant(String aliasMerchant) {
		this.aliasMerchant = aliasMerchant;
	}


}
