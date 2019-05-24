package org.openmrs.module.erp.api.impl.odoo;

import com.odoojava.api.*;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpOrderService;
import org.openmrs.module.erp.api.utils.ErpConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component(ErpConstants.COMPONENT_ODOO_ORDER_SERVICE)
public class OdooOrderServiceImpl implements ErpOrderService {
	
	private static final String ORDER_MODEL = "sale.order";
	
	private List<String> orderDefaultAttributes = Arrays.asList("name", "amount_total", "state", "pricelist_id",
	    "payment_term_id", "invoice_status", "origin", "create_date", "currency_id", "order_line", "invoice_count",
	    "invoice_ids", "product_id");
	
	private Session odooSession;
	
	@Autowired
	public OdooOrderServiceImpl(ErpConnection erpConnection) {
		this.odooSession = erpConnection.getSession();
	}
	
	@Override
	public List<String> defaultModelAttributes() {
		return orderDefaultAttributes;
	}
	
	@Override
	public Map<String, Object> getErpOrderById(String erpOrderId) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			odooSession.startSession();
			ObjectAdapter orderAdapter = odooSession.getObjectAdapter(ORDER_MODEL);
			FilterCollection filters = new FilterCollection();
			
			String[] fields = orderAdapter.getFieldNames();
			
			filters.clear();
			filters.add("id", "=", erpOrderId);
			
			RowCollection records = orderAdapter.searchAndReadObject(filters, orderDefaultAttributes.toArray(new String[0]));
			if ((records != null) && (!records.isEmpty())) {
				Row record = records.get(0);
				for (String field : fields) {
					Object value = record.get(field);
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
	public List<Map<String, Object>> getErpOrdersByFilters(List<Filter> filters) {
		
		ArrayList<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		
		try {
			odooSession.startSession();
			ObjectAdapter orderAdapter = odooSession.getObjectAdapter(ORDER_MODEL);
			FilterCollection filterCollection = new FilterCollection();
			String[] fields = orderAdapter.getFieldNames();
			filterCollection.clear();
			for (Filter filter : filters) {
				filterCollection.add(filter.getFieldName(), filter.getComparison(), filter.getValue());
			}
			
			RowCollection records = orderAdapter.searchAndReadObject(filterCollection,
			    orderDefaultAttributes.toArray(new String[0]));
			if ((records != null) && (!records.isEmpty())) {
				for (Row record : records) {
					Map<String, Object> result = new HashMap<String, Object>();
					for (String field : fields) {
						Object value = record.get(field);
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
