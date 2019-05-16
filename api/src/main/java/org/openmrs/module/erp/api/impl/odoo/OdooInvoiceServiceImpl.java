package org.openmrs.module.erp.api.impl.odoo;

import com.odoojava.api.FilterCollection;
import com.odoojava.api.ObjectAdapter;
import com.odoojava.api.Row;
import com.odoojava.api.RowCollection;
import com.odoojava.api.Session;
import org.json.JSONObject;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.api.ErpInvoiceService;
import org.openmrs.module.erp.api.utils.ErpConnection;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component(ErpConstants.COMPONENT_ODOO_INVOICE_SERVICE)
public class OdooInvoiceServiceImpl implements ErpInvoiceService {
	
	private final static String invoiceModel = "account.invoice";
	
	private ArrayList<String> invoiceDefaultAttributes = new ArrayList<String>(Arrays.asList("name", "amount_total",
	    "state", "pricelist_id", "payment_term_id", "invoice_status", "origin", "create_date", "currency_id"));
	
	private Session odooSession;
	
	public OdooInvoiceServiceImpl() {
		this.odooSession = new ErpConnection().getERPSession();
	}
	
	public OdooInvoiceServiceImpl(Session session) {
		this.odooSession = session;
	}
	
	@Override
	public ArrayList<String> defaultModelAttributes() {
		return invoiceDefaultAttributes;
	}
	
	@Override
	public Map<String, Object> getInvoiceById(String invoiceId) throws APIException {
		
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			odooSession.startSession();
			ObjectAdapter orderAdapter = odooSession.getObjectAdapter(invoiceModel);
			FilterCollection filters = new FilterCollection();
			
			filters.clear();
			filters.add("id", "=", invoiceId);
			RowCollection records = orderAdapter.searchAndReadObject(filters,
			    invoiceDefaultAttributes.toArray(new String[0]));
			if ((records != null) && (records.size() > 0)) {
				
				Row record = records.get(0);
				for (String field : invoiceDefaultAttributes) {
					Object value = record.get(field);
					if (value != null)
						response.put(field, value);
				}
				
			}
		}
		catch (Exception e) {
			throw new APIException("Error while reading data from ERP server", e);
		}
		return response;
	}
	
	@Override
	public ArrayList<Map<String, Object>> getInvoicesByFilters(ArrayList<JSONObject> filters) {
		
		ArrayList<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		try {
			odooSession.startSession();
			ObjectAdapter orderAdapter = odooSession.getObjectAdapter(invoiceModel);
			FilterCollection filterCollection = new FilterCollection();
			
			filterCollection.clear();
			
			for (JSONObject filter : filters) {
				filterCollection.add(filter.getString("field"), filter.getString("comparison"), filter.get("value"));
			}
			
			RowCollection records = orderAdapter.searchAndReadObject(filterCollection,
			    (String[]) invoiceDefaultAttributes.toArray());
			if ((records != null) && (records.size() > 0)) {
				for (Row record : records) {
					Map<String, Object> result = new HashMap<String, Object>();
					for (String field : invoiceDefaultAttributes) {
						Object value = record.get(field);
						if (value != null)
							result.put(field, value);
					}
					response.add(result);
				}
			}
		}
		catch (Exception e) {
			throw new APIException("Error while reading data from ERP server", e);
		}
		return response;
	}
}
