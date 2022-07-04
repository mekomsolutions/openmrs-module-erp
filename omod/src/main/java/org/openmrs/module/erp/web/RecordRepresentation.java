package org.openmrs.module.erp.web;

import java.util.*;

public class RecordRepresentation {
	
	public static final String FULL = "full";
	
	public static final String CUSTOM = "custom";
	
	private List<String> defaultAttributes;
	
	public RecordRepresentation(List<String> defaultAttributes) {
		this.defaultAttributes = defaultAttributes;
	}
	
	private static List<String> parseCustomRepresentationFields(String rep) {
		
		String[] splitRep = rep.split(":");
		String[] fields = splitRep[1].split(",");
		
		return Arrays.asList(fields);
		
	}
	
	public Map<String, Object> getRepresentedRecord(Map<String, Object> record, String rep) {
		
		List<String> fields;
		
		if (rep.startsWith(FULL))
			return record;
		else if (rep.startsWith(CUSTOM) && rep.contains(":"))
			fields = parseCustomRepresentationFields(rep);
		else
			fields = defaultAttributes;
		
		Map<String, Object> filteredRecords = new HashMap<>();
		for (Map.Entry<String, Object> recordField : record.entrySet()) {
			if (fields.contains(recordField.getKey())) {
				filteredRecords.put(recordField.getKey(), recordField.getValue());
			}
		}
		
		return filteredRecords;
	}
}
