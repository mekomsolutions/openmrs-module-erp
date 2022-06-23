package org.openmrs.module.erp.impl.odoo;

public class Equipment extends BaseNamedOdooModel {
	
	private Category category;
	
	private String serialNo;
	
	private String location;
	
	/**
	 * Gets the category
	 *
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}
	
	/**
	 * Sets the category
	 *
	 * @param category the category to set
	 */
	public void setCategory(org.openmrs.module.erp.impl.odoo.Category category) {
		this.category = category;
	}
	
	/**
	 * Gets the serialNo
	 *
	 * @return the serialNo
	 */
	public String getSerialNo() {
		return serialNo;
	}
	
	/**
	 * Sets the serialNo
	 *
	 * @param serialNo the serialNo to set
	 */
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	/**
	 * Gets the location
	 *
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Sets the location
	 *
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
}
