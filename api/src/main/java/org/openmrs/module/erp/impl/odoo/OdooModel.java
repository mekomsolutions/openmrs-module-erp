package org.openmrs.module.erp.impl.odoo;

/**
 * Super interface for all classes representing Odoo domain models
 */
public interface OdooModel {
	
	Integer getId();
	
	void setId(Integer id);
	
	//TODO Add more fields like create_date
	
}
