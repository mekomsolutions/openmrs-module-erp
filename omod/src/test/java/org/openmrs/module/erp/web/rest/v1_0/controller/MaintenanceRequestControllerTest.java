package org.openmrs.module.erp.web.rest.v1_0.controller;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.EQUIPMENT_FETCH_FIELDS;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.MODEL_EQUIPMENT;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.MODEL_REQUEST;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.MODEL_STAGE;
import static org.openmrs.module.erp.web.rest.ErpRestConstants.REPRESENTATION_ODOO_PREFIX;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.module.erp.api.ErpMaintenanceService;
import org.openmrs.module.erp.api.impl.odoo.OdooClient;
import org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl;
import org.openmrs.module.erp.api.impl.odoo.OdooTestUtils;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.test.Util;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;

public class MaintenanceRequestControllerTest extends BaseErpControllerTest {
	
	@Mock
	private OdooClient mockOdooClient;
	
	@Autowired
	private ErpMaintenanceService service;
	
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		OdooMaintenanceServiceImpl s = (OdooMaintenanceServiceImpl) ((Advised) service).getTargetSource().getTarget();
		s.setOdooClient(mockOdooClient);
		Object[] stageIds = getStageIds();
		Mockito.when(mockOdooClient.search(MODEL_STAGE, asList("done", "=", false))).thenReturn(stageIds);
		
		Object[] requestData = OdooTestUtils.loadResource("odoo_maintenance_requests.json");
		Mockito.when(mockOdooClient.searchAndRead(MODEL_REQUEST, asList("stage_id", "in", stageIds), null))
		        .thenReturn(requestData);
		
		List<Map> activeRequests = Arrays.stream(requestData).map(r -> (Map) r).collect(Collectors.toList());
		Object[] equipmentData = OdooTestUtils.loadResource("odoo_maintenance_equipment.json");
		List<Integer> equipmentIds = activeRequests.stream().map(r -> (Integer) ((Object[]) r.get("equipment_id"))[0])
		        .collect(Collectors.toList());
		Mockito.when(mockOdooClient.searchAndRead(MODEL_EQUIPMENT, asList("id", "in", equipmentIds), EQUIPMENT_FETCH_FIELDS))
		        .thenReturn(equipmentData);
	}
	
	private Object[] getStageIds() throws IOException {
		return OdooTestUtils.loadResource("odoo_maintenance_stages.json");
	}
	
	@Override
	public String getURI() {
		return "maintenancerequest";
	}
	
	@Override
	public String getUuid() {
		return null;
	}
	
	@Override
	public long getAllCount() {
		return 2;
	}
	
	@Test
	public void shouldGetAllTheMaintenanceRequestsThatAreNotYetDone() throws Exception {
		SimpleObject result = deserialize(handle(newGetRequest(getURI())));
		
		assertNotNull(result);
		Assert.assertEquals(getAllCount(), Util.getResultsSize(result));
	}
	
	@Test
	public void shouldGetAllTheMaintenanceRequestsAsTheSpecifiedCustomRepresentation() throws Exception {
		SimpleObject result = deserialize(handle(newGetRequest(getURI(), new Parameter("v", "custom:(id,name)"))));
		
		assertNotNull(result);
		assertEquals(getAllCount(), Util.getResultsSize(result));
		assertEquals(1, Util.getByPath(result, "results[0]/id"));
		assertEquals("Upper Left Limb Repair", Util.getByPath(result, "results[0]/name"));
		assertNull(Util.getByPath(result, "results[0]/other_field"));
		assertNull(Util.getByPath(result, "results[0]/request_date"));
		assertEquals(2, Util.getByPath(result, "results[1]/id"));
		assertEquals("Lower Left Limb Repair", Util.getByPath(result, "results[1]/name"));
		assertNull(Util.getByPath(result, "results[1]/other_field"));
		assertNull(Util.getByPath(result, "results[1]/request_date"));
	}
	
	@Test
	public void shouldGetAllTheMaintenanceRequestsAsTheOdooRepresentation() throws Exception {
		SimpleObject result = deserialize(
		    handle(newGetRequest(getURI(), new Parameter("v", REPRESENTATION_ODOO_PREFIX + "name,other_field"))));
		
		assertNotNull(result);
		assertEquals(getAllCount(), Util.getResultsSize(result));
		assertNull(Util.getByPath(result, "results[0]/id"));
		assertEquals("Upper Left Limb Repair", Util.getByPath(result, "results[0]/name"));
		assertEquals("other1", Util.getByPath(result, "results[0]/other_field"));
		assertNull(Util.getByPath(result, "results[0]/request_date"));
		assertNull(Util.getByPath(result, "results[1]/id"));
		assertEquals("Lower Left Limb Repair", Util.getByPath(result, "results[1]/name"));
		assertNull(Util.getByPath(result, "results[1]/other_field"));
		assertNull(Util.getByPath(result, "results[1]/request_date"));
	}
	
}
