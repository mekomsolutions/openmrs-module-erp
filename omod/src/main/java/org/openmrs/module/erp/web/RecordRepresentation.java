package org.openmrs.module.erp.web;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecordRepresentation {
	
	private static final String FULL = "full";
	
	private static final String CUSTOM = "custom";
	
	private List<String> defaultAttributes;
	
	public RecordRepresentation(List<String> defaultAttributes) {
		this.defaultAttributes = defaultAttributes;
	}
	
	static private List<String> parseCustomRepresentationFields(String rep) {

		if (!rep.startsWith(CUSTOM) || !rep.contains(":"))
			return new ArrayList<>();
		String[] splitRep = rep.split(":");
		String[] fields = splitRep[1].split(",");

		return Arrays.asList(fields);

	}
	
	public Map<String, Object> getRepresentedRecord(Map<String, Object> records, String rep) {
		List<String> fields;
		if (rep.startsWith(FULL))
			return records;
		else if (rep.startsWith(CUSTOM))
			fields = parseCustomRepresentationFields(rep);
		else
			fields = defaultAttributes;

		Map<String, Object> filteredRecords = new HashMap<>();

		for (Map.Entry<String, Object> record :
				records.entrySet()) {
			if (fields.contains(record.getKey())) {
				filteredRecords.put(record.getKey(), record.getValue());
			}
		}

		return filteredRecords;
	}
}
