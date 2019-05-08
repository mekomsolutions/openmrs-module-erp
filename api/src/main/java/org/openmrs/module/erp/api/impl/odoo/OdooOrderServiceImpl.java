package org.openmrs.module.erp.api.impl.odoo;

import com.odoojava.api.FilterCollection;
import com.odoojava.api.ObjectAdapter;
import com.odoojava.api.Row;
import com.odoojava.api.RowCollection;
import com.odoojava.api.Session;
import org.json.JSONObject;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.api.ErpOrderService;
import org.openmrs.module.erp.api.utils.ErpConnection;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component(ErpConstants.COMPONENT_ODOO_ORDER_SERVICE)
public class OdooOrderServiceImpl implements ErpOrderService {
	
	private final static String orderModel = "sale.order";
	
	private String[] orderModelAttributes = new String[] { "partner_uuid", "name", "amount_total", "state", "pricelist_id",
	        "payment_term_id", "invoice_status", "origin", "create_date", "currency_id", "order_line", "invoice_count",
	        "invoice_ids", "product_id" };
	
	private Session odooSession;
	
	public OdooOrderServiceImpl() {
		this.odooSession = new ErpConnection().getERPSession();
	}
	
	public OdooOrderServiceImpl(Session session) {
		this.odooSession = session;
	}
	
	@Override
	public ArrayList<JSONObject> getErpOrdersByPatientUuid(String uuid) throws APIException {
		
		ArrayList<JSONObject> response = new ArrayList<JSONObject>();
		try {
			odooSession.startSession();
			ObjectAdapter orderAdapter = odooSession.getObjectAdapter(orderModel);
			FilterCollection filters = new FilterCollection();
			
			filters.clear();
			filters.add("partner_uuid", "=", uuid);
			RowCollection records = orderAdapter.searchAndReadObject(filters, orderModelAttributes);
			if ((records != null) && (records.size() > 0)) {
				for (Row record : records) {
					Map<String, Object> result = new HashMap<String, Object>();
					for (String field : orderModelAttributes) {
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
	
	@Override
	public JSONObject getErpOrderById(String erpOrderId) throws APIException {
		
		JSONObject response = new JSONObject();
		
		try {
			odooSession.startSession();
			ObjectAdapter orderAdapter = odooSession.getObjectAdapter(orderModel);
			FilterCollection filters = new FilterCollection();
			
			filters.clear();
			filters.add("id", "=", erpOrderId);
			RowCollection records = orderAdapter.searchAndReadObject(filters, orderModelAttributes);
			if ((records != null) && (records.size() > 0)) {
				Row record = records.get(0);
				Map<String, Object> result = new HashMap<String, Object>();
				for (String field : orderModelAttributes) {
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
}
