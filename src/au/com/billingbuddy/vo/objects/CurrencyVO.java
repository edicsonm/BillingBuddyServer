package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class CurrencyVO extends VO implements Serializable {
	
	private static final long serialVersionUID = 4154325801107518886L;
	private String numericCode;
	private String countryNumeric;
	private String countryName;
	private String name;
	private String alphabeticCode;
	private String MinorUnit;
	
	public String getNumericCode() {
		return numericCode;
	}
	public void setNumericCode(String numericCode) {
		this.numericCode = numericCode;
	}
	public String getCountryNumeric() {
		return countryNumeric;
	}
	public void setCountryNumeric(String countryNumeric) {
		this.countryNumeric = countryNumeric;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlphabeticCode() {
		return alphabeticCode;
	}
	public void setAlphabeticCode(String alphabeticCode) {
		this.alphabeticCode = alphabeticCode;
	}
	public String getMinorUnit() {
		return MinorUnit;
	}
	public void setMinorUnit(String minorUnit) {
		MinorUnit = minorUnit;
	}
}
