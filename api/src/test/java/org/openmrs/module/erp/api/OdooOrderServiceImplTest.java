package org.openmrs.module.erp.api;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.impl.odoo.OdooOrderServiceImpl;
import org.openmrs.module.erp.api.impl.odoo.OdooClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.openmrs.module.erp.api.utils.TestHelper.getOdooRecord;
import static org.powermock.api.mockito.PowerMockito.when;

public class OdooOrderServiceImplTest {
	
	private String[] fields = { "id", "name", "amount_total", "origin", "order_ids", "create_date" };
	
	private ErpOrderService odooOrderService;
	
	@Before
	public void setup() throws XmlRpcException {
		// Setup mocks
		
		OdooClient odooSession = mock(OdooClient.class);
		when(odooSession.getUid()).thenReturn("1");
		when(odooSession.execute(any(String.class), any(String.class), any(), any())).thenReturn(getOdooRecord());
		when(odooSession.getDomainFields(any())).thenReturn(new ArrayList<>(asList(fields)));
		
		odooOrderService = new OdooOrderServiceImpl(odooSession);
	}
	
	@Test
	public void getOrderByIdShouldReturnOrderTest() {
		Map<String, Object> orders = odooOrderService.getErpOrderById("1");
		
		// Verify
		Assert.assertEquals("REC/001", String.valueOf(orders.get("name")));
		Assert.assertEquals("3175.0", String.valueOf(orders.get("amount_total")));
	}
	
	@Test
	public void getOrdersByFiltersShouldReturnOrders() {
		
		Filter filter = new Filter("amount_total", ">", "100");
		
		List<Map<String, Object>> orders = odooOrderService.getErpOrdersByFilters(Collections.singletonList(filter));
		
		// Verify
		Assert.assertEquals("REC/001", String.valueOf(orders.get(0).get("name")));
		Assert.assertEquals("3175.0", String.valueOf(orders.get(0).get("amount_total")));
	}
	
	@Test
	public void getOrderByIdShouldReturnOrderLines() {
		
		Map<String, Object> order = odooOrderService.getErpOrderById("1");
		
		// Verify
		List<Map<String, Object>> orderLines = (List<Map<String, Object>>) order.get("order_lines");
		Assert.assertNotNull(orderLines);
		
		Assert.assertEquals("REC/001", orderLines.get(0).get("name"));
	}
	
	@Test
	public void getOrdersByFiltersShouldReturnOrderLines() {
		
		Filter filter = new Filter("amount_total", ">", "100");
		
		List<Map<String, Object>> orders = odooOrderService.getErpOrdersByFilters(Collections.singletonList(filter));
		
		// Verify
		List<Map<String, Object>> orderLines = (List<Map<String, Object>>) orders.get(0).get("order_lines");
		Assert.assertNotNull(orderLines);
		
		Assert.assertEquals("REC/001", orderLines.get(0).get("name"));
	}
	
}
