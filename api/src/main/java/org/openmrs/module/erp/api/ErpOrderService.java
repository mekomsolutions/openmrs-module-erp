package org.openmrs.module.erp.api;

import org.json.JSONObject;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;

import java.util.ArrayList;

public interface ErpOrderService {
	
	/**
	 * Returns a filtered list of erp orders.
	 * 
	 * @param filters The filter expressions
	 * @return ArrayList<JSONObject>
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	ArrayList<JSONObject> getErpOrdersByFilters(ArrayList<Object> filters) throws APIException;
	
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
