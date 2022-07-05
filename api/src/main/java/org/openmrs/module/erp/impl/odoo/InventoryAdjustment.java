package org.openmrs.module.erp.impl.odoo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class InventoryAdjustment extends BaseNamedOdooModel {
	
	private Date date;
	
	@JsonProperty("end_date")
	private Date endDate;
	
	/**
	 * Gets the date
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Gets the endDate
	 * 
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	
}
