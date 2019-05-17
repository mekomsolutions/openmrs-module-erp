package org.openmrs.module.erp.web.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpInvoiceService;
import org.openmrs.module.erp.web.JSONRepresentation;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/" + ErpConstants.MODULE_ARTIFACT_ID + "/"
        + ErpConstants.ERP_INVOICE_URI)
public class ErpInvoiceController {
	
	@Autowired
	protected ErpContext erpContext;
	
	@Autowired
	private ErpInvoiceService erpInvoiceService;
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public List<JSONObject> getInvoicesByFilters(@RequestBody String jsonString, @RequestParam String rep) {
		erpInvoiceService = erpContext.getErpInvoiceService();

		JSONRepresentation jsonRepresentation = new JSONRepresentation(erpInvoiceService.defaultModelAttributes());

		List<Filter> filtersArray = new ArrayList<>();

		List<JSONObject> records = new ArrayList<>();

		JSONArray jsonFilters = new JSONArray();
		JSONObject jsonObject = new JSONObject(jsonString);
		if (jsonObject.has("filters")) {
			jsonFilters = jsonObject.getJSONArray("filters");
		}

		for (int i = 0; i < jsonFilters.length(); i++) {
			JSONObject jsonFilter = jsonFilters.getJSONObject(i);
			Filter filter = new Filter(jsonFilter.getString("field"), jsonFilter.getString("comparison"),
					jsonFilter.get("value"));
			filtersArray.add(filter);
		}

		List<Map<String, Object>> results = erpInvoiceService.getInvoicesByFilters(filtersArray);

		for (Map<String, Object> result :
				results) {
			records.add(jsonRepresentation.getRepresentedRecord(result, rep));
		}

		return records;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getInvoiceById(@PathVariable("id") String invoiceId, @RequestBody String rep) {
		
		erpInvoiceService = erpContext.getErpInvoiceService();
		
		return new JSONRepresentation(erpInvoiceService.defaultModelAttributes()).getRepresentedRecord(
		    erpInvoiceService.getInvoiceById(invoiceId), rep);
	}
	
}
