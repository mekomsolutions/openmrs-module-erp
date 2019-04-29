package org.openmrs.module.erp.api.utils;

import com.odoojava.api.Session;

public class ErpConnection {
	
	private String erpHost() {
		String property = ErpProperties.getProperty("erp.host");
		return property;
	}
	
	private int erpPort() {
		String property = ErpProperties.getProperty("erp.port");
		return Integer.valueOf(property);
	}
	
	private String erpDatabase() {
		String property = ErpProperties.getProperty("erp.database");
		return property;
	}
	
	private String erpUser() {
		String property = ErpProperties.getProperty("erp.user");
		return property;
	}
	
	private String erpPassword() {
		String property = ErpProperties.getProperty("erp.password");
		return property;
	}
	
	public Session getERPSession() {
		return new Session(erpHost(), erpPort(), erpDatabase(), erpUser(), erpPassword());
	}
	
	public ErpConnection() {
		ErpProperties.load();
		
	}
	
}
