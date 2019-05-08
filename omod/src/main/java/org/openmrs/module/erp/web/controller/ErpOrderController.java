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
import org.json.JSONObject;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class ErpOrderController extends BaseRestController {
	
	@Autowired
	protected ErpContext erpContext;
	
	@Autowired
	private ErpOrderService erpOrderService;
	
	@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/" + "erp" + ErpConstants.ERP_ORDERS_URI, method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<JSONObject> getErpOrdersByPatientUuid(@PathVariable("uuid") String uuid) throws ResponseException {
		erpOrderService = erpContext.getErpOrderService();
		return erpOrderService.getErpOrdersByPatientUuid(uuid);
	}
	
	@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/" + "erp" + ErpConstants.ERP_ORDER_URI, method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getErpOrderById(@PathVariable("id") String id) throws ResponseException {
		erpOrderService = erpContext.getErpOrderService();
		return erpOrderService.getErpOrderById(id);
	}
	
}
