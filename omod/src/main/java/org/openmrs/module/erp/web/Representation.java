package org.openmrs.module.erp.web;

import jdk.nashorn.internal.objects.annotations.Constructor;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Representation {
	
	private static String FULL = "full";
	
	private static String CUSTOM = "custom";
	
	private ArrayList<String> defaultAttributes;
	
	public Representation(ArrayList<String> defaultAttributes) {
		this.defaultAttributes = defaultAttributes;
	}
	
	static private ArrayList<String> parseCustomRepresentationFields(String rep) {

		if (!rep.startsWith(CUSTOM) || !rep.contains(":"))
			return null;
		String[] splitRep = rep.split(":");
		String[] fields = splitRep[1].split(",");

		return new ArrayList<>(Arrays.asList(fields));

	}
	
	public JSONObject getRepresentedRecord(Map<String, Object> records, String rep) {
		ArrayList<String> fields;
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
