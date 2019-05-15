package org.openmrs.module.erp.web.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.module.erp.api.ErpInvoiceService;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

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
	public ArrayList<JSONObject> getInvoicesByFilters(@RequestBody String jsonString, @RequestBody String representation) {
		erpInvoiceService = erpContext.getErpInvoiceService();
		
		ArrayList<Object> filtersArray = new ArrayList<Object>();
		
		JSONArray jsonFilters = new JSONArray();
		JSONObject jsonObject = new JSONObject(jsonString);
		if (jsonObject.getJSONArray("filters") != null) {
			jsonFilters = jsonObject.getJSONArray("filters");
		}
		
		for (int i = 0; i < jsonFilters.length(); i++) {
			JSONObject filters = jsonFilters.getJSONObject(i);
			String field = filters.getString("field");
			String comparison = filters.getString("comparison");
			String value = filters.getString("value");
			filtersArray.add(new Object[] { field, comparison, value });
		}
		return erpInvoiceService.getInvoicesByFilters(filtersArray);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getInvoiceById(@PathVariable("id") String invoiceId) {
		erpInvoiceService = erpContext.getErpInvoiceService();
		return erpInvoiceService.getInvoiceById(invoiceId);
	}
	
}
