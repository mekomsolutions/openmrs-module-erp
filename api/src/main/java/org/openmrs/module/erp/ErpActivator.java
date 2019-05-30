package org.openmrs.module.erp;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.erp.api.impl.odoo.OdooSession;
import org.openmrs.module.erp.api.utils.ErpPropertiesFile;
import org.openmrs.module.erp.exceptions.ErpPropertyNotFoundException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
@Component(ErpConstants.COMPONENT_ERP_ACTIVATOR)
public class ErpActivator extends BaseModuleActivator {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("Refreshing " + ErpConstants.MODULE_NAME + " Module");
	}
	
	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		log.info(ErpConstants.MODULE_NAME + " Module refreshed");
	}
	
	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("Starting " + ErpConstants.MODULE_NAME + " Module");
	}
	
	/**
	 * @see #started()
	 */
	public void started() {
		String errorMessage = null;
		String error = null;
		try {
			OdooSession odooSession = Context.getRegisteredComponent(ErpConstants.COMPONENT_ODOO_SESSION, OdooSession.class);
			odooSession.init();
		}
		catch (FileNotFoundException e) {
			errorMessage = "Unable to find ERP properties file.";
			error = ExceptionUtils.getStackTrace(e);
		}
		catch (IOException e) {
			errorMessage = "Unable to read ERP properties file.";
			error = ExceptionUtils.getStackTrace(e);
		}
		catch (ErpPropertyNotFoundException e) {
			errorMessage = "Unable to find all the required ERP session properties.";
			error = ExceptionUtils.getStackTrace(e);
		}
		catch (NumberFormatException e) {
			errorMessage = "Unable to convert property to the specified Type.";
			error = ExceptionUtils.getStackTrace(e);
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
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("Stopping " + ErpConstants.MODULE_NAME + " Module");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info(ErpConstants.MODULE_NAME + " Module stopped");
	}
}
