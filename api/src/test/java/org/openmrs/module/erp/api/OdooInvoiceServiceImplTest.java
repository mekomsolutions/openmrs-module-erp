package org.openmrs.module.erp.api;

import com.odoojava.api.*;
import org.apache.xmlrpc.XmlRpcException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.impl.odoo.OdooInvoiceServiceImpl;
import org.openmrs.module.erp.api.utils.ErpConnection;

import java.io.IOException;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class OdooInvoiceServiceImplTest {

	private String[] fields = { "id", "name", "origin", "amount_total" };

	private OdooInvoiceServiceImpl odooInvoiceService;

	@Before
	public void setup() throws XmlRpcException, OdooApiException {
		// Setup mocks
		Session session = mock(Session.class);
		ObjectAdapter objectAdapter = mock(ObjectAdapter.class);
		when(objectAdapter.searchAndReadObject(any(FilterCollection.class), any(String[].class))).thenReturn(getInvoice());
		when(objectAdapter.getFieldNames()).thenReturn(fields);
		when(session.getObjectAdapter(any(String.class))).thenReturn(objectAdapter);

		ErpConnection erpConnection = mock(ErpConnection.class);
		when(erpConnection.getSession()).thenReturn(session);

		odooInvoiceService = new OdooInvoiceServiceImpl(erpConnection);
	}

	@Test
	public void getInvoiceByIdShouldReturnInvoice() throws Exception {

		Map<String, Object> invoice = odooInvoiceService.getInvoiceById("1");

		// Verify
		Assert.assertEquals("INV/001", String.valueOf(invoice.get("name")));
		Assert.assertEquals("3175.0", String.valueOf(invoice.get("amount_total")));
	}

	@Test
	public void getInvoicesByFiltersShouldReturnInvoices() throws XmlRpcException, OdooApiException, IOException {

		Filter filter = new Filter("amount_total", ">", "100");

		List<Map<String, Object>> invoice = odooInvoiceService.getInvoicesByFilters(Collections.singletonList(filter));

		// Verify
		Assert.assertEquals("INV/001", String.valueOf(invoice.get(0).get("name")));
		Assert.assertEquals("3175.0", String.valueOf(invoice.get(0).get("amount_total")));
	}

	private RowCollection getInvoice() throws OdooApiException {

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

}
