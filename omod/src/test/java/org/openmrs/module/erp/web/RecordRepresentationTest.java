package org.openmrs.module.erp.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class RecordRepresentationTest {
	
	private String amountTotal = "amount_total";
	
	private String name = "name";
	
	private String invoiceIds = "invoice_ids";
	
	private ArrayList<String> invoiceDefaultAttributes;
	
	private RecordRepresentation recordRepresentation;
	
	private Map record1;
	
	@Before
	public void setup() {
		
		invoiceDefaultAttributes = new ArrayList<String>(Arrays.asList(amountTotal, name));
		recordRepresentation = new RecordRepresentation(invoiceDefaultAttributes);
		
		record1 = new HashMap();
		record1.put(amountTotal, "1234");
		record1.put(invoiceIds, "1,2,3");
		record1.put(name, "testRecord");
	}
	
	@Test
	public void shouldGetFullyRepresentedRecord() {
		Map<String, Object> representedRecord = recordRepresentation.getRepresentedRecord(record1, "full");
		
		Assert.assertTrue(representedRecord.containsKey(amountTotal));
		Assert.assertTrue(representedRecord.containsKey(invoiceIds));
		Assert.assertTrue(representedRecord.containsKey(name));
	}
	
	@Test
	public void shouldGetDefaultRepresentedRecord() {
		Map<String, Object> representedRecord = recordRepresentation.getRepresentedRecord(record1, "");
		
		Assert.assertTrue(representedRecord.containsKey(amountTotal));
		Assert.assertFalse(representedRecord.containsKey(invoiceIds));
		Assert.assertTrue(representedRecord.containsKey(name));
	}
	
	@Test
	public void shouldGetCustomRepresentedRecord() {
		String customRep = "custom:invoice_ids,name";
		Map<String, Object> representedRecord = recordRepresentation.getRepresentedRecord(record1, customRep);
		
		Assert.assertFalse(representedRecord.containsKey(amountTotal));
		Assert.assertTrue(representedRecord.containsKey(invoiceIds));
		Assert.assertTrue(representedRecord.containsKey(name));
	}
}
