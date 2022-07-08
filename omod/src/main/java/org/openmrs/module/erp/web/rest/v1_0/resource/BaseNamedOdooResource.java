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

import static org.openmrs.module.erp.web.rest.ErpRestConstants.REPRESENTATION_ERP;
import static org.openmrs.module.erp.web.rest.ErpRestConstants.REPRESENTATION_ERP_PREFIX;

import org.openmrs.module.erp.impl.odoo.BaseNamedOdooModel;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.RepHandler;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.NamedRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ConversionException;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

public abstract class BaseNamedOdooResource<R extends BaseNamedOdooModel> extends DelegatingCrudResource<R> {
	
	/**
	 * @see DelegatingCrudResource#getRepresentationDescription(Representation)
	 */
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		if (rep instanceof RefRepresentation || rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addRequiredProperty("id");
			description.addRequiredProperty("name");
			description.addProperty("display");
			
			return description;
		}
		
		return null;
	}
	
	@PropertyGetter("display")
	public String getDisplayString(R delegate) {
		return delegate.getName();
	}
	
	@RepHandler(name = REPRESENTATION_ERP)
	public SimpleObject asRepresentation(R delegate) {
		SimpleObject simpleObject = new SimpleObject();
		simpleObject.putAll(delegate.getData());
		
		return simpleObject;
	}
	
	/**
	 * @see DelegatingCrudResource#asRepresentation(Object, Representation)
	 */
	@Override
	public SimpleObject asRepresentation(R delegate, Representation representation) throws ConversionException {
		String rep = representation.getRepresentation();
		if (representation instanceof NamedRepresentation && rep.startsWith(REPRESENTATION_ERP_PREFIX)) {
			SimpleObject simpleObject = new SimpleObject();
			for (String propertyName : rep.replace(REPRESENTATION_ERP_PREFIX, "").split(",")) {
				simpleObject.put(propertyName, delegate.getValue(propertyName));
			}
			
			return simpleObject;
		}
		
		return super.asRepresentation(delegate, representation);
	}
	
	/**
	 * @see DelegatingCrudResource@getCreatableProperties()
	 */
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		throw new ResourceDoesNotSupportOperationException("read-only resource");
	}
	
	/**
	 * @see DelegatingCrudResource#newDelegate()
	 */
	@Override
	public R newDelegate() {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	/**
	 * @see DelegatingCrudResource#save(Object)
	 */
	@Override
	public R save(R delegate) {
		throw new ResourceDoesNotSupportOperationException("read-only resource");
	}
	
	/**
	 * @see DelegatingCrudResource#getByUniqueId(String)
	 */
	@Override
	public R getByUniqueId(String uniqueId) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	/**
	 * @see DelegatingCrudResource#delete(Object, String, RequestContext)
	 */
	@Override
	protected void delete(R delegate, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException("read-only resource");
	}
	
	/**
	 * @see DelegatingCrudResource#purge(Object, RequestContext)
	 */
	@Override
	public void purge(R delegate, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException("read-only resource");
	}
	
}
