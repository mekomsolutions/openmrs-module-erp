package org.openmrs.module.erp.api.impl.odoo;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.ArrayType;

public class OdooTestUtils {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public static Object[] loadResource(String resourceName) throws IOException {
		String data = IOUtils.toString(OdooTestUtils.class.getClassLoader().getResourceAsStream(resourceName), "UTF-8");
		MAPPER.configure(DeserializationConfig.Feature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
		ArrayType javaType = MAPPER.getTypeFactory().constructArrayType(Object.class);
		return MAPPER.readValue(data, javaType);
	}
	
}
