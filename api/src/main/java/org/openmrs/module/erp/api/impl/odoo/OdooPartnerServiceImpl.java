package org.openmrs.module.erp.api.impl.odoo;

import org.openmrs.api.APIException;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Arrays.asList;

@Component(ErpConstants.COMPONENT_ODOO_PARTNER_SERVICE)
public class OdooPartnerServiceImpl implements ErpPartnerService {
	
	private static final String PARTNER_MODEL = "res.partner";
	
	private List<String> partnerDefaultAttributes = Arrays.asList("id", "name", "ref", "create_date");
	
	@Autowired
	private OdooSession odooSession;
	
	public OdooPartnerServiceImpl() {
	}
	
	public OdooPartnerServiceImpl(OdooSession odooSession) {
		this.odooSession = odooSession;
	}
	
	@Override
	public List<String> defaultModelAttributes() {
		return partnerDefaultAttributes;
	}
	
	@Override
	public Map<String, Object> getErpPartnerById(String erpPartnerId) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
			
			Object[] records = (Object[]) odooSession.execute("read", PARTNER_MODEL,
			    Collections.singletonList(Integer.parseInt(erpPartnerId)), null);
			
			if ((records != null) && (records.length > 0)) {
				Map record = (Map) records[0];
				for (String field : partnerDefaultAttributes) {
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

		try {
			List<List<Object>> filterCollection = new ArrayList<List<Object>>();

			for (Filter filter : filters) {

				filterCollection.add(asList(filter.getFieldName(),
						filter.getComparison(),
						filter.getValue()));
			}
			ArrayList<String> fields = odooSession.getDomainFields(PARTNER_MODEL);
			Object[] records = (Object[]) odooSession.execute("search_read", PARTNER_MODEL, filterCollection, new HashMap() {{
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
