package org.openmrs.module.erp.api.impl.odoo;

import com.odoojava.api.*;
import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component(ErpConstants.COMPONENT_ODOO_PARTNER_SERVICE)
public class OdooPartnerServiceImpl implements ErpPartnerService {
	
	private static final String PARTNER_MODEL = "res.partner";
	
	private List<String> partnerDefaultAttributes = Arrays.asList("id", "name", "uuid", "create_date");
	
	private Session session;
	
	@Autowired
	private OdooSession odooSession;
	
	public OdooPartnerServiceImpl() {
	}
	
	public OdooPartnerServiceImpl(OdooSession odooSession) {
		this.session = odooSession.getSession();
	}
	
	@Override
	public List<String> defaultModelAttributes() {
		return partnerDefaultAttributes;
	}
	
	@Override
	public Map<String, Object> getErpPartnerById(String erpPartnerId) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		if (this.session == null) {
			this.session = odooSession.getSession();
		}
		try {
			session.startSession();
			ObjectAdapter partnerAdapter = session.getObjectAdapter(PARTNER_MODEL);
			FilterCollection filters = new FilterCollection();
			
			String[] fields = partnerAdapter.getFieldNames();
			
			filters.clear();
			filters.add("id", "=", erpPartnerId);
			
			RowCollection records = partnerAdapter.searchAndReadObject(filters, fields);
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
	public List<Map<String, Object>> getErpPartnersByFilters(List<Filter> filters) {
		
		ArrayList<Map<String, Object>> response = new ArrayList<Map<String, Object>>();
		if (this.session == null) {
			this.session = odooSession.getSession();
		}
		try {
			session.startSession();
			ObjectAdapter partnerAdapter = session.getObjectAdapter(PARTNER_MODEL);
			FilterCollection filterCollection = new FilterCollection();
			String[] fields = partnerAdapter.getFieldNames();
			filterCollection.clear();
			for (Filter filter : filters) {
				filterCollection.add(filter.getFieldName(), filter.getComparison(), filter.getValue());
			}
			
			RowCollection records = partnerAdapter.searchAndReadObject(filterCollection, fields);
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
