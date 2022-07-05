package org.openmrs.module.erp.api;

import java.util.List;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.impl.odoo.InventoryAdjustment;

public interface ErpInventoryService extends OpenmrsService {
	
	/**
	 * Fetches all active inventory adjustments.
	 * 
	 * @return list of {@link InventoryAdjustment}
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	List<InventoryAdjustment> getInventoryAdjustments() throws APIException;
	
}
