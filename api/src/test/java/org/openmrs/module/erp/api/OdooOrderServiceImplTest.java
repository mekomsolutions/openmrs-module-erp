package org.openmrs.module.erp.api;

import com.odoojava.api.FilterCollection;
import com.odoojava.api.ObjectAdapter;
import com.odoojava.api.OdooApiException;
import com.odoojava.api.Session;
import org.apache.xmlrpc.XmlRpcException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.impl.odoo.OdooOrderServiceImpl;
import org.openmrs.module.erp.api.impl.odoo.OdooSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.openmrs.module.erp.api.utils.TestHelper.getOdooRecord;
import static org.powermock.api.mockito.PowerMockito.when;

public class OdooOrderServiceImplTest {
	
	private String[] fields = { "id", "name", "amount_total", "origin", "order_ids", "create_date" };
	
	private ErpOrderService odooOrderService;
	
	@Before
	public void setup() throws XmlRpcException, OdooApiException {
		// Setup mocks
		Session session = mock(Session.class);
		ObjectAdapter objectAdapter = mock(ObjectAdapter.class);
		when(objectAdapter.searchAndReadObject(any(FilterCollection.class), any(String[].class)))
		        .thenReturn(getOdooRecord());
		when(objectAdapter.getFieldNames()).thenReturn(fields);
		when(session.getObjectAdapter(any(String.class))).thenReturn(objectAdapter);
		
		OdooSession odooSession = mock(OdooSession.class);
		when(odooSession.getSession()).thenReturn(session);
		
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
