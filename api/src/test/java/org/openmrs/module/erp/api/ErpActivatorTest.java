package org.openmrs.module.erp.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.erp.ErpActivator;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;

public class ErpActivatorTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	@InjectMocks
	protected ErpActivator activator;
	
	protected Module module;
	
	@Before
	public void setup() {
		module = new Module(ErpConstants.MODULE_ARTIFACT_ID);
		ModuleFactory.getLoadedModulesMap().put(ErpConstants.MODULE_ARTIFACT_ID, module);
	}
	
	@Test
	public void shouldSetStartupErrorMessage() throws IOException {
		activator.started();
		assertNotNull(module.getStartupErrorMessage());
	}
}
