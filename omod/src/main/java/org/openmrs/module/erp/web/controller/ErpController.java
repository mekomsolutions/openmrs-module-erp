/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.erp.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.api.impl.odoo.OdooOrderServiceServiceImpl;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.openmrs.module.webservices.rest.web.RestConstants;

import javax.servlet.http.HttpServletResponse;

/**
 * This class configured as controller using annotation and mapped with the URL of
 * 'module/${rootArtifactid}/${rootArtifactid}Link.form'.
 */
@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/" + "erp")
public class ErpController extends BaseRestController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * All the parameters are optional based on the necessity
	 * 
	 * @param uuid
	 * @return
	 */
	@RequestMapping(value = ErpConstants.ERP_ORDERS_URI, method = RequestMethod.GET)
	public void getErpOrdersByPatientUuid(@PathVariable("uuid") String uuid, HttpServletResponse response)
	        throws ResponseException {
		OdooOrderServiceServiceImpl odooOrder = new OdooOrderServiceServiceImpl();
		odooOrder.getErpOrdersByPatientUuid(uuid);
	}
	
}
