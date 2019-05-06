package org.openmrs.module.erp.api.utils;

import org.openmrs.util.OpenmrsUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ErpProperties {
	
	private static Properties properties;
	
	public static void load() {
		File file = new File(OpenmrsUtil.getApplicationDataDirectory(), "erp.properties");
		if (file.exists()) {
			String propertyFile = file.getAbsolutePath();
			try {
				properties = new Properties(System.getProperties());
				properties.load(new FileInputStream(propertyFile));
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public static String getProperty(String key) {
		return properties != null ? properties.getProperty(key) : null;
	}
	
	public static void initalize(Properties props) {
		properties = props;
	}
}
