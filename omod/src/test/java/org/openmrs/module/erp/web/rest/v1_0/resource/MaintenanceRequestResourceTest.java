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
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.EQUIPMENT_FETCH_FIELDS;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.MODEL_EQUIPMENT;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.MODEL_REQUEST;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.MODEL_STAGE;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.api.impl.odoo.OdooClient;
import org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl;
import org.openmrs.module.erp.api.impl.odoo.OdooTestUtils;
import org.openmrs.module.erp.impl.odoo.Equipment;
import org.openmrs.module.erp.impl.odoo.MaintenanceRequest;
import org.openmrs.module.erp.impl.odoo.OdooConstants;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingResourceTest;

public class MaintenanceRequestResourceTest extends BaseDelegatingResourceTest<MaintenanceRequestResource, MaintenanceRequest> {
	
	@Mock
	private OdooClient mockOdooClient;
	
	private OdooMaintenanceServiceImpl service;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		service = new OdooMaintenanceServiceImpl();
		service.setOdooClient(mockOdooClient);
	}
	
	private Object[] getStageIds() throws IOException {
		return OdooTestUtils.loadResource("odoo_maintenance_stages.json");
	}
	
	@Override
	public MaintenanceRequest newObject() {
		try {
			Object[] stageIds = getStageIds();
			Mockito.when(mockOdooClient.search(MODEL_STAGE, asList("done", "=", false))).thenReturn(stageIds);
			
			Object[] requestData = OdooTestUtils.loadResource("odoo_maintenance_requests.json");
			Mockito.when(mockOdooClient.searchAndRead(MODEL_REQUEST, asList("stage_id", "in", stageIds), null))
			        .thenReturn(requestData);
			
			List<Map> activeRequests = Arrays.stream(requestData).map(r -> (Map) r).collect(Collectors.toList());
			Object[] equipmentData = OdooTestUtils.loadResource("odoo_maintenance_equipment.json");
			List<Integer> equipmentIds = activeRequests.stream().map(r -> (Integer) ((Object[]) r.get("equipment_id"))[0])
			        .collect(Collectors.toList());
			Mockito.when(
			    mockOdooClient.searchAndRead(MODEL_EQUIPMENT, asList("id", "in", equipmentIds), EQUIPMENT_FETCH_FIELDS))
			        .thenReturn(equipmentData);
			
			return service.getMaintenanceRequests().get(0);
		}
		catch (Exception e) {
			throw new APIException(e);
		}
	}
	
	@Override
	public String getDisplayProperty() {
		return "Upper Left Limb Repair";
	}
	
	@Override
	public String getUuidProperty() {
		return null;
	}
	
	private void assertProps() {
		assertPropEquals("id", getObject().getId());
		assertPropEquals("name", getObject().getName());
		assertPropEquals("requestDate", OdooConstants.DATE_FORMATTER.format(getObject().getRequestDate()));
		assertPropEquals("scheduleDate", getObject().getScheduleDate());
		assertPropEquals("duration", getObject().getDuration());
		assertPropEquals("display", getObject().getName());
		Equipment equipment = getObject().getEquipment();
		Map category = new HashMap();
		category.put("categoryId", equipment.getCategoryId());
		category.put("categoryName", equipment.getCategoryName());
		Map equipmentResource = new HashMap();
		equipmentResource.put("id", equipment.getId());
		equipmentResource.put("name", equipment.getName());
		equipmentResource.put("serialNo", equipment.getSerialNo());
		equipmentResource.put("location", equipment.getLocation());
		equipmentResource.put("category", category);
		equipmentResource.put("display", equipment.getName());
		assertPropEquals("equipment", equipmentResource);
	}
	
	@Override
	public void validateRefRepresentation() {
		assertPropEquals("id", getObject().getId());
		assertPropEquals("name", getObject().getName());
		assertPropEquals("display", getObject().getName());
		assertPropNotPresent("requestDate");
		assertPropNotPresent("scheduleDate");
		assertPropNotPresent("duration");
		assertPropNotPresent("equipment");
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
