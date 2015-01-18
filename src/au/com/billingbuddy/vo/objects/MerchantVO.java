package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class MerchantVO extends VO implements Serializable {

	private static final long serialVersionUID = -3463623290868877516L;

	private String id;
	private String countryNumeric;
	private String name;
	
	private String urlApproved;
	private String urlDeny;
	
	private CountryVO countryVO;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountryNumeric() {
		return countryNumeric;
	}

	public void setCountryNumeric(String countryNumeric) {
		this.countryNumeric = countryNumeric;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CountryVO getCountryVO() {
		return countryVO;
	}

	public void setCountryVO(CountryVO countryVO) {
		this.countryVO = countryVO;
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
