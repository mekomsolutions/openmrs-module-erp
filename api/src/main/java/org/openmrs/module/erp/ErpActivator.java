package org.openmrs.module.erp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.erp.api.utils.ErpPropertiesFile;
import org.openmrs.module.erp.api.utils.OdooSession;
import org.openmrs.module.erp.exceptions.ErpPropertyNotFoundException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
@Component
public class ErpActivator extends BaseModuleActivator {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see #willRefreshContext()
	 */
	@Override
	public void willRefreshContext() {
		String errorMessage = null;
		String error = null;
		try {
			// Ensure that Odoo connection can be instantiated before Spring beans initialization
			// and catch thrown exceptions
			
			new OdooSession();
			
		}
		catch (FileNotFoundException e) {
			errorMessage = "Unable to find ERP properties file.";
			error = e.getMessage();
		}
		catch (IOException e) {
			errorMessage = "Unable to read ERP properties file.";
			error = e.getMessage();
		}
		catch (NullPointerException e) {
			errorMessage = "Unable to get ERP session properties.";
			error = e.getMessage();
		}
		catch (ErpPropertyNotFoundException e) {
			errorMessage = "Unable to find all the required ERP session properties.";
			error = e.getMessage();
		}
		catch (NumberFormatException e) {
			errorMessage = "Unable to convert property to the specified Type.";
			error = e.getMessage();
		}
		finally {
			if (errorMessage != null) {
				log.error(error);
				Module module = ModuleFactory.getModuleById(ErpConstants.MODULE_ARTIFACT_ID);
				module.setStartupErrorMessage(ErpPropertiesFile.getFile().getAbsolutePath() + ": " + errorMessage);
				ModuleFactory.stopModule(module, true, true);
			}
		}
	}
	
	/**
	 * @see #started()
	 */
	@Override
	public void started() {
		log.info("Started OpenMRS ERP");
	}
	
}
