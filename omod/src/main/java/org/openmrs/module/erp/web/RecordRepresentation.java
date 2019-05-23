package org.openmrs.module.erp.web;

import java.util.*;

public class RecordRepresentation {

	private static final String FULL = "full";

	private static final String CUSTOM = "custom";

	private List<String> defaultAttributes;

	private static List<String> parseCustomRepresentationFields(String rep) {

		if (!rep.startsWith(CUSTOM) || !rep.contains(":"))
			return new ArrayList<>();
		String[] splitRep = rep.split(":");
		String[] fields = splitRep[1].split(",");

		return Arrays.asList(fields);

	}

	public RecordRepresentation(List<String> defaultAttributes) {
		this.defaultAttributes = defaultAttributes;
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
