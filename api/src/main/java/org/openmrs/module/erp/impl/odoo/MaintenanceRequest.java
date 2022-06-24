package org.openmrs.module.erp.impl.odoo;

public class MaintenanceRequest extends BaseNamedOdooModel {
	
	private Equipment equipment;
	
	public MaintenanceRequest(Integer id, Equipment equipment) {
		this.id = id;
		this.equipment = equipment;
	}
	
	/**
	 * Gets the equipment
	 *
	 * @return the equipment
	 */
	public Equipment getEquipment() {
		return equipment;
	}
	
}
