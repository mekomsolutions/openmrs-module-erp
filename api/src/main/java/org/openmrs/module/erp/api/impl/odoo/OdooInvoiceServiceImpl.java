package org.openmrs.module.erp.api.impl.odoo;

import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Component(ErpConstants.COMPONENT_ODOO_INVOICE_SERVICE)
public class OdooInvoiceServiceImpl implements ErpInvoiceService {
	
	private static final String INVOICE_MODEL = "account.move";
	
	private ArrayList<String> invoiceDefaultAttributes = new ArrayList<String>(Arrays.asList("name", "amount_total",
	    "state", "pricelist_id", "payment_term_id", "invoice_status", "origin", "create_date", "currency_id"));
	
	@Autowired
	private OdooClient odooClient;
	
	public OdooInvoiceServiceImpl() {
	}
	
	public OdooInvoiceServiceImpl(OdooClient odooClient) {
		this.odooClient = odooClient;
	}
	
	@Override
	public List<String> defaultModelAttributes() {
		return invoiceDefaultAttributes;
	}
	
	@Override
	public Map<String, Object> getInvoiceById(String invoiceId) {
		
		Map<String, Object> response = new HashMap<>();

		if (odooClient.getUid().isEmpty()) {
			try {
				odooClient.authenticate();
			} catch (IOException e) {
				throw new APIException("Cannot authenticate to Odoo server", e);
			}
		}
		
		try {
			
			Object[] records = (Object[]) odooClient.execute("read", INVOICE_MODEL,
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

			ArrayList<String> fields = odooClient.getDomainFields(INVOICE_MODEL);
			Object[] records = (Object[]) odooClient.execute("search_read", INVOICE_MODEL, filterCollection, new HashMap() {{
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

			List<Object> condition = asList("move_id", "=", Integer.parseInt(invoiceId));

			filterCollection.add(condition);

			ArrayList<String> fields = odooClient.getDomainFields("account.move.line");
			Object[] records = (Object[]) odooClient.execute("search_read", "account.move.line", filterCollection, new HashMap() {{
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
