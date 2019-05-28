package org.openmrs.module.erp.api.utils;

import com.odoojava.api.Session;
import org.openmrs.module.erp.ErpConstants;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.openmrs.module.erp.ErpConstants.DATABASE_PROPERTY;
import static org.openmrs.module.erp.ErpConstants.HOST_PROPERTY;
import static org.openmrs.module.erp.ErpConstants.PASSWORD_PROPERTY;
import static org.openmrs.module.erp.ErpConstants.PORT_PROPERTY;
import static org.openmrs.module.erp.ErpConstants.USER_PROPERTY;

@Component(ErpConstants.COMPONENT_ERP_CONNECTION)
public class OdooConnection implements ErpConnection {
	
	private Properties properties;
	
	private Session session;
	
	public OdooConnection() throws IOException {
		setErpConnection(ErpPropertiesFile.getInputStream());
	}
	
	public OdooConnection(InputStream inStream) throws IOException {
		setErpConnection(inStream);
	}
	
	@Override
	public void setErpConnection(InputStream inStream) throws IOException {
		
		Map<String, Class> props = new HashMap<String, Class>();
		props.put(HOST_PROPERTY, String.class);
		props.put(PORT_PROPERTY, Integer.class);
		props.put(DATABASE_PROPERTY, String.class);
		props.put(USER_PROPERTY, String.class);
		props.put(PASSWORD_PROPERTY, String.class);
		
		this.properties = ErpPropertiesFile.getProperties(inStream, props);
		
		this.session = new Session(getHost(), getPort(), getDatabase(), getUser(), getPassword());
	}
	
	@Override
	public String getHost() {
		return properties.getProperty(HOST_PROPERTY);
	}
	
	@Override
	public Integer getPort() {
		return Integer.parseInt(properties.getProperty(PORT_PROPERTY));
	}
	
	@Override
	public String getUser() {
		return properties.getProperty(USER_PROPERTY);
	}
	
	@Override
	public String getPassword() {
		return properties.getProperty(PASSWORD_PROPERTY);
	}
	
	public String getDatabase() {
		return properties.getProperty(DATABASE_PROPERTY);
	}
	
	public Session getSession() {
		return session;
	}
	
}
