package au.com.billingbuddy.common.objects;

import java.util.ResourceBundle;

public class ConfigurationSystem {
	
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("au.com.billingbuddy.properties.ConfigurationSystem");
	private static ConfigurationSystem instance = null;
	
	public static ConfigurationSystem getInstance() {
		if (instance == null) {
			instance = new ConfigurationSystem();
		}
		return instance;
	}
	
	public static String getKey(String key){
		return resourceBundle.getString(key);
	}
	
	private ConfigurationSystem(){
		
	} 
	
}
