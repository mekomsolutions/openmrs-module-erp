package org.openmrs.module.erp.api;

import org.json.JSONObject;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;

public interface ErpInvoiceService {
	
	/**
	 * Returns an invoice by order id.
	 * 
	 * @param orderId The ERP invoice id
	 * @return JSONObject
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	JSONObject getInvoiceById(String orderId) throws APIException;
}
