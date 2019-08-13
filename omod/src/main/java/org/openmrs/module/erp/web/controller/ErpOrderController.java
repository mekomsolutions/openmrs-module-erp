package org.openmrs.module.erp.web.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.ErpOrderService;
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
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + ErpConstants.ERP_URI + ErpConstants.ERP_ORDER_URI)
public class ErpOrderController extends BaseRestController {
	
	@Autowired
	protected ErpContext erpContext;
	
	@Autowired
	private ErpOrderService erpOrderService;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Object getErpOrdersByFilters(@RequestBody String jsonString,
			@RequestParam(defaultValue = "default") String rep) {
		erpOrderService = erpContext.getErpOrderService();
		RecordRepresentation recordRepresentation = new RecordRepresentation(erpOrderService.defaultModelAttributes());

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

		List<Map<String, Object>> results = erpOrderService.getErpOrdersByFilters(filtersArray);

		for (Map<String, Object> result :
				results) {
			records.add(recordRepresentation.getRepresentedRecord(result, rep));
		}

		return records;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Object getErpOrderById(@PathVariable("id") String id, @RequestParam(defaultValue = "default") String rep) {
		erpOrderService = erpContext.getErpOrderService();
		return new RecordRepresentation(erpOrderService.defaultModelAttributes()).getRepresentedRecord(
		    erpOrderService.getErpOrderById(id), rep);
	}
	
}
