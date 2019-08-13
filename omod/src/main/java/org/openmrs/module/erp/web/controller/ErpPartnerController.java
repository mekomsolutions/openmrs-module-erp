package org.openmrs.module.erp.web.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpPartnerService;
import org.openmrs.module.erp.web.RecordRepresentation;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + ErpConstants.ERP_URI + ErpConstants.ERP_PARTNER_URI)
public class ErpPartnerController extends BaseRestController {
	
	@Autowired
	protected ErpContext erpContext;
	
	@Autowired
	private ErpPartnerService erpPartnerService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Object getErpPartnersByFilters(@RequestBody String jsonString,
										  @RequestParam(defaultValue = "default") String rep) {
		erpPartnerService = erpContext.getErpPartnerService();
		RecordRepresentation recordRepresentation = new RecordRepresentation(erpPartnerService.defaultModelAttributes());

		ArrayList<Filter> filtersArray = new ArrayList<>();

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

		List<Map<String, Object>> results = erpPartnerService.getErpPartnersByFilters(filtersArray);

		for (Map<String, Object> result :
				results) {
			records.add(recordRepresentation.getRepresentedRecord(result, rep));
		}

		return records;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object getErpPartnerById(@PathVariable("id") String id, @RequestParam(defaultValue = "default") String rep) {
		erpPartnerService = erpContext.getErpPartnerService();
		return new RecordRepresentation(erpPartnerService.defaultModelAttributes()).getRepresentedRecord(
		    erpPartnerService.getErpPartnerById(id), rep);
	}
	
}
