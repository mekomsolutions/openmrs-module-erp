/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * 
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.erp.web.rest.v1_0.resource;

import java.util.List;

import org.openmrs.api.context.Context;
import org.openmrs.module.erp.api.ErpInventoryService;
import org.openmrs.module.erp.impl.odoo.InventoryAdjustment;
import org.openmrs.module.erp.web.rest.ErpRestConstants;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = ErpRestConstants.REST_NAMESPACE
        + "/inventoryadjustment", supportedClass = InventoryAdjustment.class, supportedOpenmrsVersions = { "1.10.*",
                "1.11.*", "1.12.*", "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*", "2.5.*" })
public class InventoryAdjustmentResource extends BaseNamedOdooResource<InventoryAdjustment> {
	
	/**
	 * @see DelegatingCrudResource#getRepresentationDescription(Representation)
	 */
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addRequiredProperty("date");
		}
		
		return description;
	}
	
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		List<InventoryAdjustment> adjustments = Context.getService(ErpInventoryService.class).getInventoryAdjustments();
		return new AlreadyPaged(context, adjustments, false);
	}
	
}
