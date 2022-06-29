package org.openmrs.module.erp.api.impl.odoo;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openmrs.module.erp.ErpConstants;
import org.openmrs.module.erp.api.impl.odoo.OdooClient;
import org.powermock.reflect.Whitebox;

public class OdooClientTest {
	
	private OdooClient odooClient;
	
	@Mock
	private Properties mockProperties;
	
	@Mock
	private XmlRpcClient mockXmlRpcClient;
	
	private static final String ODOO_PASSWORD = "pass";
	
	private static final String ODOO_DB = "test_db";
	
	private static final Integer ODOO_USER_ID = 19;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		odooClient = new OdooClient();
		Whitebox.setInternalState(odooClient, Properties.class, mockProperties);
		Whitebox.setInternalState(odooClient, XmlRpcClient.class, mockXmlRpcClient);
		Whitebox.setInternalState(odooClient, "uid", ODOO_USER_ID);
	}
	
	@Test
	public void searchAndRead_shouldExecuteTheOdooRpcSearchReadCall() throws Exception {
		final String modelName = "maintenance.equipment";
		final String idField = "id";
		final String op = "=";
		final Integer id = 1;
		final String nameField = "name";
		when(mockProperties.getProperty(ErpConstants.PASSWORD_PROPERTY)).thenReturn(ODOO_PASSWORD);
		when(mockProperties.getProperty(ErpConstants.DATABASE_PROPERTY)).thenReturn(ODOO_DB);
		
		odooClient.searchAndRead(modelName, asList(idField, op, id), singletonList(nameField));
		
		Mockito.verify(mockXmlRpcClient).execute("execute_kw",
		    asList(ODOO_DB, ODOO_USER_ID, ODOO_PASSWORD, modelName, "search_read",
		        singletonList(singletonList(asList(idField, op, id))), singletonMap("fields", singletonList(nameField))));
	}
	
	@Test
	public void searchAndRead_shouldAuthenticateWithOdooIfNecessary() throws Exception {
		final String modelName = "maintenance.equipment";
		final String idField = "id";
		final String op = "=";
		final Integer id = 1;
		final String nameField = "name";
		final String host = "http://test.server";
		final Integer port = 8071;
		Whitebox.setInternalState(odooClient, "uid", (Integer) null);
		when(mockProperties.getProperty(ErpConstants.PASSWORD_PROPERTY)).thenReturn(ODOO_PASSWORD);
		when(mockProperties.getProperty(ErpConstants.DATABASE_PROPERTY)).thenReturn(ODOO_DB);
		when(mockProperties.getProperty(ErpConstants.HOST_PROPERTY)).thenReturn(host);
		when(mockProperties.getProperty(ErpConstants.PORT_PROPERTY)).thenReturn(port.toString());
		when(mockXmlRpcClient.execute(any(XmlRpcClientConfig.class), eq("authenticate"), anyList()))
		        .thenReturn(ODOO_USER_ID);
		
		odooClient.searchAndRead(modelName, asList(idField, op, id), singletonList(nameField));
		
		Mockito.verify(mockXmlRpcClient).execute("execute_kw",
		    asList(ODOO_DB, ODOO_USER_ID, ODOO_PASSWORD, modelName, "search_read",
		        singletonList(singletonList(asList(idField, op, id))), singletonMap("fields", singletonList(nameField))));
	}
	
	@Test
	public void search_shouldExecuteTheOdooRpcSearchCall() throws Exception {
		final String modelName = "maintenance.stage";
		final String doneField = "done";
		final String op = "=";
		when(mockProperties.getProperty(ErpConstants.PASSWORD_PROPERTY)).thenReturn(ODOO_PASSWORD);
		when(mockProperties.getProperty(ErpConstants.DATABASE_PROPERTY)).thenReturn(ODOO_DB);
		
		odooClient.search(modelName, asList(doneField, op, false));
		
		Mockito.verify(mockXmlRpcClient).execute("execute_kw", asList(ODOO_DB, ODOO_USER_ID, ODOO_PASSWORD, modelName,
		    "search", singletonList(singletonList(asList(doneField, op, false)))));
	}
	
	@Test
	public void search_shouldAuthenticateWithOdooIfNecessary() throws Exception {
		final String modelName = "maintenance.stage";
		final String doneField = "done";
		final String op = "=";
		final String host = "http://test.server";
		final Integer port = 8071;
		Whitebox.setInternalState(odooClient, "uid", (Integer) null);
		when(mockProperties.getProperty(ErpConstants.PASSWORD_PROPERTY)).thenReturn(ODOO_PASSWORD);
		when(mockProperties.getProperty(ErpConstants.DATABASE_PROPERTY)).thenReturn(ODOO_DB);
		when(mockProperties.getProperty(ErpConstants.HOST_PROPERTY)).thenReturn(host);
		when(mockProperties.getProperty(ErpConstants.PORT_PROPERTY)).thenReturn(port.toString());
		when(mockXmlRpcClient.execute(any(XmlRpcClientConfig.class), eq("authenticate"), anyList()))
		        .thenReturn(ODOO_USER_ID);
		
		odooClient.search(modelName, asList(doneField, op, false));
		
		Mockito.verify(mockXmlRpcClient).execute("execute_kw", asList(ODOO_DB, ODOO_USER_ID, ODOO_PASSWORD, modelName,
		    "search", singletonList(singletonList(asList(doneField, op, false)))));
	}
	
}
