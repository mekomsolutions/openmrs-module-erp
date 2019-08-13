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
import org.openmrs.module.erp.api.ErpPartnerService;
import org.openmrs.module.erp.api.utils.TestHelper;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class ErpPartnerControllerTest extends BaseModuleWebContextSensitiveTest {
	
	@Mock
	protected ErpContext erpContext;
	
	@InjectMocks
	@Autowired
	protected ErpPartnerController erpPartnerController;
	
	public ErpPartnerControllerTest() {
		super();
		TestHelper.createErpPropertiesFile();
	}
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		ErpPartnerService erpPartnerService = mock(ErpPartnerService.class);
		
		Mockito.doReturn(erpPartnerService).when(erpContext).getErpPartnerService();
		Mockito.doReturn(Collections.singletonList(getPartner())).when(erpPartnerService).getErpPartnersByFilters(any());
		
		Mockito.doReturn(getPartner()).when(erpPartnerService).getErpPartnerById(any());
		
	}
	
	private Map<String, Object> getPartner() {

		Map<String, Object> partner = new HashMap<>();
		partner.put("id", "1");
		partner.put("uuid", "1234-4321");
		partner.put("name", "Romain B");

		return partner;
	}
	
	@Test
	public void getErpPartnerByIdShouldReturnResponse() {
		
		Map<String, Object> response = (Map<String, Object>) erpPartnerController.getErpPartnerById("1", "full");
		
		JSONObject result = new JSONObject(response);
		
		Assert.assertEquals("Romain B", result.getString("name"));
		
	}
	
	@Test
	public void shouldReturnPartnersWithEmptyFilter() {
		
		List<Map<String, Object>> result = (List<Map<String, Object>>) erpPartnerController.getErpPartnersByFilters("{}",
		    "full");
		
		Assert.assertEquals("Romain B", result.get(0).get("name"));
		
	}
	
	@Test
	public void shouldReturnPartnersWithFilter() {
		
		String filters = "{\"filters\":[{\"field\":\"id\",\"comparison\":\"=\",\"value\":\"1\"},{\"field\":\"uuid\",\"comparison\":\"=\",\"value\":\"1234-4321\"}]}";
		List<Map<String, Object>> result = (List<Map<String, Object>>) erpPartnerController.getErpPartnersByFilters(filters,
		    "full");
		
		Assert.assertEquals("Romain B", result.get(0).get("name"));
		
	}
}
