package org.openmrs.module.erp.api;

import com.odoojava.api.Field;
import com.odoojava.api.FieldCollection;
import com.odoojava.api.FilterCollection;
import com.odoojava.api.ObjectAdapter;
import com.odoojava.api.OdooApiException;
import com.odoojava.api.Row;
import com.odoojava.api.RowCollection;
import com.odoojava.api.Session;
import org.apache.xmlrpc.XmlRpcException;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.impl.odoo.OdooOrderServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class OdooOrderServiceImplTest {
	
	private RowCollection getOrders() throws OdooApiException {
		
		RowCollection order = new RowCollection();
		
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
		data.put("name", "SO/001");
		data.put("amount_total", "3175.0");
		
		FieldCollection fields = new FieldCollection();
		fields.addAll(Arrays.asList(id, name, amountTotal));
		
		Row row = new Row(data, fields);
		order.add(row);
		
		return order;
	}
	
	private String[] fields = { "id", "name", "amount_total", "origin", "invoice_ids", "create_date" };
	
	@Test
	public void getOrderByIdShouldReturnOrderTest() throws Exception {
		
		// Setup
		
		TestHelper.setErpProperties();
		
		// create mocked session
		Session session = mock(Session.class);
		ObjectAdapter objectAdapter = mock(ObjectAdapter.class);
		when(objectAdapter.searchAndReadObject(any(FilterCollection.class), any(String[].class))).thenReturn(getOrders());
		when(objectAdapter.getFieldNames()).thenReturn(fields);
		when(session.getObjectAdapter(any(String.class))).thenReturn(objectAdapter);
		
		OdooOrderServiceImpl odooOrderService = new OdooOrderServiceImpl(session);
		
		// Replay
		
		Map<String, Object> orders = odooOrderService.getErpOrderById("1");
		
		// Verify
		
		Assert.assertEquals("SO/001", String.valueOf(orders.get("name")));
		Assert.assertEquals("3175.0", String.valueOf(orders.get("amount_total")));
	}
	
	@Test
	public void getInvoicesByFiltersShouldReturnInvoices() throws XmlRpcException, OdooApiException {
		// Setup
		
		TestHelper.setErpProperties();
		
		// create mocked session
		Session session = mock(Session.class);
		ObjectAdapter objectAdapter = mock(ObjectAdapter.class);
		when(objectAdapter.searchAndReadObject(any(FilterCollection.class), any(String[].class))).thenReturn(getOrders());
		when(objectAdapter.getFieldNames()).thenReturn(fields);
		when(session.getObjectAdapter(any(String.class))).thenReturn(objectAdapter);
		
		OdooOrderServiceImpl odooOrderService = new OdooOrderServiceImpl(session);
		
		Filter filter = new Filter("amount_total", ">", "100");
		
		// Replay
		
		List<Map<String, Object>> invoice = odooOrderService.getErpOrdersByFilters(Collections.singletonList(filter));
		
		// Verify
		
		Assert.assertEquals("SO/001", String.valueOf(invoice.get(0).get("name")));
		Assert.assertEquals("3175.0", String.valueOf(invoice.get(0).get("amount_total")));
	}
	
}
