package org.openmrs.module.erp.api.impl.odoo;

import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Component(ErpConstants.COMPONENT_ODOO_ORDER_SERVICE)
public class OdooOrderServiceImpl implements ErpOrderService {
	
	private static final String ORDER_MODEL = "sale.order";
	
	private List<String> orderDefaultAttributes = asList("name", "amount_total", "state", "pricelist_id", "payment_term_id",
	    "invoice_status", "origin", "create_date", "currency_id", "order_line", "invoice_count", "invoice_ids", "product_id");
	
	@Autowired
	private OdooClient odooClient;
	
	public OdooOrderServiceImpl() {
	}
	
	public OdooOrderServiceImpl(OdooClient odooClient) {
		this.odooClient = odooClient;
	}
	
	@Override
	public List<String> defaultModelAttributes() {
		return orderDefaultAttributes;
	}
	
	@Override
	public Map<String, Object> getErpOrderById(String erpOrderId) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		if (odooClient.getUid().isEmpty()) {
			try {
				odooClient.authenticate();
			}
			catch (IOException e) {
				throw new APIException("Cannot authenticate to Odoo server", e);
			}
		}
		
		try {
			
			Object[] records = (Object[]) odooClient.execute("read", ORDER_MODEL,
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
		
		ArrayList<Map<String, Object>> response = new ArrayList<>();

		if (odooClient.getUid().isEmpty()) {
			try {
				odooClient.authenticate();
			} catch (IOException e) {
				throw new APIException("Cannot authenticate to Odoo server", e);
			}
		}

		try {
			List<List<Object>> filterCollection = new ArrayList<>();

			for (Filter filter : filters) {

				filterCollection.add(asList(filter.getFieldName(),
						filter.getComparison(),
						filter.getValue()));
			}

			ArrayList<String> fields = odooClient.getDomainFields(ORDER_MODEL);
			Object[] records = (Object[]) odooClient.execute("search_read", ORDER_MODEL, filterCollection, new HashMap() {{
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
		
		List<Map<String, Object>> response = new ArrayList<>();
		List<List<Object>> filterCollection = new ArrayList<>();

		if (odooClient.getUid().isEmpty()) {
			try {
				odooClient.authenticate();
			} catch (IOException e) {
				throw new APIException("Cannot authenticate to Odoo server", e);
			}
		}

		try {

			List<Object> condition = asList("order_id", "=", Integer.parseInt(erpOrderId));
			filterCollection.add(condition);

			ArrayList<String> fields = odooClient.getDomainFields("sale.order.line");
			Object[] records = (Object[]) odooClient.execute("search_read", "sale.order.line", filterCollection, new HashMap() {{
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
			}
		}
		catch (Exception e) {
			throw new APIException("Error while reading data from ERP server", e);
		}
		return response;
	}
}
