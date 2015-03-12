package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class CountryVO extends VO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2383299434703138066L;
	private String numeric;
	private String alpha_2;
	private String alpha_3;
	private String name;
	private String ISO_3166_2;

	public String getNumeric() {
		return numeric;
	}

	public void setNumeric(String numeric) {
		this.numeric = numeric;
	}

	public String getAlpha_2() {
		return alpha_2;
	}

	public void setAlpha_2(String alpha_2) {
		this.alpha_2 = alpha_2;
	}

	public String getAlpha_3() {
		return alpha_3;
	}

	public void setAlpha_3(String alpha_3) {
		this.alpha_3 = alpha_3;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getISO_3166_2() {
		return ISO_3166_2;
	}

	public void setISO_3166_2(String iSO_3166_2) {
		ISO_3166_2 = iSO_3166_2;
	}

}
