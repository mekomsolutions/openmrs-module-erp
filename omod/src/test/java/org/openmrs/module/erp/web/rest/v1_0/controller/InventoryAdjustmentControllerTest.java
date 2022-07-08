package org.openmrs.module.erp.web.rest.v1_0.controller;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.openmrs.module.erp.api.impl.odoo.OdooInventoryServiceImpl.MODEL_INVENTORY;
import static org.openmrs.module.erp.web.rest.ErpRestConstants.REPRESENTATION_ODOO_PREFIX;

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
		assertEquals(getAllCount(), Util.getResultsSize(result));
	}
	
	@Test
	public void shouldGetAllTheActiveInventoryAdjustmentsAsTheSpecifiedCustomRepresentation() throws Exception {
		SimpleObject result = deserialize(handle(newGetRequest(getURI(), new Parameter("v", "custom:(id,name)"))));
		
		assertNotNull(result);
		assertEquals(getAllCount(), Util.getResultsSize(result));
		assertEquals(1, Util.getByPath(result, "results[0]/id"));
		assertEquals("Annual Inventory", Util.getByPath(result, "results[0]/name"));
		assertNull(Util.getByPath(result, "results[0]/other_field"));
		assertNull(Util.getByPath(result, "results[0]/date"));
		assertEquals(2, Util.getByPath(result, "results[1]/id"));
		assertEquals("First Quarter Inventory", Util.getByPath(result, "results[1]/name"));
		assertNull(Util.getByPath(result, "results[1]/other_field"));
		assertNull(Util.getByPath(result, "results[1]/date"));
		assertEquals(3, Util.getByPath(result, "results[2]/id"));
		assertEquals("Second Quarter Inventory", Util.getByPath(result, "results[2]/name"));
		assertNull(Util.getByPath(result, "results[2]/other_field"));
		assertNull(Util.getByPath(result, "results[2]/date"));
	}
	
	@Test
	public void shouldGetAllTheActiveInventoryAdjustmentsAsTheOdooRepresentation() throws Exception {
		SimpleObject result = deserialize(
		    handle(newGetRequest(getURI(), new Parameter("v", REPRESENTATION_ODOO_PREFIX + "name,other_field"))));
		
		assertNotNull(result);
		assertEquals(getAllCount(), Util.getResultsSize(result));
		assertNull(Util.getByPath(result, "results[0]/id"));
		assertEquals("Annual Inventory", Util.getByPath(result, "results[0]/name"));
		assertEquals("other1", Util.getByPath(result, "results[0]/other_field"));
		assertNull(Util.getByPath(result, "results[0]/date"));
		assertNull(Util.getByPath(result, "results[1]/id"));
		assertEquals("First Quarter Inventory", Util.getByPath(result, "results[1]/name"));
		assertEquals("other2", Util.getByPath(result, "results[1]/other_field"));
		assertNull(Util.getByPath(result, "results[1]/date"));
		assertNull(Util.getByPath(result, "results[2]/id"));
		assertEquals("Second Quarter Inventory", Util.getByPath(result, "results[2]/name"));
		assertNull(Util.getByPath(result, "results[2]/other_field"));
		assertNull(Util.getByPath(result, "results[2]/date"));
	}
	
}
