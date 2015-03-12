package au.com.billingbuddy.vo.objects;

import au.com.billingbuddy.common.objects.ValidateData;
import au.com.billingbuddy.exceptions.objects.DataSanitizeException;

public class ShippingAddressVO {

	private String address;
	private String city;
	private String region;
	private String postal;
	private String country;

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getRegion() {
		return region;
	}

	public String getPostal() {
		return postal;
	}

	public String getCountry() {
		return country;
	}

	public void setAddress(String address) throws DataSanitizeException {
//		ValidateData.validate(address, ValidateData.STRING);
		this.address = address;
	}

	public void setCity(String city) throws DataSanitizeException {
		ValidateData.validate(city, ValidateData.NAME);
		this.city = city;
	}

	public void setRegion(String region) throws DataSanitizeException {
		ValidateData.validate(region, ValidateData.NAME);
		this.region = region;
	}

	public void setPostal(String postal) throws DataSanitizeException {
		ValidateData.validate(postal, ValidateData.DECIMALNUMERIC);
		this.postal = postal;
	}

	public void setCountry(String country) throws DataSanitizeException {
		ValidateData.validate(country, ValidateData.NAME);
		this.country = country;
	}

}
