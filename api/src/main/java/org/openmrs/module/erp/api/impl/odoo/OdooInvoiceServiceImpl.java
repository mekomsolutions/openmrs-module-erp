package org.openmrs.module.erp.api.impl.odoo;

import com.odoojava.api.FilterCollection;
import com.odoojava.api.ObjectAdapter;
import com.odoojava.api.Row;
import com.odoojava.api.RowCollection;
import com.odoojava.api.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.api.ErpInvoiceService;
import org.openmrs.module.erp.api.utils.ErpConnection;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component(ErpConstants.COMPONENT_ODOO_INVOICE_SERVICE)
public class OdooInvoiceServiceImpl implements ErpInvoiceService {
	
	private final static String invoiceModel = "account.invoice";
	
	private String[] invoiceModelAttributes = new String[] { "partner_uuid", "name", "amount_total", "state",
	        "pricelist_id", "payment_term_id", "invoice_status", "origin", "create_date", "currency_id" };
	
	private Session odooSession;
	
	public OdooInvoiceServiceImpl() {
		this.odooSession = new ErpConnection().getERPSession();
	}
	
	public OdooInvoiceServiceImpl(Session session) {
		this.odooSession = session;
	}
	
	@Override
	public JSONObject getInvoiceById(String invoiceId) throws APIException {
		
		JSONObject response = new JSONObject();
		try {
			odooSession.startSession();
			ObjectAdapter orderAdapter = odooSession.getObjectAdapter(invoiceModel);
			FilterCollection filters = new FilterCollection();
			
			filters.clear();
			filters.add("id", "=", invoiceId);
			RowCollection records = orderAdapter.searchAndReadObject(filters, invoiceModelAttributes);
			if ((records != null) && (records.size() > 0)) {
				
				Row record = records.get(0);
				Map<String, Object> result = new HashMap<String, Object>();
				for (String field : invoiceModelAttributes) {
					Object value = record.get(field);
					if (value != null)
						result.put(field, value);
				}
				response = new JSONObject(result);
				
			}
		}
		catch (Exception e) {
			throw new APIException("Error while reading data from ERP server", e);
		}
		return response;
	}
	
	@Override
	public ArrayList<JSONObject> getInvoicesByFilters(ArrayList<Object> filters) {
		
		ArrayList<JSONObject> response = new ArrayList<JSONObject>();
		JSONArray filtersObject = new JSONArray();
		try {
			odooSession.startSession();
			ObjectAdapter orderAdapter = odooSession.getObjectAdapter(invoiceModel);
			FilterCollection filterCollection = new FilterCollection();
			
			filterCollection.clear();
			
			RowCollection records = orderAdapter.searchAndReadObject(filterCollection, invoiceModelAttributes);
			if ((records != null) && (records.size() > 0)) {
				for (Row record : records) {
					Map<String, Object> result = new HashMap<String, Object>();
					for (String field : invoiceModelAttributes) {
						Object value = record.get(field);
						if (value != null)
							result.put(field, value);
					}
					response.add(new JSONObject(result));
				}
			}
		}
		catch (Exception e) {
			throw new APIException("Error while reading data from ERP server", e);
		}
		return response;
	}
}
