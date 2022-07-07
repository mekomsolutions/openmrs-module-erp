package org.openmrs.module.erp.api.impl.odoo;

import static java.util.Arrays.asList;
import static java.util.Date.from;
import static org.junit.Assert.assertEquals;
import static org.openmrs.module.erp.api.impl.odoo.OdooInventoryServiceImpl.MODEL_INVENTORY;
import static org.openmrs.module.erp.impl.odoo.OdooConstants.DATE_TIME_FORMATTER;
import static org.openmrs.module.erp.impl.odoo.OdooConstants.ZONE_ID_UTC;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.module.erp.impl.odoo.InventoryAdjustment;

public class OdooInventoryServiceImplTest {
	
	@Mock
	private OdooClient mockOdooClient;
	
	private OdooInventoryServiceImpl service;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		service = new OdooInventoryServiceImpl();
		service.setOdooClient(mockOdooClient);
	}
	
	@Test
	public void getInventoryAdjustments_shouldFetchTheActiveInventoryAdjustments() throws Exception {
		Mockito.when(mockOdooClient.searchAndRead(MODEL_INVENTORY, asList("state", "=", "confirm"), null))
		        .thenReturn(OdooTestUtils.loadResource("odoo_inventory_adjustments.json"));
		
		List<InventoryAdjustment> adjustments = service.getInventoryAdjustments();
		assertEquals(3, adjustments.size());
		InventoryAdjustment annual = adjustments.get(0);
		assertEquals(1, annual.getId().intValue());
		assertEquals("Annual Inventory", annual.getName());
		assertEquals(from(LocalDateTime.parse("2021-12-31 10:00:15", DATE_TIME_FORMATTER).atZone(ZONE_ID_UTC).toInstant()),
		    annual.getDate());
		
		InventoryAdjustment firstQtr = adjustments.get(1);
		assertEquals(2, firstQtr.getId().intValue());
		assertEquals("First Quarter Inventory", firstQtr.getName());
		assertEquals(from(LocalDateTime.parse("2022-03-31 15:00:20", DATE_TIME_FORMATTER).atZone(ZONE_ID_UTC).toInstant()),
		    firstQtr.getDate());
		
		InventoryAdjustment secondQtr = adjustments.get(2);
		assertEquals(3, secondQtr.getId().intValue());
		assertEquals("Second Quarter Inventory", secondQtr.getName());
		assertEquals(from(LocalDateTime.parse("2022-06-30 16:00:20", DATE_TIME_FORMATTER).atZone(ZONE_ID_UTC).toInstant()),
		    secondQtr.getDate());
	}
}
