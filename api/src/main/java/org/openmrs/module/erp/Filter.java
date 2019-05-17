package org.openmrs.module.erp;

public class Filter {
	
	private String fieldName;
	
	private String comparison;
	
	private Object value;
	
	public String getFieldName() {
		return fieldName;
	}
	
	public String getComparison() {
		return comparison;
	}
	
	public Object getValue() {
		return value;
	}
	
	public Filter(String fieldName, String comparison, Object value) {
		this.fieldName = fieldName;
		this.comparison = comparison;
		this.value = value;
	}
	
}
