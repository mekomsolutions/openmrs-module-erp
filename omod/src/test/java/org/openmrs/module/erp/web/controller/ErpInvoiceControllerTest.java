package org.openmrs.module.erp.web.controller;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.module.erp.api.ErpInvoiceService;
import org.openmrs.module.erp.api.TestHelper;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

public class ErpInvoiceControllerTest extends BaseModuleWebContextSensitiveTest {
	
	@Spy
	protected ErpContext erpContext;
	
	@InjectMocks
	@Autowired
	protected ErpInvoiceController erpInvoiceController;
	
	public ErpInvoiceControllerTest() {
		super();
		TestHelper.setErpProperties();
	}
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		ErpInvoiceService erpInvoiceService = mock(ErpInvoiceService.class);
		
		Mockito.doReturn(erpInvoiceService).when(erpContext).getErpInvoiceService();
		Mockito.doReturn(getInvoice()).when(erpInvoiceService).getInvoiceById(anyString());
		Mockito.doReturn(Collections.singletonList(getInvoice())).when(erpInvoiceService).getInvoicesByFilters(any());
		
	}
	
	private Map<String, Object> getInvoice() {
		Map<String, Object> order = new HashMap<>();
		order.put("partner_id", "1");
		order.put("amount_total", "3017.5");
		order.put("name", "INV/001");

		return order;
	}
	
	@Test
	public void getErpInvoiceByIdShouldReturnResponse() {
		// Replay
		JSONObject result = erpInvoiceController.getInvoiceById("1", "full");
		
		// Verify
		Assert.assertEquals("INV/001", result.getString("name"));
		
	}
	
	@Test
	public void getErpInvoicesByFiltersShouldReturnResponse() {
		// Replay
		List<JSONObject> result = erpInvoiceController.getInvoicesByFilters("{}", "full");
		
		// Verify
		Assert.assertEquals("INV/001", result.get(0).getString("name"));
		
	}
	
}
