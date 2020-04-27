package org.openmrs.module.erp.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;

import java.util.List;
import java.util.Map;

public interface ErpProductService {
	
	List<String> defaultModelAttributes();
	
	/**
	 * Returns an ERP partner by order id.
	 * 
	 * @param erpProductId The ERP partner id
	 * @return JSONObject
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	Map<String, Object> getErpProductById(String erpProductId);
	
	/**
	 * Returns a filtered list of ERP partners.
	 * 
	 * @param filters The filter expressions
	 * @return ArrayList<JSONObject>
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	List<Map<String, Object>> getErpProductByFilters(List<Filter> filters);
	
}
