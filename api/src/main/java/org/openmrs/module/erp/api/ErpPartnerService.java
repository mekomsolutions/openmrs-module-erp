package org.openmrs.module.erp.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;

import java.util.List;
import java.util.Map;

public interface ErpPartnerService {
	
	List<String> defaultModelAttributes();
	
	/**
	 * Returns an ERP partner by order id.
	 * 
	 * @param erpPartnerId The ERP partner id
	 * @return JSONObject
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	Map<String, Object> getErpPartnerById(String erpPartnerId);
	
	/**
	 * Returns a filtered list of ERP partners.
	 * 
	 * @param filters The filter expressions
	 * @return ArrayList<JSONObject>
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	List<Map<String, Object>> getErpPartnersByFilters(List<Filter> filters);
	
}
