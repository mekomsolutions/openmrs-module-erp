package org.openmrs.module.erp.api.impl.odoo;

import static java.util.Arrays.asList;
import static java.util.Date.from;
import static org.junit.Assert.assertEquals;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.EQUIPMENT_FETCH_FIELDS;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.MODEL_EQUIPMENT;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.MODEL_REQUEST;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.MODEL_STAGE;
import static org.openmrs.module.erp.api.impl.odoo.OdooMaintenanceServiceImpl.REQUEST_FETCH_FIELDS;
import static org.openmrs.module.erp.impl.odoo.OdooConstants.DATE_FORMATTER;
import static org.openmrs.module.erp.impl.odoo.OdooConstants.DATE_TIME_FORMATTER;
import static org.openmrs.module.erp.impl.odoo.OdooConstants.ZONE_ID_UTC;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.module.erp.impl.odoo.MaintenanceRequest;

public class OdooMaintenanceServiceImplTest {
	
	@Mock
	private OdooClient mockOdooClient;
	
	private OdooMaintenanceServiceImpl service;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		service = new OdooMaintenanceServiceImpl();
		service.setOdooClient(mockOdooClient);
	}
	
	private Object[] getStageIds() throws Exception {
		return OdooTestUtils.loadResource("odoo_maintenance_stages.json");
	}
	
	@Test
	public void getMaintenanceRequests_shouldFetchTheMaintenanceRequestsThatAreNotYetDone() throws Exception {
		Object[] stageIds = getStageIds();
		Mockito.when(mockOdooClient.search(MODEL_STAGE, asList("done", "=", false))).thenReturn(stageIds);
		
		Object[] requestData = OdooTestUtils.loadResource("odoo_maintenance_requests.json");
		Mockito.when(mockOdooClient.searchAndRead(MODEL_REQUEST, asList("stage_id", "in", stageIds), REQUEST_FETCH_FIELDS))
		        .thenReturn(requestData);
		
		List<Map> activeRequests = Arrays.stream(requestData).map(r -> (Map) r).collect(Collectors.toList());
		Object[] equipmentData = OdooTestUtils.loadResource("odoo_maintenance_equipment.json");
		List<Integer> equipmentIds = activeRequests.stream().map(r -> (Integer) ((Object[]) r.get("equipment_id"))[0])
		        .collect(Collectors.toList());
		Mockito.when(mockOdooClient.searchAndRead(MODEL_EQUIPMENT, asList("id", "in", equipmentIds), EQUIPMENT_FETCH_FIELDS))
		        .thenReturn(equipmentData);
		
		List<MaintenanceRequest> requests = service.getMaintenanceRequests();
		assertEquals(2, requests.size());
		MaintenanceRequest upperLeftLimbReq = requests.get(0);
		assertEquals(1, upperLeftLimbReq.getId().intValue());
		assertEquals("Upper Left Limb Repair", upperLeftLimbReq.getName());
		assertEquals(DATE_FORMATTER.parse("2022-06-28"), upperLeftLimbReq.getRequestDate());
		assertEquals(from(LocalDateTime.parse("2022-06-28 11:00:15", DATE_TIME_FORMATTER).atZone(ZONE_ID_UTC).toInstant()),
		    upperLeftLimbReq.getScheduleDate());
		assertEquals(new Double(1.0), upperLeftLimbReq.getDuration());
		assertEquals(101, upperLeftLimbReq.getEquipment().getId().intValue());
		assertEquals("Upper Left Limb", upperLeftLimbReq.getEquipment().getName());
		assertEquals(201, upperLeftLimbReq.getEquipment().getCategoryId().intValue());
		assertEquals("prosthesis", upperLeftLimbReq.getEquipment().getCategoryName());
		assertEquals("ULL", upperLeftLimbReq.getEquipment().getSerialNo());
		assertEquals("Repair House 1", upperLeftLimbReq.getEquipment().getLocation());
		MaintenanceRequest lowerLeftLimbReq = requests.get(1);
		assertEquals(2, lowerLeftLimbReq.getId().intValue());
		assertEquals("Lower Left Limb Repair", lowerLeftLimbReq.getName());
		assertEquals(DATE_FORMATTER.parse("2022-06-29"), lowerLeftLimbReq.getRequestDate());
		assertEquals(from(LocalDateTime.parse("2022-06-29 16:00:20", DATE_TIME_FORMATTER).atZone(ZONE_ID_UTC).toInstant()),
		    lowerLeftLimbReq.getScheduleDate());
		assertEquals("Upper Left Limb Repair", upperLeftLimbReq.getName());
		assertEquals(new Double(2.0), lowerLeftLimbReq.getDuration());
		assertEquals(102, lowerLeftLimbReq.getEquipment().getId().intValue());
		assertEquals("Lower Left Limb", lowerLeftLimbReq.getEquipment().getName());
		assertEquals(202, lowerLeftLimbReq.getEquipment().getCategoryId().intValue());
		assertEquals("orthesis", lowerLeftLimbReq.getEquipment().getCategoryName());
		assertEquals("LLL", lowerLeftLimbReq.getEquipment().getSerialNo());
		assertEquals("Repair House 2", lowerLeftLimbReq.getEquipment().getLocation());
	}
}
