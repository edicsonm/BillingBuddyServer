package au.com.billingbuddy.vo.objects;

import java.io.Serializable;
import java.sql.Blob;

public class MerchantDocumentVO extends VO implements Serializable {

	private static final long serialVersionUID = -2970385932735589826L;
	private String id;
	private String merchantId;
	private String name;
	private String description;
	private String size;
	private Blob file;
	
	private MerchantVO merchantVO;
	
	public MerchantDocumentVO() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Blob getFile() {
		return file;
	}

	public void setFile(Blob file) {
		this.file = file;
	}

	public MerchantVO getMerchantVO() {
		return merchantVO;
	}

	public void setMerchantVO(MerchantVO merchantVO) {
		this.merchantVO = merchantVO;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
