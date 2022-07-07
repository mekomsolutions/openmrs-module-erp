package org.openmrs.module.erp.impl.odoo;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Super interface for all classes representing Odoo domain models
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public interface OdooModel {
	
	/**
	 * Gets the id
	 * 
	 * @return the id
	 */
	Integer getId();
	
	/**
	 * Gets the data
	 * 
	 * @return the data
	 */
	Map<String, Object> getData();
	
	/**
	 * Sets the data
	 * 
	 * @param data the data to set
	 */
	void setData(Map<String, Object> data);
	
	/**
	 * Gets the value of the field matching the specified name, this method returns the raw value as
	 * read from odoo.
	 *
	 * @param fieldName the name of the field
	 * @return the field value
	 */
	Object getValue(String fieldName);
	
	//TODO Add more fields like create_date
	
}
