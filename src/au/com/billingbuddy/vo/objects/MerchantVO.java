package au.com.billingbuddy.vo.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class MerchantVO extends VO implements Serializable {

	private static final long serialVersionUID = -3463623290868877516L;

	private String id;
	private String countryNumeric;
	private String name;
	
	private CountryVO countryVO;
	private MerchantConfigurationVO merchantConfigurationVO;
	private ArrayList<MerchantRestrictionVO> listMerchantRestrictionsVO;
	
	private String timeUnit;
	private String amountTransactions;
	private String numberTransactions;
	private String since;
	private String to;
	
	
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

	public MerchantConfigurationVO getMerchantConfigurationVO() {
		return merchantConfigurationVO;
	}

	public ArrayList<MerchantRestrictionVO> getListMerchantRestrictionsVO() {
		return listMerchantRestrictionsVO;
	}

	public void setMerchantConfigurationVO(
			MerchantConfigurationVO merchantConfigurationVO) {
		this.merchantConfigurationVO = merchantConfigurationVO;
	}

	public void setListMerchantRestrictionsVO(
			ArrayList<MerchantRestrictionVO> listMerchantRestrictionsVO) {
		this.listMerchantRestrictionsVO = listMerchantRestrictionsVO;
	}

	public String getAmountTransactions() {
		return amountTransactions;
	}

	public String getNumberTransactions() {
		return numberTransactions;
	}

	public String getSince() {
		return since;
	}

	public String getTo() {
		return to;
	}

	public void setAmountTransactions(String amountTransactions) {
		this.amountTransactions = amountTransactions;
	}

	public void setNumberTransactions(String numberTransactions) {
		this.numberTransactions = numberTransactions;
	}

	public void setSince(String since) {
		this.since = since;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}


}
