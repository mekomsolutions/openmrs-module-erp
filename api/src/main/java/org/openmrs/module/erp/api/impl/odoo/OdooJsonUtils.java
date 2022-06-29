package org.openmrs.module.erp.api.impl.odoo;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.openmrs.module.erp.impl.odoo.BaseOdooModel;

public class OdooJsonUtils {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	/**
	 * Converts the specified data containing a list of odoo items of the specified type
	 * 
	 * @param data the data to convert
	 * @param itemClass the odoo item type
	 * @param <T>
	 * @return list of odoo items
	 */
	public static <T extends BaseOdooModel> List<T> convertToList(Object[] data, Class<T> itemClass) {
		CollectionType javaType = MAPPER.getTypeFactory().constructCollectionType(List.class, itemClass);
		return MAPPER.convertValue(data, javaType);
	}
	
}
