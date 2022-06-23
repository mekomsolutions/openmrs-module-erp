package org.openmrs.module.erp.impl.odoo;

/**
 * Base class for odoo model classes with a name
 */
public abstract class BaseNamedOdooModel extends BaseOdooModel {
	
	private String name;
	
	/**
	 * Gets the name
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
