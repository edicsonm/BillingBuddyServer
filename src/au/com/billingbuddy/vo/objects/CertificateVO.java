package au.com.billingbuddy.vo.objects;

import java.io.File;
import java.io.Serializable;
import java.sql.Blob;

public class CertificateVO extends VO implements Serializable {

	private static final long serialVersionUID = 5453993659224427175L;
	private String id;
	private String merchantId;
	private String commonName;
	private String organization;
	private String organizationUnit;
	private String country;
	private String passwordKeyStore;
	private String passwordkey;
	private String keyName;
	private String aliasMerchant;
	private String creationTime;
	private String expirationTime;
	private Blob BBKeyStore;
	private Blob MerchantKeyStore;
	private File fileKeyStoreMerchant;
	private File fileKeyStoreBB;
	private String infoCertificateBB;
	private String infoCertificateMerchant;
	
	private String passwordBBKeyStore;
	private String passwordBBKey;
	private String aliasBB;
	
	private StringBuffer log;
	
	private MerchantVO merchantVO;
	
	public CertificateVO() {
	}
	
	public CertificateVO(String userId) {
		setUserId(userId);
	}
	
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

	public String getPasswordkey() {
		return passwordkey;
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

	public void setPasswordkey(String passwordkey) {
		this.passwordkey = passwordkey;
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

	public String getExpirationTime() {
		return expirationTime;
	}

	public Blob getBBKeyStore() {
		return BBKeyStore;
	}

	public Blob getMerchantKeyStore() {
		return MerchantKeyStore;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

	public void setBBKeyStore(Blob bBKeyStore) {
		BBKeyStore = bBKeyStore;
	}

	public void setMerchantKeyStore(Blob merchantKeyStore) {
		MerchantKeyStore = merchantKeyStore;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public File getFileKeyStoreMerchant() {
		return fileKeyStoreMerchant;
	}

	public File getFileKeyStoreBB() {
		return fileKeyStoreBB;
	}

	public void setFileKeyStoreMerchant(File fileKeyStoreMerchant) {
		this.fileKeyStoreMerchant = fileKeyStoreMerchant;
	}

	public void setFileKeyStoreBB(File fileKeyStoreBB) {
		this.fileKeyStoreBB = fileKeyStoreBB;
	}

	public String getInfoCertificateBB() {
		return infoCertificateBB;
	}

	public String getInfoCertificateMerchant() {
		return infoCertificateMerchant;
	}

	public void setInfoCertificateBB(String infoCertificateBB) {
		this.infoCertificateBB = infoCertificateBB;
	}

	public void setInfoCertificateMerchant(String infoCertificateMerchant) {
		this.infoCertificateMerchant = infoCertificateMerchant;
	}

	public MerchantVO getMerchantVO() {
		return merchantVO;
	}

	public void setMerchantVO(MerchantVO merchantVO) {
		this.merchantVO = merchantVO;
	}

	public String getPasswordBBKeyStore() {
		return passwordBBKeyStore;
	}

	public void setPasswordBBKeyStore(String passwordBBKeyStore) {
		this.passwordBBKeyStore = passwordBBKeyStore;
	}

	public String getPasswordBBKey() {
		return passwordBBKey;
	}

	public void setPasswordBBKey(String passwordBBKey) {
		this.passwordBBKey = passwordBBKey;
	}

	public String getAliasBB() {
		return aliasBB;
	}

	public void setAliasBB(String aliasBB) {
		this.aliasBB = aliasBB;
	}

}
