package org.openmrs.module.erp.api.impl.odoo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.map.type.CollectionType;
import org.openmrs.module.erp.impl.odoo.LocalDateDeserializer;
import org.openmrs.module.erp.impl.odoo.LocalDateTimeDeserializer;
import org.openmrs.module.erp.impl.odoo.BaseOdooModel;

public class OdooJsonUtils {
	
	private static final ObjectMapper MAPPER;
	
	static {
		MAPPER = new ObjectMapper();
		SimpleModule module = new SimpleModule("erp_odoo", Version.unknownVersion());
		module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
		module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
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
		CollectionType javaType = MAPPER.getTypeFactory().constructCollectionType(List.class, itemClass);
		return MAPPER.convertValue(data, javaType);
	}
	
}
