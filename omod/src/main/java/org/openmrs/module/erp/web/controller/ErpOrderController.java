/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.erp.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.ErpContext;
import org.openmrs.module.erp.api.ErpOrderService;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

import java.util.ArrayList;

@Controller(value = "/rest/" + RestConstants.VERSION_1 + "/" + "erp" + ErpConstants.ERP_ORDER_URI)
public class ErpOrderController extends BaseRestController {
	
	@Autowired
	protected ErpContext erpContext;
	
	@Autowired
	private ErpOrderService erpOrderService;
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<JSONObject> getErpOrdersByFilters(@RequestBody String jsonString,
	        @RequestParam(value = "rep", defaultValue = "default") String representation) throws ResponseException {
		erpOrderService = erpContext.getErpOrderService();
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
		
		return erpOrderService.getErpOrdersByFilters(filtersArray);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getErpOrderById(@PathVariable("id") String id,
	        @RequestParam(value = "rep", defaultValue = "default") String representation) throws ResponseException {
		erpOrderService = erpContext.getErpOrderService();
		return erpOrderService.getErpOrderById(id);
	}
	
}
