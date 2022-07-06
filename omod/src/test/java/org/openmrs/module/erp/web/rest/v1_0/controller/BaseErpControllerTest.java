package org.openmrs.module.erp.web.rest.v1_0.controller;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.openmrs.module.erp.web.rest.ErpRestConstants;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceControllerTest;

public abstract class BaseErpControllerTest extends MainResourceControllerTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	/**
	 * @see MainResourceControllerTest#getNamespace()
	 */
	@Override
	public String getNamespace() {
		return ErpRestConstants.REST_NAMESPACE;
	}
	
	@Override
	public void shouldGetDefaultByUuid() throws Exception {
		expectedException.expect(ResourceDoesNotSupportOperationException.class);
		super.shouldGetDefaultByUuid();
	}
	
	@Override
	public void shouldGetRefByUuid() throws Exception {
		expectedException.expect(ResourceDoesNotSupportOperationException.class);
		super.shouldGetRefByUuid();
	}
	
	@Override
	public void shouldGetFullByUuid() throws Exception {
		expectedException.expect(ResourceDoesNotSupportOperationException.class);
		super.shouldGetFullByUuid();
	}
	
}
