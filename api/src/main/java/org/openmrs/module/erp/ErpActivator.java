package org.openmrs.module.erp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.erp.api.utils.ErpPropertiesFile;
import org.openmrs.module.erp.api.utils.OdooConnection;
import org.openmrs.module.erp.exceptions.ErpNotFoundException;
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
			
			new OdooConnection();
			
		}
		catch (FileNotFoundException e) {
			errorMessage = "Unable to find ERP properties file: " + ErpPropertiesFile.getFile().getAbsolutePath();
			error = e.getMessage();
		}
		catch (IOException e) {
			errorMessage = "Unable to read ERP properties file: " + ErpPropertiesFile.getFile().getAbsolutePath();
			error = e.getMessage();
		}
		catch (NullPointerException e) {
			errorMessage = "Unable to get ERP connection properties";
			error = e.getMessage();
		}
		catch (ErpNotFoundException e) {
			errorMessage = "Unable to find ERP required connection property";
			error = e.getMessage();
		}
		catch (NumberFormatException e) {
			errorMessage = "Unable to convert property to a number";
			error = e.getMessage();
		}
		finally {
			if (errorMessage != null) {
				log.error(error);
				Module module = ModuleFactory.getModuleById(ErpConstants.MODULE_ARTIFACT_ID);
				module.setStartupErrorMessage("Loading properties file : " + errorMessage);
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
