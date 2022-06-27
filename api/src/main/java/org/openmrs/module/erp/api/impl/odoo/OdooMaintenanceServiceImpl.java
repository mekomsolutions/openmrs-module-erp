package org.openmrs.module.erp.api.impl.odoo;

import static java.util.Arrays.asList;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.xmlrpc.XmlRpcException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.api.ErpMaintenanceService;
import org.openmrs.module.erp.impl.odoo.Equipment;
import org.openmrs.module.erp.impl.odoo.MaintenanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(ErpConstants.COMPONENT_ODOO_MAINTENANCE_SERVICE)
public class OdooMaintenanceServiceImpl extends BaseOpenmrsService implements ErpMaintenanceService {
	
	private static final String MODEL_REQUEST = "maintenance.request";
	
	private static final String MODEL_EQUIPMENT = "maintenance.equipment";
	
	private static final String MODEL_STAGE = "maintenance.stage";
	
	private OdooClient odooClient;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	public OdooMaintenanceServiceImpl(OdooClient odooClient) {
		this.odooClient = odooClient;
	}
	
	@Override
	public List<MaintenanceRequest> getActiveMaintenanceRequests() throws APIException {
		List<Map> activeRequests = Arrays.stream(getActiveRequests()).map(r -> (Map) r).collect(Collectors.toList());
		List<Integer> equipmentIds = activeRequests.stream().map(r -> (Integer) r.get("equipment_id"))
		        .collect(Collectors.toList());
		
		try {
			Object[] equipmentsData = odooClient.searchAndRead(MODEL_EQUIPMENT, asList("id", "in", equipmentIds),
			    asList("name", "category_id", "serial_no", "location"));
			
			CollectionType javaType = MAPPER.getTypeFactory().constructCollectionType(List.class, Equipment.class);
			
			List<Equipment> equipments = MAPPER.convertValue(equipmentsData, javaType);
			Map<Integer, Equipment> idAndEquipmentMap = equipments.stream()
			        .collect(Collectors.toMap(Equipment::getId, Function.identity()));
			
			List<MaintenanceRequest> requests = activeRequests.stream().map(r -> {
				Integer id = (Integer) r.get("id");
				Integer equipmentId = (Integer) r.get("equipment_id");
				MaintenanceRequest request = new MaintenanceRequest(id, idAndEquipmentMap.get(equipmentId));
				return request;
			}).collect(Collectors.toList());
			
			return requests;
		}
		catch (XmlRpcException e) {
			throw new APIException(e);
		}
		catch (MalformedURLException e) {
			throw new APIException(e);
		}
	}
	
	private Object[] getActiveRequests() {
		try {
			return odooClient.searchAndRead(MODEL_REQUEST, asList("stage_id", "in", getNonTerminalMaintenanceStageIds()),
			    asList("equipment_id"));
		}
		catch (XmlRpcException e) {
			throw new APIException(e);
		}
		catch (MalformedURLException e) {
			throw new APIException(e);
		}
	}
	
	private Object[] getNonTerminalMaintenanceStageIds() {
		try {
			return odooClient.search(MODEL_STAGE, asList("done", "=", false));
		}
		catch (XmlRpcException e) {
			throw new APIException(e);
		}
		catch (MalformedURLException e) {
			throw new APIException(e);
		}
	}
	
}
