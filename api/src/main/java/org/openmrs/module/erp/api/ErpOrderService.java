package org.openmrs.module.erp.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;

import java.util.List;
import java.util.Map;

public interface ErpOrderService {
	
	List<String> defaultModelAttributes();
	
	/**
	 * Returns an erp order by order id.
	 * 
	 * @param erpOrderId The ERP order id
	 * @return JSONObject
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	Map<String, Object> getErpOrderById(String erpOrderId) throws APIException;
	
	/**
	 * Returns a filtered list of erp orders.
	 * 
	 * @param filters The filter expressions
	 * @return ArrayList<JSONObject>
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	List<Map<String, Object>> getErpOrdersByFilters(List<Filter> filters) throws APIException;
	
}
