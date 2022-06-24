package org.openmrs.module.erp.impl.odoo;

/**
 * Base class for odoo model classes with a name
 */
public abstract class BaseNamedOdooModel extends BaseOdooModel {
	
	protected String name;
	
	/**
	 * Gets the name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
