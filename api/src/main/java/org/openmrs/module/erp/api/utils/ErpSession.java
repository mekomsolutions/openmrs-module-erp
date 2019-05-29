package org.openmrs.module.erp.api.utils;

import java.io.IOException;
import java.io.InputStream;

public interface ErpSession {
	
	/**
	 * Setup a session used to connect to the ERP server
	 * 
	 * @param inStream The session properties' InputStream
	 * @throws IOException
	 */
	void setErpSession(InputStream inStream) throws IOException;
	
	/**
	 * Return the ERP host
	 */
	String getHost();
	
	/**
	 * Return the ERP port
	 */
	Integer getPort();
	
	/**
	 * Return the ERP user
	 */
	String getUser();
	
	/**
	 * Return the ERP user's password
	 */
	String getPassword();
	
}
