package org.openmrs.module.erp.web.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.module.erp.api.ErpInvoiceService;
import org.openmrs.module.erp.api.utils.TestHelper;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

public class ErpInvoiceControllerTest extends BaseModuleWebContextSensitiveTest {
	
	@Mock
	protected ErpInvoiceService erpInvoiceService;
	
	@InjectMocks
	@Autowired
	protected ErpInvoiceController erpInvoiceController;
	
	public ErpInvoiceControllerTest() {
		super();
		TestHelper.createErpPropertiesFile();
	}
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		
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
		Object result = erpInvoiceController.getInvoiceById("1", "full");
		
		// Verify
		Assert.assertEquals("INV/001", ((Map<String, Object>) result).get("name"));
		
	}
	
	@Test
	public void getErpInvoicesByFiltersShouldReturnResponse() {
		// Replay
		List<Map<String, Object>> response = (List<Map<String, Object>>) erpInvoiceController.getInvoicesByFilters("{}",
		    "full");
		
		// Verify
		Assert.assertEquals("INV/001", response.get(0).get("name"));
		
	}
	
}
