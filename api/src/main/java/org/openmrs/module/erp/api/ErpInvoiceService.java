package org.openmrs.module.erp.api;

import org.json.JSONObject;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;

import java.util.ArrayList;

public interface ErpInvoiceService {
	
	/**
	 * Returns an invoice by order id.
	 * 
	 * @param invoiceId The ERP invoice id
	 * @return JSONObject
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	JSONObject getInvoiceById(String invoiceId) throws APIException;
	
	/**
	 * Returns an invoice by order id.
	 * 
	 * @param filters The filters expressions
	 * @return JSONObject
	 * @throws APIException
	 */
	@Authorized(ErpConstants.MODULE_PRIVILEGE)
	ArrayList<JSONObject> getInvoicesByFilters(ArrayList<Object> filters) throws APIException;
}
