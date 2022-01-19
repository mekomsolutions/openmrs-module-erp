package org.openmrs.module.erp.api.impl.odoo;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.api.ErpSession;
import org.openmrs.module.erp.api.utils.ErpPropertiesFile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.openmrs.module.erp.ErpConstants.*;

@Component(ErpConstants.COMPONENT_ODOO_SESSION)
public class OdooSession implements ErpSession {
	
	private Properties properties;
	
	private int uid;
	
	private XmlRpcClient client;
	
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
		
		this.client = new XmlRpcClient() {
			
			{
				setConfig(new XmlRpcClientConfigImpl() {
					
					{
						setServerURL(new URL(String.format("%s/xmlrpc/2/object",
						    getHost().concat(":").concat(String.valueOf(getPort())
						    
						    ))));
					}
				});
			}
		};
		
		final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
		common_config.setServerURL(new URL(String.format("%s/xmlrpc/2/common",
		    getHost().concat(":").concat(String.valueOf(getPort())
		    
		    ))));
		try {
			uid = (Integer) client.execute(common_config, "authenticate",
			    asList(getDatabase(), getUser(), getPassword(), emptyMap()));
		}
		catch (XmlRpcException e) {
			e.printStackTrace();
		}
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
	
	public Object execute(String method, String model, List dataParams, HashMap requestParams) throws XmlRpcException {
		
		List<Object> params;
		
		if (requestParams == null) {
			params = Collections.singletonList(dataParams);
			return client.execute("execute_kw", asList(getDatabase(), uid, getPassword(), model, method, params));
		} else {
			params = asList(dataParams);
			return client.execute("execute_kw",
			    asList(getDatabase(), uid, getPassword(), model, method, params, requestParams));
		}
	}
	
	public ArrayList<String> getDomainFields(String model) throws XmlRpcException {

		Map<String, Map<String, Object>> fieldsResult = (Map<String, Map<String, Object>>) client.execute("execute_kw", asList(
				getDatabase(), uid, getPassword(),
				model, "fields_get", emptyList(),
				new HashMap() {{
					put("attributes", asList("string"));
				}}
		));

		return new ArrayList<>(fieldsResult.keySet());

	}
}
