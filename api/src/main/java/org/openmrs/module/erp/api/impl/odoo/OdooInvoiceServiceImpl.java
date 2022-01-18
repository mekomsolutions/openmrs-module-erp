package org.openmrs.module.erp.api.impl.odoo;

//import com.odoojava.api.FilterCollection;
//import com.odoojava.api.ObjectAdapter;
//import com.odoojava.api.Row;
//import com.odoojava.api.RowCollection;
//import com.odoojava.api.Session;
//import org.openmrs.api.APIException;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Component(ErpConstants.COMPONENT_ODOO_INVOICE_SERVICE)
public class OdooInvoiceServiceImpl implements ErpInvoiceService {
	
	private static final String INVOICE_MODEL = "account.invoice";
	
	private ArrayList<String> invoiceDefaultAttributes = new ArrayList<String>(Arrays.asList("name", "amount_total",
	    "state", "pricelist_id", "payment_term_id", "invoice_status", "origin", "create_date", "currency_id"));
	
	@Autowired
	private OdooSession odooSession;
	
	public OdooInvoiceServiceImpl() {
	}
	
	public OdooInvoiceServiceImpl(OdooSession odooSession) {
		this.odooSession = odooSession;
	}
	
	@Override
	public List<String> defaultModelAttributes() {
		return invoiceDefaultAttributes;
	}
	
	@Override
	public Map<String, Object> getInvoiceById(String invoiceId) {
		
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
			
			Object[] records = (Object[]) odooSession.execute("read", INVOICE_MODEL,
			    Collections.singletonList(Integer.parseInt(invoiceId)), null);
			
			if ((records != null) && (records.length > 0)) {
				Map record = (Map) records[0];
				for (String field : invoiceDefaultAttributes) {
					Object value = record.get(field);
					response.put(field, value);
				}
				response.put("invoice_lines", getInvoiceLinesByInvoiceId(invoiceId));
			}
		}
		catch (Exception e) {
			throw new APIException("Error while reading data from ERP server", e);
		}
		return response;
	}
	
	@Override
	public List<Map<String, Object>> getInvoicesByFilters(List<Filter> filters) {
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

			ArrayList<String> fields = odooSession.getDomainFields(INVOICE_MODEL);
			Object[] records = (Object[]) odooSession.execute("search_read", INVOICE_MODEL, filterCollection, new HashMap() {{
				put("fields", fields);
			}});

			if ((records != null) && (records.length > 0)) {

				asList(records).forEach(record -> {
					Map<String, Object> rec = (Map<String, Object>) record;
					Map<String, Object> result = new HashMap<>();
					for (String field : fields) {
						Object value = rec.get(field);
						result.put(field, value);
					}
					String erpOrderId = (String.valueOf(result.get("id")));
					result.put("invoice_lines", getInvoiceLinesByInvoiceId(erpOrderId));
					response.add(result);
				});
			}
		}
		catch (Exception e) {
			throw new APIException("Error while reading data from ERP server", e);
		}
		return response;
	}
	
	private List<Map<String, Object>> getInvoiceLinesByInvoiceId(String invoiceId) {

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

			List<Object> condition = asList("order_id", "=", Integer.parseInt(invoiceId));

			filterCollection.add(condition);

			ArrayList<String> fields = odooSession.getDomainFields("account.invoice.line");
			Object[] records = (Object[]) odooSession.execute("search_read", "account.invoice.line", filterCollection, new HashMap() {{
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
