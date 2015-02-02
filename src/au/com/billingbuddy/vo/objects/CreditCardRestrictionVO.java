package au.com.billingbuddy.vo.objects;

import java.io.Serializable;

public class CreditCardRestrictionVO extends VO implements Serializable {

	private static final long serialVersionUID = 6077257940709336765L;
	private String id;
	private String value;
	private String concept;
	private String timeUnit;
	
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


}
