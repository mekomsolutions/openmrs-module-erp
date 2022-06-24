package org.openmrs.module.erp.impl.odoo;

/**
 * Base class for odoo model classes, subclasses should extend this class instead of directly
 * implementing OdooModel
 */
public abstract class BaseOdooModel implements OdooModel {
	
	protected Integer id;
	
	/**
	 * Gets the id
	 * 
	 * @return the id
	 */
	@Override
	public Integer getId() {
		return id;
	}
	
	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		
		if (other == null || !getClass().isAssignableFrom(other.getClass())) {
			return false;
		}
		
		if (getId() == null) {
			return false;
		}
		
		OdooModel otherObj = (OdooModel) other;
		if (getId() == null && otherObj.getId() == null) {
			return super.equals(other);
		}
		
		return getId().equals(otherObj.getId());
	}
	
	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + getId();
	}
	
}
