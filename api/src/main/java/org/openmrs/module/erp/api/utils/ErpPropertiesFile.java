package org.openmrs.module.erp.api.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.exceptions.ErpPropertyNotFoundException;
import org.openmrs.util.OpenmrsUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ErpPropertiesFile {
	
	private ErpPropertiesFile() {
	}
	
	public static File getFile() {
		String fileName = ErpConstants.ERP_PROPERTY_FILE_NAME;
		File file = new File(OpenmrsUtil.getApplicationDataDirectory(), fileName);
		if (file.exists()) {
			return file;
		}
		LogFactory.getLog(ErpPropertiesFile.class).warn("Could not find erp.properties file in directory: " + file.getParent() + ". Failing over to location /etc/properties/.");
		return new File("/etc/properties/", fileName);
	}
	
	public static InputStream getInputStream() throws FileNotFoundException {
		File file = getFile();
		return new FileInputStream(file);
	}
	
	public static Properties getProperties(InputStream inStream, Map<String, Class> expectedProperties) throws IOException {
		
		Log log = LogFactory.getLog(ErpPropertiesFile.class);
		
		Properties props = new Properties();
		props.load(inStream);
		
		for (Map.Entry property : expectedProperties.entrySet()) {
			if (props.getProperty(String.valueOf(property.getKey())) != null) {
				if (property.getValue() == Integer.class) {
					try {
						Integer.parseInt(props.getProperty(String.valueOf(property.getKey())));
					}
					catch (NumberFormatException e) {
						log.error("Unable to convert property \"" + property.getKey() + "\" into type "
						        + property.getValue().toString());
						throw e;
					}
				}
			} else
				throw new ErpPropertyNotFoundException();
		}
		return props;
	}
	
}
