package org.openmrs.module.erp.api.utils;

import java.io.IOException;
import java.io.InputStream;

public interface ErpConnection {
	
	/**
	 * Setup a connection to the ERP server
	 * 
	 * @param inStream The Connection properties' InputStream
	 * @throws IOException
	 */
	void setErpConnection(InputStream inStream) throws IOException;
	
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
