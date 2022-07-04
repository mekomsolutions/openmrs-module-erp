package org.openmrs.module.erp.impl.odoo;

import static org.openmrs.module.erp.impl.odoo.OdooConstants.DATE_FORMATTER;
import static org.openmrs.module.erp.impl.odoo.OdooConstants.DATE_TIME_FORMATTER;
import static org.openmrs.module.erp.impl.odoo.OdooConstants.ZONE_ID_UTC;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdDeserializer;

/**
 * Custom deserializer that converts odoo date and datetime strings to a Date Object, datetime
 * values are considered to be in UTC since it is the timezone used by odoo.
 */
public class DateDeserializer extends StdDeserializer<Date> {
	
	public DateDeserializer() {
		super(Date.class);
	}
	
	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		if (p.getText().length() == 10) {
			try {
				return DATE_FORMATTER.parse(p.getText());
			}
			catch (ParseException e) {
				throw new IOException(e);
			}
		}
		
		return Date.from(LocalDateTime.parse(p.getText(), DATE_TIME_FORMATTER).atZone(ZONE_ID_UTC).toInstant());
	}
	
}
