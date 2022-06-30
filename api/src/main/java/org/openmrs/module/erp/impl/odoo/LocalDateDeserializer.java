package org.openmrs.module.erp.impl.odoo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdDeserializer;

/**
 * Custom deserializer to convert odoo dates strings
 */
public class LocalDateDeserializer extends StdDeserializer<LocalDate> {
	
	public LocalDateDeserializer() {
		super(LocalDate.class);
	}
	
	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		return LocalDate.parse(p.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
	}
	
}
