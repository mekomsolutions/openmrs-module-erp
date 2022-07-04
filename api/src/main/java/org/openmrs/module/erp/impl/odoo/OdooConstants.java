package org.openmrs.module.erp.impl.odoo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class OdooConstants {
	
	public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");
	
}
