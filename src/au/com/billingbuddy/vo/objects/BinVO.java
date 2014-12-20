package au.com.billingbuddy.vo.objects;

public class BinVO {
	private String id;
	private String bin;
	private String brand;
	private String suBrand;
	private String countryCode;
	private String countryName;
	private String bankName;
	private String cardType;
	private String cardCategory;

	public String getId() {
		return id;
	}

	public String getBin() {
		return bin;
	}

	public String getBrand() {
		return brand;
	}

	public String getSuBrand() {
		return suBrand;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getBankName() {
		return bankName;
	}

	public String getCardType() {
		return cardType;
	}

	public String getCardCategory() {
		return cardCategory;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setSuBrand(String suBrand) {
		this.suBrand = suBrand;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public void setCardCategory(String cardCategory) {
		this.cardCategory = cardCategory;
	}
}
