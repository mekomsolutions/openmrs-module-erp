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
import org.openmrs.module.erp.api.utils.TestHelper;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import org.openmrs.module.erp.api.ErpProductService;

public class ErpProductControllerTest extends BaseModuleWebContextSensitiveTest {
	
	@Mock
	protected ErpContext erpContext;
	
	@InjectMocks
	@Autowired
	protected ErpProductController erpProductController;
	
	public ErpProductControllerTest() {
		super();
		TestHelper.createErpPropertiesFile();
	}
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		ErpProductService erpProductService = mock(ErpProductService.class);
		
		Mockito.doReturn(erpProductService).when(erpContext).getErpProductService();
		Mockito.doReturn(Collections.singletonList(getProduct())).when(erpProductService).getErpProductByFilters(any());
		
		Mockito.doReturn(getProduct()).when(erpProductService).getErpProductById(any());
		
	}
	
	private Map<String, Object> getProduct() {

		Map<String, Object> product = new HashMap<>();
		product.put("id", "1");
		product.put("name", "medA");
		product.put("actual_stock", "5");
		product.put("price", "100");

		return product;
	}
	
	@Test
	public void getErpProductByIdShouldReturnResponse() {
		
		Map<String, Object> response = (Map<String, Object>) erpProductController.getErpProductById("1", "full");
		
		JSONObject result = new JSONObject(response);
		
		Assert.assertEquals("medA", result.getString("name"));
		
	}
	
	@Test
	public void shouldReturnPartnersWithEmptyFilter() {
		
		List<Map<String, Object>> result = (List<Map<String, Object>>) erpProductController.getErpProductByFilters("{}",
		    "full");
		
		Assert.assertEquals("medA", result.get(0).get("name"));
		
	}
	
	@Test
	public void shouldReturnPartnersWithFilter() {
		
		String filters = "{\"filters\":[{\"field\":\"id\",\"comparison\":\"=\",\"value\":\"1\"},{\"field\":\"actual_stock\",\"comparison\":\">\",\"value\":\"4\"}]}";
		List<Map<String, Object>> result = (List<Map<String, Object>>) erpProductController.getErpProductByFilters(filters,
		    "full");
		
		Assert.assertEquals("medA", result.get(0).get("name"));
		
	}
}
