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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.openmrs.module.erp.api.ErpMaintenanceService;
import org.openmrs.module.erp.impl.odoo.Equipment;
import org.openmrs.module.erp.impl.odoo.MaintenanceRequest;
import org.openmrs.module.erp.impl.odoo.OdooConstants;
import org.openmrs.module.erp.web.rest.ErpRestConstants;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = ErpRestConstants.REST_NAMESPACE
        + "/maintenancerequest", supportedClass = MaintenanceRequest.class, supportedOpenmrsVersions = { "1.10.*", "1.11.*",
                "1.12.*", "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*", "2.5.*" })
public class MaintenanceRequestResource extends BaseNamedOdooResource<MaintenanceRequest> {
	
	/**
	 * @see DelegatingCrudResource#getRepresentationDescription(Representation)
	 */
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = super.getRepresentationDescription(rep);
		description.addRequiredProperty("equipment");
		description.addRequiredProperty("requestDate");
		description.addRequiredProperty("equipment");
		description.addProperty("scheduleDate");
		description.addProperty("duration");
		return description;
	}
	
	@PropertyGetter("equipment")
	public Map getEquipment(MaintenanceRequest delegate) {
		Equipment equipment = delegate.getEquipment();
		Map category = new HashMap();
		category.put("categoryId", equipment.getCategoryId());
		category.put("categoryName", equipment.getCategoryName());
		Map equipmentResource = new HashMap();
		equipmentResource.put("id", equipment.getId());
		equipmentResource.put("name", equipment.getName());
		equipmentResource.put("serialNo", equipment.getSerialNo());
		equipmentResource.put("location", equipment.getLocation());
		equipmentResource.put("category", category);
		equipmentResource.put("display", equipment.getName());
		return equipmentResource;
	}
	
	@PropertyGetter("requestDate")
	public String getRequestDate(MaintenanceRequest delegate) {
		return OdooConstants.DATE_FORMATTER.format(delegate.getRequestDate());
	}
	
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		List<MaintenanceRequest> requests = Context.getService(ErpMaintenanceService.class).getMaintenanceRequests();
		return new AlreadyPaged(context, requests, false);
	}
	
}
