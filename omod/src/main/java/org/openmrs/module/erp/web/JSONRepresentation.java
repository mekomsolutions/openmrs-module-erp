package org.openmrs.module.erp.web;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JSONRepresentation {
	
	private static final String FULL = "full";
	
	private static final String CUSTOM = "custom";
	
	private List<String> defaultAttributes;
	
	public JSONRepresentation(List<String> defaultAttributes) {
		this.defaultAttributes = defaultAttributes;
	}
	
	static private List<String> parseCustomRepresentationFields(String rep) {
		
		if (!rep.startsWith(CUSTOM) || !rep.contains(":"))
			return new ArrayList<>();
		String[] splitRep = rep.split(":");
		String[] fields = splitRep[1].split(",");
		
		return Arrays.asList(fields);
		
	}
	
	public JSONObject getRepresentedRecord(Map<String, Object> records, String rep) {
		List<String> fields;
		if (rep.startsWith(FULL))
			return new JSONObject(records);
		else if (rep.startsWith(CUSTOM))
			fields = parseCustomRepresentationFields(rep);
		else
			fields = defaultAttributes;

		Map<String, Object> filteredRecords = records.entrySet().stream()
				.filter(x -> fields.contains(x.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return new JSONObject(filteredRecords);
	}
}
