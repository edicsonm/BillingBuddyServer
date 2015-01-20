package au.com.billingbuddy.common.objects;

public class MySQLError {
	
	private String value;
	private String sqlObjectName;
	private String SQLMessage;
	
	public MySQLError() {}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSqlObjectName() {
		return sqlObjectName;
	}

	public void setSqlObjectName(String sqlObjectName) {
		this.sqlObjectName = sqlObjectName;
	}

	public String getSQLMessage() {
		return SQLMessage;
	}

	public void setSQLMessage(String sQLMessage) {
		SQLMessage = sQLMessage;
	}

}
