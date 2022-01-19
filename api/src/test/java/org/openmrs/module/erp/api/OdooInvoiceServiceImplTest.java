package org.openmrs.module.erp.api;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.impl.odoo.OdooInvoiceServiceImpl;
import org.openmrs.module.erp.api.impl.odoo.OdooSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.openmrs.module.erp.api.utils.TestHelper.getOdooRecord;
import static org.powermock.api.mockito.PowerMockito.when;

public class OdooInvoiceServiceImplTest {
	
	private String[] fields = { "id", "name", "origin", "amount_total" };
	
	private OdooInvoiceServiceImpl odooInvoiceService;
	
	@Before
	public void setup() throws XmlRpcException {
		// Setup mocks

		OdooSession odooSession = mock(OdooSession.class);
		when(odooSession.execute(any(String.class), any(String.class), any(), any())).thenReturn(getOdooRecord());
		when(odooSession.getDomainFields(any())).thenReturn(new ArrayList<>(asList(fields)));

		odooInvoiceService = new OdooInvoiceServiceImpl(odooSession);
	}
	
	@Test
	public void getInvoiceByIdShouldReturnInvoice() {
		
		Map<String, Object> invoice = odooInvoiceService.getInvoiceById("1");
		
		// Verify
		Assert.assertEquals("REC/001", String.valueOf(invoice.get("name")));
		Assert.assertEquals("3175.0", String.valueOf(invoice.get("amount_total")));
	}
	
	@Test
	public void getInvoicesByFiltersShouldReturnInvoices() {
		
		Filter filter = new Filter("amount_total", ">", "100");
		
		List<Map<String, Object>> invoice = odooInvoiceService.getInvoicesByFilters(Collections.singletonList(filter));
		
		// Verify
		Assert.assertEquals("REC/001", String.valueOf(invoice.get(0).get("name")));
		Assert.assertEquals("3175.0", String.valueOf(invoice.get(0).get("amount_total")));
	}
	
	@Test
	public void getInvoiceByIdShouldReturnInvoiceLines() {
		
		Map<String, Object> invoice = odooInvoiceService.getInvoiceById("1");
		
		// Verify
		List<Map<String, Object>> invoiceLines = (List<Map<String, Object>>) invoice.get("invoice_lines");
		Assert.assertNotNull(invoiceLines);
		
		Assert.assertEquals("REC/001", invoiceLines.get(0).get("name"));
	}
	
	@Test
	public void getInvoicesByFiltersShouldReturnInvoiceLines() {
		
		Filter filter = new Filter("amount_total", ">", "100");
		
		List<Map<String, Object>> invoice = odooInvoiceService.getInvoicesByFilters(Collections.singletonList(filter));
		
		// Verify
		List<Map<String, Object>> invoiceLines = (List<Map<String, Object>>) invoice.get(0).get("invoice_lines");
		Assert.assertNotNull(invoiceLines);
		
		Assert.assertEquals("REC/001", invoiceLines.get(0).get("name"));
		
	}
	
}
