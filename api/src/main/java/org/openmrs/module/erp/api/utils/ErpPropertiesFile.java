package org.openmrs.module.erp.api.utils;

import org.openmrs.module.erp.ErpConstants;
import org.openmrs.util.OpenmrsUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ErpPropertiesFile {

	public static InputStream getInputStream() throws FileNotFoundException {
		String fileName = ErpConstants.ERP_PROPERTY_FILE_NAME;
		File file = new File(OpenmrsUtil.getApplicationDataDirectory(), fileName);
		return new FileInputStream(file);
	}
}
