package org.openmrs.module.erp.web.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpInvoiceService;
import org.openmrs.module.erp.web.RecordRepresentation;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + ErpConstants.ERP_URI + ErpConstants.ERP_INVOICE_URI)
public class ErpInvoiceController {
	
	@Autowired
	protected ErpContext erpContext;
	
	@Autowired
	private ErpInvoiceService erpInvoiceService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Object getInvoicesByFilters(@RequestBody String jsonString,
	                                   @RequestParam(value = "rep", defaultValue = "default") String rep) {
		erpInvoiceService = erpContext.getErpInvoiceService();
		
		RecordRepresentation recordRepresentation = new RecordRepresentation(erpInvoiceService.defaultModelAttributes());
		
		List<Filter> filtersArray = new ArrayList<>();
		
		List<Map<String, Object>> records = new ArrayList<>();
		
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
		
		for (Map<String, Object> result : results) {
			records.add(recordRepresentation.getRepresentedRecord(result, rep));
		}
		
		return records;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object getInvoiceById(@PathVariable("id") String invoiceId,
	                             @RequestParam(value = "rep", defaultValue = "default") String rep) {
		
		erpInvoiceService = erpContext.getErpInvoiceService();
		
		return new RecordRepresentation(erpInvoiceService.defaultModelAttributes())
		        .getRepresentedRecord(erpInvoiceService.getInvoiceById(invoiceId), rep);
	}
	
}
