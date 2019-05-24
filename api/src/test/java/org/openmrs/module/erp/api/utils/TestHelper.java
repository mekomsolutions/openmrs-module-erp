package org.openmrs.module.erp.api.utils;

import org.openmrs.module.erp.ErpActivator;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

@Component
public class TestHelper {
	
	private static String fileName = "erp.properties";
	
	@Autowired
	private ErpActivator erpActivator;
	
	@Autowired
	@Qualifier(ErpConstants.COMPONENT_ERP_CONTEXT)
	protected ErpContext context;
	
	public static void createErpPropertiesFile() {
		File erpPropsFile = new File(OpenmrsUtil.getApplicationDataDirectory(), fileName);
		try {
			OutputStream outputStream = new FileOutputStream(erpPropsFile);
			Properties props = setTestErpProps();
			props.store(outputStream, null);
			outputStream.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void removeErpPropertiesFile() {
		File erpPropsFile = new File(OpenmrsUtil.getApplicationDataDirectory(), fileName);
		erpPropsFile.delete();
	}
	
	public static Properties setTestErpProps() {
		Properties erpProperties = new Properties();
		erpProperties.put("erp.host", "localhost");
		erpProperties.put("erp.port", "8069");
		erpProperties.put("erp.database", "odoo");
		erpProperties.put("erp.user", "admin");
		erpProperties.put("erp.password", "admin");
		return erpProperties;
	}
	
	public void init() throws IOException {
		erpActivator.started();
	}
}
