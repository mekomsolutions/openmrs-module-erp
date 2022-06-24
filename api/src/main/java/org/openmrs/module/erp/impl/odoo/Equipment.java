package org.openmrs.module.erp.impl.odoo;

import org.codehaus.jackson.annotate.JsonProperty;

public class Equipment extends BaseNamedOdooModel {
	
	@JsonProperty("serial_no")
	private String serialNo;
	
	private String location;
	
	@JsonProperty("category_id")
	private Object[] categoryRef;
	
	/**
	 * Gets the serialNo
	 * 
	 * @return the serialNo
	 */
	public String getSerialNo() {
		return serialNo;
	}
	
	/**
	 * Gets the location
	 * 
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	
	public Integer getCategoryId() {
		return (Integer) categoryRef[0];
	}
	
	public String getCategoryName() {
		return (String) categoryRef[1];
	}
	
}
