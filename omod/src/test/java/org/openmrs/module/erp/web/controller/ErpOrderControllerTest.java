package org.openmrs.module.erp.web.controller;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.module.erp.api.ErpOrderService;
import org.openmrs.module.erp.api.TestHelper;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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
	
	@BeforeMethod
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		
	}
	
	@BeforeMethod
	public void setup() {
		ErpOrderService erpOrderService = mock(ErpOrderService.class);
		
		Mockito.doReturn(erpOrderService).when(erpContext).getErpOrderService();
		Mockito.doReturn(get_orders()).when(erpOrderService).getErpOrdersByPatientUuid(anyString());
		
	}
	
	private ArrayList<JSONObject> get_orders() {
		ArrayList<JSONObject> orders = new ArrayList<JSONObject>();
		JSONObject order = new JSONObject();
		order.put("partner_uuid", "4237cc1e-11d6-4036-9674-5716b883340e");
		order.put("amount_total", "3017.5");
		order.put("name", "SO/001");
		orders.add(order);
		
		return orders;
	}
	
	@Test
	public void getErpOrdersByPatientUuidShouldReturnOrders() {
		
		// Replay
		ArrayList<JSONObject> result = erpOrderController.getErpOrdersByPatientUuid("4237cc1e-11d6-4036-9674-5716b883340e");
		
		// Verify
		Assert.assertEquals(result.get(0).getString("name"), "SO/001");
		
	}
	
}
