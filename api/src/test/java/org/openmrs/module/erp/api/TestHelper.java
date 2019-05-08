package org.openmrs.module.erp.api;

import java.io.IOException;
import java.util.Properties;

import org.openmrs.module.erp.ErpActivator;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.module.erp.api.utils.ErpProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TestHelper {
	
	@Autowired
	private ErpActivator erpActivator;
	
	@Autowired
	@Qualifier(ErpConstants.COMPONENT_ERP_CONTEXT)
	protected ErpContext context;
	
	public static void setErpProperties() {
		Properties erpProperties = new Properties();
		erpProperties.put("erp.host", "localhost");
		erpProperties.put("erp.port", "8069");
		erpProperties.put("erp.database", "odoo");
		erpProperties.put("erp.user", "admin");
		erpProperties.put("erp.password", "admin");
		ErpProperties.initialize(erpProperties);
		
	}
	
	public void init() throws IOException {
		erpActivator.started();
	}
}
