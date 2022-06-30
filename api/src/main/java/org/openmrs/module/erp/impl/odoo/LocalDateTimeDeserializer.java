package org.openmrs.module.erp.impl.odoo;

import static org.openmrs.module.erp.impl.odoo.OdooConstants.DATE_TIME_FORMATTER;
import static org.openmrs.module.erp.impl.odoo.OdooConstants.ZONE_ID_UTC;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdDeserializer;

/**
 * Custom deserializer that converts odoo datetime strings to the equivalent datetime instant of the
 * timezone of the OpenMRS server.
 */
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
	
	public LocalDateTimeDeserializer() {
		super(LocalDateTime.class);
	}
	
	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		return LocalDateTime.parse(p.getText(), DATE_TIME_FORMATTER).atZone(ZONE_ID_UTC)
		        .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
	}
	
}
