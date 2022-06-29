package org.openmrs.module.erp.api;

import java.util.List;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.impl.odoo.MaintenanceRequest;

public interface ErpMaintenanceService extends OpenmrsService {
	
	/**
	 * Fetches all maintenance requests that are not yet done.
	 * 
	 * @return java.util.Map object
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	List<MaintenanceRequest> getMaintenanceRequests() throws APIException;
	
}
