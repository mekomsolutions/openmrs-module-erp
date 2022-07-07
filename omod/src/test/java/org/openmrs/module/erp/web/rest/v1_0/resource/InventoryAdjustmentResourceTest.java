/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * 
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.erp.web.rest.v1_0.resource;

import static java.util.Arrays.asList;
import static org.openmrs.module.erp.api.impl.odoo.OdooInventoryServiceImpl.INVENTORY_FETCH_FIELDS;
import static org.openmrs.module.erp.api.impl.odoo.OdooInventoryServiceImpl.MODEL_INVENTORY;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.api.impl.odoo.OdooClient;
import org.openmrs.module.erp.api.impl.odoo.OdooInventoryServiceImpl;
import org.openmrs.module.erp.api.impl.odoo.OdooTestUtils;
import org.openmrs.module.erp.impl.odoo.InventoryAdjustment;
import org.openmrs.module.erp.impl.odoo.OdooConstants;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResourceTest;

public class InventoryAdjustmentResourceTest extends BaseDelegatingResourceTest<InventoryAdjustmentResource, InventoryAdjustment> {
	
	@Mock
	private OdooClient mockOdooClient;
	
	private OdooInventoryServiceImpl service;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		service = new OdooInventoryServiceImpl();
		service.setOdooClient(mockOdooClient);
	}
	
	@Override
	public InventoryAdjustment newObject() {
		try {
			Mockito.when(
			    mockOdooClient.searchAndRead(MODEL_INVENTORY, asList("state", "=", "confirm"), INVENTORY_FETCH_FIELDS))
			        .thenReturn(OdooTestUtils.loadResource("odoo_inventory_adjustments.json"));
			
			return service.getInventoryAdjustments().get(0);
		}
		catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	@Override
	public String getDisplayProperty() {
		return "Annual Inventory";
	}
	
	@Override
	public String getUuidProperty() {
		return null;
	}
	
	private void assertProps() {
		assertPropEquals("id", getObject().getId());
		assertPropEquals("name", getObject().getName());
		assertPropEquals("date", getObject().getDate());
		assertPropEquals("endDate", OdooConstants.DATE_FORMATTER.format(getObject().getEndDate()));
		assertPropEquals("display", getObject().getName());
	}
	
	@Override
	public void validateRefRepresentation() {
		assertProps();
	}
	
	@Override
	public void validateDefaultRepresentation() {
		assertProps();
	}
	
	@Override
	public void validateFullRepresentation() {
		assertProps();
	}
}
