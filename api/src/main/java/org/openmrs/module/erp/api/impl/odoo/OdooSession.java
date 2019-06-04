package org.openmrs.module.erp.api.impl.odoo;

import com.odoojava.api.Session;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.api.ErpSession;
import org.openmrs.module.erp.api.utils.ErpPropertiesFile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.openmrs.module.erp.ErpConstants.*;

@Component(ErpConstants.COMPONENT_ODOO_SESSION)
public class OdooSession implements ErpSession {
	
	private Properties properties;
	
	private Session session;
	
	public OdooSession() {
	}
	
	@Override
	public void init() throws IOException {
		setErpSession(ErpPropertiesFile.getInputStream());
	}
	
	@Override
	public void init(InputStream inStream) throws IOException {
		setErpSession(inStream);
	}
	
	@Override
	public void setErpSession(InputStream inStream) throws IOException {
		
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
