package org.openmrs.module.erp.web.controller;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.module.erp.api.ErpOrderService;
import org.openmrs.module.erp.api.utils.TestHelper;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class ErpOrderControllerTest extends BaseModuleWebContextSensitiveTest {
	
	@Mock
	protected ErpContext erpContext;
	
	@InjectMocks
	@Autowired
	protected ErpOrderController erpOrderController;
	
	public ErpOrderControllerTest() {
		super();
		TestHelper.createErpPropertiesFile();
	}
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		ErpOrderService erpOrderService = mock(ErpOrderService.class);
		
		Mockito.doReturn(erpOrderService).when(erpContext).getErpOrderService();
		Mockito.doReturn(Collections.singletonList(getOrder())).when(erpOrderService).getErpOrdersByFilters(any());
		
		Mockito.doReturn(getOrder()).when(erpOrderService).getErpOrderById(any());
		
	}
	
	private Map<String, Object> getOrder() {

		Map<String, Object> order = new HashMap<>();
		order.put("id", "1");
		order.put("amount_total", "3017.5");
		order.put("name", "SO/001");

		return order;
	}
	
	@Test
	public void getErpOrderByIdShouldReturnResponse() {
		
		Map<String, Object> response = (Map<String, Object>) erpOrderController.getErpOrderById("1", "full");
		
		JSONObject result = new JSONObject(response);
		
		Assert.assertEquals("SO/001", result.getString("name"));
		
	}
	
	@Test
	public void shouldReturnOrdersWithEmptyFilter() {
		
		List<Map<String, Object>> result = (List<Map<String, Object>>) erpOrderController
		        .getErpOrdersByFilters("{}", "full");
		
		Assert.assertEquals("SO/001", result.get(0).get("name"));
		
	}
	
	@Test
	public void shouldReturnOrdersWithFilter() {
		
		String filters = "{\"filters\":[{\"field\":\"id\",\"comparison\":\"=\",\"value\":\"1\"},{\"field\":\"invoice_date\",\"comparison\":\">\",\"value\":\"2019-05-19\"}]}";
		List<Map<String, Object>> result = (List<Map<String, Object>>) erpOrderController.getErpOrdersByFilters(filters,
		    "full");
		
		Assert.assertEquals("SO/001", result.get(0).get("name"));
		
	}
}
