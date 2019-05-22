package org.openmrs.module.erp.web.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.module.erp.api.ErpOrderService;
import org.openmrs.module.erp.api.TestHelper;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

public class ErpOrderControllerTest extends BaseModuleWebContextSensitiveTest {
	
	@Spy
	protected ErpContext erpContext;
	
	@InjectMocks
	@Autowired
	protected ErpOrderController erpOrderController;
	
	public ErpOrderControllerTest() {
		super();
		TestHelper.setErpProperties();
	}
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		ErpOrderService erpOrderService = mock(ErpOrderService.class);
		
		Mockito.doReturn(erpOrderService).when(erpContext).getErpOrderService();
		Mockito.doReturn(Collections.singletonList(get_order())).when(erpOrderService).getErpOrdersByFilters(any());
		Mockito.doReturn(get_order()).when(erpOrderService).getErpOrderById(any());
		
	}
	
	private Map<String, Object> get_order() {

		Map<String, Object> order = new HashMap<>();
		order.put("id", "1");
		order.put("amount_total", "3017.5");
		order.put("name", "SO/001");

		return order;
	}
	
	@Test
	public void getErpOrderByIdShouldReturnResponse() {
		
		// Replay
		Map<String, Object> response = (Map<String, Object>) erpOrderController.getErpOrderById("1", "full");
		
		JSONObject result = new JSONObject(response);
		
		//		// Verify
		Assert.assertEquals("SO/001", result.getString("name"));
		
	}
	
	@Test
	public void getErpOrdersByFiltersShouldReturnOrders() {
		
		// Replay
		List<Map<String, Object>> result = (List<Map<String, Object>>) erpOrderController
		        .getErpOrdersByFilters("{}", "full");
		
		// Verify
		Assert.assertEquals("SO/001", result.get(0).get("name"));
		
	}
	
}
