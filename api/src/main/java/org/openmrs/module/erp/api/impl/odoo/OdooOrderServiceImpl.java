package org.openmrs.module.erp.api.impl.odoo;

import com.sun.rowset.internal.Row;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Component(ErpConstants.COMPONENT_ODOO_ORDER_SERVICE)
public class OdooOrderServiceImpl implements ErpOrderService {
	
	private static final String ORDER_MODEL = "sale.order";
	
	private List<String> orderDefaultAttributes = asList("name", "amount_total", "state", "pricelist_id", "payment_term_id",
	    "invoice_status", "origin", "create_date", "currency_id", "order_line", "invoice_count", "invoice_ids", "product_id");
	
	@Autowired
	private OdooSession odooSession;
	
	public OdooOrderServiceImpl() {
	}
	
	public OdooOrderServiceImpl(OdooSession odooSession) {
		this.odooSession = odooSession;
	}
	
	@Override
	public List<String> defaultModelAttributes() {
		return orderDefaultAttributes;
	}
	
	@Override
	public Map<String, Object> getErpOrderById(String erpOrderId) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			
			//			ObjectAdapter orderAdapter = session.getObjectAdapter(ORDER_MODEL);
			//			FilterCollection filters = new FilterCollection();
			//
			//			String[] fields = orderAdapter.getFieldNames();
			//
			//			filters.clear();
			//			filters.add("id", "=", erpOrderId);
			//
			//			RowCollection records = orderAdapter.searchAndReadObject(filters, fields);
			
			// new HashMap() {{ put("limit", 1);}}
			
			Object[] records = (Object[]) odooSession.execute("read", ORDER_MODEL,
			    Collections.singletonList(Integer.parseInt(erpOrderId)), null);
			
			if ((records != null) && (records.length > 0)) {
				Map record = (Map) records[0];
				for (String field : orderDefaultAttributes) {
					Object value = record.get(field);
					response.put(field, value);
				}
				response.put("order_lines", getErpOrderLinesByOrderId(erpOrderId));
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
			List<List<Object>> filterCollection = new ArrayList<List<Object>>();
//			session.startSession();
//			ObjectAdapter orderAdapter = session.getObjectAdapter(ORDER_MODEL);

//			String[] fields = orderAdapter.getFieldNames();
//			filterCollection.clear();


			for (Filter filter : filters) {

				filterCollection.add(asList(filter.getFieldName(),
						filter.getComparison(),
						filter.getValue()));
			}
			
//			RowCollection records = orderAdapter.searchAndReadObject(filterCollection, fields);

			ArrayList<String> fields = odooSession.getDomainFields(ORDER_MODEL);
			Object[] records = (Object[]) odooSession.execute("search_read", ORDER_MODEL, filterCollection, new HashMap() {{
				put("fields", fields);
			}});

			if ((records != null) && (records.length > 0)) {

				asList(records).forEach(record -> {
					Map<String, Object> rec = (Map<String, Object>) record;
					Map<String, Object> result = new HashMap<String, Object>();
					for (String field : fields) {
						Object value = rec.get(field);
						result.put(field, value);
					}
					String erpOrderId = (String.valueOf(result.get("id")));
					result.put("order_lines", getErpOrderLinesByOrderId(erpOrderId));
					response.add(result);
				});

			}
		}
		catch (Exception e) {
			throw new APIException("Error while reading data from ERP server", e);
		}
		return response;
	}
	
	private List<Map<String, Object>> getErpOrderLinesByOrderId(String erpOrderId) {
		
		List<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		List<List<Object>> filterCollection = new ArrayList<List<Object>>();

		try {
//			this.session.startSession();
//			ObjectAdapter orderAdapter = this.session.getObjectAdapter("sale.order.line");
//			FilterCollection filterCollection = new FilterCollection();
//			String[] fields = orderAdapter.getFieldNames();
//
//			filterCollection.clear();
//

			List<Object> condition = asList("order_id", "=", Integer.parseInt(erpOrderId));

			filterCollection.add(condition);



			ArrayList<String> fields = odooSession.getDomainFields("sale.order.line");
			Object[] records = (Object[]) odooSession.execute("search_read", "sale.order.line", filterCollection, new HashMap() {{
				put("fields", fields);
				put("limit", 15);
			}});

			if ((records != null) && (records.length > 0)) {

				asList(records).forEach(record -> {
					Map<String, Object> rec = (Map<String, Object>) record;
					Map<String, Object> result = new HashMap<>();
					for (String field : fields) {
						Object value = rec.get(field);
						result.put(field, value);
					}
					response.add(result);
				});


//				for (Object record : result) {
//					record = new HashMap<String, Object>();
//					for (String field : fields) {
//						Object value = record.get(field);
//						result.put(field, value);
//					}
//					response.add(result);
//				}
			}
		}
		catch (Exception e) {
			throw new APIException("Error while reading data from ERP server", e);
		}
		return response;
	}
}
