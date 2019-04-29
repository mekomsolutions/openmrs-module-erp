package org.openmrs.module.erp.api;

import org.json.JSONObject;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;

import java.util.ArrayList;

public interface ErpOrderService {
	
	/**
	 * Returns a list of erp orders by client uuid.
	 * 
	 * @param uuid The patient uuid
	 * @return ArrayList<JSONObject>
	 * @throws APIException
	 */
	
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	ArrayList<JSONObject> getErpOrdersByPatientUuid(String uuid) throws APIException;
	
	/**
	 * Returns an erp order by order id.
	 * 
	 * @param erpOrderId The ERP order id
	 * @return JSONObject
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	JSONObject getErpOrderById(String erpOrderId) throws APIException;
	
}
