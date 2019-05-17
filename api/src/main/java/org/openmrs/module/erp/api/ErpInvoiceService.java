package org.openmrs.module.erp.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;

import java.util.List;
import java.util.Map;

public interface ErpInvoiceService {
	
	List<String> defaultModelAttributes();
	
	/**
	 * Returns an invoice by order id.
	 * 
	 * @param invoiceId The ERP invoice id
	 * @return JSONObject
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	Map<String, Object> getInvoiceById(String invoiceId) throws APIException;
	
	/**
	 * Returns an invoice by order id.
	 * 
	 * @param filters The filters expressions
	 * @return JSONObject
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	List<Map<String, Object>> getInvoicesByFilters(List<Filter> filters) throws APIException;
}
