package org.openmrs.module.erp.impl.odoo;

public class MaintenanceRequest extends BaseNamedOdooModel {
	
	private Equipment equipment;
	
	/**
	 * Gets the equipment
	 *
	 * @return the equipment
	 */
	public Equipment getEquipment() {
		return equipment;
	}
	
	/**
	 * Sets the equipment
	 *
	 * @param equipment the equipment to set
	 */
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
	
}
