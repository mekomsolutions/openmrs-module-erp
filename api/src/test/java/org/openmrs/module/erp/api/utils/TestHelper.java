package org.openmrs.module.erp.api.utils;

import com.odoojava.api.*;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
	
	public static RowCollection getOdooRecord() throws OdooApiException {
		
		RowCollection order = new RowCollection();
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		Map<String, Object> fieldData = new HashMap<String, Object>();
		fieldData.put("string", "Reference Order");
		fieldData.put("type", "string");
		fieldData.put("depends", new Object());
		fieldData.put("required", false);
		fieldData.put("sortable", true);
		fieldData.put("translate", false);
		fieldData.put("manual", false);
		fieldData.put("store", true);
		fieldData.put("readonly", true);
		fieldData.put("states", 1);
		fieldData.put("company_dependent", false);
		
		Field id = new Field("id", fieldData);
		Field name = new Field("name", fieldData);
		Field amountTotal = new Field("amount_total", fieldData);
		
		data.put("id", "1");
		data.put("name", "REC/001");
		data.put("amount_total", "3175.0");
		data.put("actual_stock", "5");
		data.put("uuid", "a-b-c");
		
		FieldCollection fields = new FieldCollection();
		fields.addAll(Arrays.asList(id, name, amountTotal));
		
		Row row = new Row(data, fields);
		order.add(row);
		
		return order;
	}
	
	public void init() throws IOException {
		erpActivator.started();
	}
}
