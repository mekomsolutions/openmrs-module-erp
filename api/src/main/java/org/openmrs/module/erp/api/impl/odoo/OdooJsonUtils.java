package org.openmrs.module.erp.api.impl.odoo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.type.CollectionType;
import org.openmrs.module.erp.impl.odoo.BaseOdooModel;
import org.openmrs.module.erp.impl.odoo.DateDeserializer;

public class OdooJsonUtils {
	
	private static final ObjectMapper MAPPER;
	
	static {
		MAPPER = new ObjectMapper();
		SimpleModule module = new SimpleModule("odooModule", Version.unknownVersion());
		module.addDeserializer(Date.class, new DateDeserializer());
		MAPPER.registerModule(module);
	}
	
	/**
	 * Converts the specified data containing a list of odoo items of the specified type
	 * 
	 * @param data the data to convert
	 * @param itemClass the odoo item type
	 * @param <T>
	 * @return list of odoo items
	 */
	public static <T extends BaseOdooModel> List<T> convertToList(Object[] data, Class<T> itemClass) {
		//Pre-process the raw data from odoo to replace values set to false with null
		//TODO Find a better way to deal with this so that for boolean values we preserve them
		for (Object o : data) {
			Map<String, Object> item = (Map) o;
			for (Map.Entry<String, Object> entry : item.entrySet()) {
				Object value = convertToNullIfFalse(entry.getValue());
				if (value != null && value.getClass().isArray()) {
					Object[] oldElements = (Object[]) value;
					List newElements = new ArrayList(oldElements.length);
					for (Object arrayItem : oldElements) {
						newElements.add(convertToNullIfFalse(arrayItem));
					}
					value = newElements;
				}
				
				item.put(entry.getKey(), value);
			}
		}
		
		CollectionType javaType = MAPPER.getTypeFactory().constructCollectionType(List.class, itemClass);
		return MAPPER.convertValue(data, javaType);
	}
	
	private static Object convertToNullIfFalse(Object o) {
		if (o != null && "false".equalsIgnoreCase(o.toString())) {
			return null;
		}
		
		return o;
	}
	
}
