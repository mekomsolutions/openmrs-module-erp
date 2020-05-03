package org.openmrs.module.erp.api.impl.odoo;

import com.odoojava.api.*;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component(ErpConstants.COMPONENT_ODOO_PRODUCT_SERVICE)
public class OdooProductServiceImpl implements ErpProductService {
	
	private static final String PRODUCT_MODEL = "product.product";
	
	private List<String> productDefaultAttributes = Arrays.asList("id", "uuid", "name", "actual_stock", "price", "mrp");
	
	private Session session;
	
	@Autowired
	private OdooSession odooSession;
	
	public OdooProductServiceImpl() {
	}
	
	public OdooProductServiceImpl(OdooSession odooSession) {
		this.session = odooSession.getSession();
	}
	
	@Override
	public List<String> defaultModelAttributes() {
		return productDefaultAttributes;
	}
	
	@Override
	public Map<String, Object> getErpProductById(String erpProductId) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		if (this.session == null) {
			this.session = odooSession.getSession();
		}
		try {
			session.startSession();
			ObjectAdapter productAdapter = session.getObjectAdapter(PRODUCT_MODEL);
			FilterCollection filters = new FilterCollection();
			
			String[] fields = productAdapter.getFieldNames();
			
			filters.clear();
			filters.add("uuid", "=", erpProductId);
			
			RowCollection records = productAdapter.searchAndReadObject(filters, fields);
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
	public List<Map<String, Object>> getErpProductByFilters(List<Filter> filters) {
		
		ArrayList<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		if (this.session == null) {
			this.session = odooSession.getSession();
		}
		try {
			session.startSession();
			ObjectAdapter productAdapter = session.getObjectAdapter(PRODUCT_MODEL);
			FilterCollection filterCollection = new FilterCollection();
			String[] fields = productAdapter.getFieldNames();
			filterCollection.clear();
			for (Filter filter : filters) {
				filterCollection.add(filter.getFieldName(), filter.getComparison(), filter.getValue());
			}
			
			RowCollection records = productAdapter.searchAndReadObject(filterCollection, fields);
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
