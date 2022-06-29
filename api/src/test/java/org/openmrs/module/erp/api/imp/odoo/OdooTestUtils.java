package org.openmrs.module.erp.api.imp.odoo;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.ArrayType;

public class OdooTestUtils {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public static Object[] loadResource(String resourceName) throws IOException {
		String data = IOUtils.toString(OdooTestUtils.class.getClassLoader().getResourceAsStream(resourceName), "UTF-8");
		ArrayType javaType = MAPPER.getTypeFactory().constructArrayType(Object.class);
		return MAPPER.readValue(data, javaType);
	}
	
}
