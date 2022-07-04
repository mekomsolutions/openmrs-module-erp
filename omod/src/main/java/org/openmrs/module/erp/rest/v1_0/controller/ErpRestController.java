package org.openmrs.module.erp.rest.v1_0.controller;

import org.openmrs.module.erp.rest.ErpRestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rest/" + ErpRestConstants.REST_NAMESPACE)
public class ErpRestController extends MainResourceController {
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController#getNamespace()
	 */
	@Override
	public String getNamespace() {
		return ErpRestConstants.REST_NAMESPACE;
	}
	
}
