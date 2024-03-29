package org.openmrs.module.erp.impl.odoo;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class MaintenanceRequest extends BaseNamedOdooModel {
	
	private Equipment equipment;
	
	@JsonProperty("equipment_id")
	private Object[] equipmentRef;
	
	@JsonProperty("request_date")
	private Date requestDate;
	
	@JsonProperty("schedule_date")
	private Date scheduleDate;
	
	@JsonProperty
	private Double duration;
	
	/**
	 * Gets the equipment
	 * 
	 * @return the equipment
	 */
	public Equipment getEquipment() {
		return equipment;
	}
	
	/**
	 * Gets the equipment id
	 * 
	 * @return the equipment id
	 */
	public Integer getEquipmentId() {
		return (Integer) equipmentRef[0];
	}
	
	/**
	 * Gets the equipment name
	 * 
	 * @return the equipment name
	 */
	public String getEquipmentName() {
		return (String) equipmentRef[1];
	}
	
	/**
	 * Gets the requestDate
	 * 
	 * @return the requestDate
	 */
	public Date getRequestDate() {
		return requestDate;
	}
	
	/**
	 * Gets the scheduleDate
	 * 
	 * @return the scheduleDate
	 */
	public Date getScheduleDate() {
		return scheduleDate;
	}
	
	/**
	 * Gets the duration
	 * 
	 * @return the duration
	 */
	public Double getDuration() {
		return duration;
	}
	
}
