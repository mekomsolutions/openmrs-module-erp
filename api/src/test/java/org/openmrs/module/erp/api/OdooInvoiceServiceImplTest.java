package org.openmrs.module.erp.api;

import com.odoojava.api.Field;
import com.odoojava.api.FieldCollection;
import com.odoojava.api.FilterCollection;
import com.odoojava.api.ObjectAdapter;
import com.odoojava.api.OdooApiException;
import com.odoojava.api.Row;
import com.odoojava.api.RowCollection;
import com.odoojava.api.Session;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.erp.api.impl.odoo.OdooInvoiceServiceImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class OdooInvoiceServiceImplTest {
	
	private RowCollection getIvoice() throws OdooApiException {
		
		RowCollection invoice = new RowCollection();
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		Map<String, Object> fieldData = new HashMap<String, Object>();
		fieldData.put("string", "Reference Order");
		fieldData.put("type", "string");
		fieldData.put("depends", new Object());
		fieldData.put("required", false);
		fieldData.put("sortable", true);
		fieldData.put("translate", false);
		fieldData.put("manual", false);
		fieldData.put("store", true);
		fieldData.put("readonly", true);
		fieldData.put("states", 1);
		fieldData.put("company_dependent", false);
		
		Field id = new Field("id", fieldData);
		Field name = new Field("name", fieldData);
		Field amountTotal = new Field("amount_total", fieldData);
		
		data.put("id", "1");
		data.put("name", "INV/001");
		data.put("amount_total", "3175.0");
		
		FieldCollection fields = new FieldCollection();
		fields.addAll(Arrays.asList(id, name, amountTotal));
		
		Row row = new Row(data, fields);
		invoice.add(row);
		
		return invoice;
	}
	
	@Test
	public void getOrdersTest() throws Exception {
		
		// Setup
		
		TestHelper.setErpProperties();
		
		// create mocked session
		Session session = mock(Session.class);
		ObjectAdapter objectAdapter = mock(ObjectAdapter.class);
		when(objectAdapter.searchAndReadObject(any(FilterCollection.class), any(String[].class))).thenReturn(getIvoice());
		when(session.getObjectAdapter(any(String.class))).thenReturn(objectAdapter);
		
		OdooInvoiceServiceImpl odooInvoiceService = new OdooInvoiceServiceImpl(session);
		
		// Replay
		
		JSONObject invoice = odooInvoiceService.getInvoiceById("1");
		
		// Verify
		
		Assert.assertEquals(String.valueOf(invoice.get("name")), "INV/001");
		Assert.assertEquals(String.valueOf(invoice.get("amount_total")), "3175.0");
	}
	
}
