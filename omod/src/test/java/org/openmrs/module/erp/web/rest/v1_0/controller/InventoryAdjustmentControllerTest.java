package org.openmrs.module.erp.web.rest.v1_0.controller;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertNotNull;
import static org.openmrs.module.erp.api.impl.odoo.OdooInventoryServiceImpl.MODEL_INVENTORY;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.module.erp.api.ErpInventoryService;
import org.openmrs.module.erp.api.impl.odoo.OdooClient;
import org.openmrs.module.erp.api.impl.odoo.OdooInventoryServiceImpl;
import org.openmrs.module.erp.api.impl.odoo.OdooTestUtils;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.test.Util;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;

public class InventoryAdjustmentControllerTest extends BaseErpControllerTest {
	
	@Mock
	private OdooClient mockOdooClient;
	
	@Autowired
	private ErpInventoryService service;
	
	@Before
	public void setup() throws Exception {
		Mockito.reset(mockOdooClient);
		MockitoAnnotations.initMocks(this);
		OdooInventoryServiceImpl s = (OdooInventoryServiceImpl) ((Advised) service).getTargetSource().getTarget();
		s.setOdooClient(mockOdooClient);
		Mockito.when(mockOdooClient.searchAndRead(MODEL_INVENTORY, asList("state", "=", "confirm"), null))
		        .thenReturn(OdooTestUtils.loadResource("odoo_inventory_adjustments.json"));
	}
	
	@Override
	public String getURI() {
		return "inventoryadjustment";
	}
	
	@Override
	public String getUuid() {
		return null;
	}
	
	@Override
	public long getAllCount() {
		return 3;
	}
	
	@Test
	public void shouldGetAllTheActiveInventoryAdjustments() throws Exception {
		SimpleObject result = deserialize(handle(newGetRequest(getURI())));
		
		assertNotNull(result);
		Assert.assertEquals(getAllCount(), Util.getResultsSize(result));
	}
	
}
