package org.openmrs.module.erp.api.impl.odoo;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.xmlrpc.XmlRpcException;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.api.ErpMaintenanceService;
import org.openmrs.module.erp.impl.odoo.Equipment;
import org.openmrs.module.erp.impl.odoo.MaintenanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(ErpConstants.COMPONENT_ODOO_MAINTENANCE_SERVICE)
public class OdooMaintenanceServiceImpl extends BaseOpenmrsService implements ErpMaintenanceService {
	
	private static final Logger logger = LoggerFactory.getLogger(OdooMaintenanceServiceImpl.class);
	
	protected static final String MODEL_REQUEST = "maintenance.request";
	
	protected static final String MODEL_EQUIPMENT = "maintenance.equipment";
	
	protected static final String MODEL_STAGE = "maintenance.stage";
	
	protected static final List<String> REQUEST_FETCH_FIELDS = unmodifiableList(asList("equipment_id"));
	
	protected static final List<String> EQUIPMENT_FETCH_FIELDS = unmodifiableList(
	    asList("name", "category_id", "serial_no", "location"));
	
	private OdooClient odooClient;
	
	@Autowired
	public OdooMaintenanceServiceImpl(OdooClient odooClient) {
		this.odooClient = odooClient;
	}
	
	@Override
	public List<MaintenanceRequest> getMaintenanceRequests() throws APIException {
		List<Map> activeRequests = Arrays.stream(getActiveRequests()).map(r -> (Map) r).collect(Collectors.toList());
		List<Integer> equipmentIds = activeRequests.stream().map(r -> (Integer) ((Object[]) r.get("equipment_id"))[0])
		        .collect(Collectors.toList());
		
		try {
			Object[] equipmentsData = odooClient.searchAndRead(MODEL_EQUIPMENT, asList("id", "in", equipmentIds),
			    EQUIPMENT_FETCH_FIELDS);
			
			List<Equipment> equipments = OdooJsonUtils.convertToList(equipmentsData, Equipment.class);
			Map<Integer, Equipment> idAndEquipmentMap = equipments.stream()
			        .collect(Collectors.toMap(Equipment::getId, Function.identity()));
			
			List<MaintenanceRequest> requests = activeRequests.stream().map(r -> {
				Integer id = (Integer) r.get("id");
				Integer equipmentId = (Integer) ((Object[]) r.get("equipment_id"))[0];
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
			//TODO cache the stage ids
			Object[] stageIds = getNonTerminalMaintenanceStageIds();
			if (logger.isDebugEnabled()) {
				logger.debug("Non terminal maintenance stage ids: " + asList(stageIds));
			}
			
			return odooClient.searchAndRead(MODEL_REQUEST, asList("stage_id", "in", stageIds), REQUEST_FETCH_FIELDS);
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
