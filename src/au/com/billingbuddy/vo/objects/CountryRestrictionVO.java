package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class CountryRestrictionVO extends VO implements Serializable {

	private static final long serialVersionUID = -4782303987896264328L;
	private String id;
	private String countryNumeric;
	private String value;
	private String concept;
	private String timeUnit;
	private CountryVO countryVO;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	public CountryVO getCountryVO() {
		return countryVO;
	}

	public void setCountryVO(CountryVO countryVO) {
		this.countryVO = countryVO;
	}

	public String getCountryNumeric() {
		return countryNumeric;
	}

	public void setCountryNumeric(String countryNumeric) {
		this.countryNumeric = countryNumeric;
	}


}
