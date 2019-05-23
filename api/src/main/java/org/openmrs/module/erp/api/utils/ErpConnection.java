package org.openmrs.module.erp.api.utils;

import com.odoojava.api.Session;
import org.openmrs.module.erp.ErpConstants;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component(ErpConstants.COMPONENT_ERP_CONNECTION)
public class ErpConnection {

	private static final String HOST_PROPERTY = "erp.host";

	private static final String PORT_PROPERTY = "erp.port";

	private static final String DATABASE_PROPERTY = "erp.database";
	
	private static final String USER_PROPERTY = "erp.user";

	private static final String PASSWORD_PROPERTY = "erp.password";

	private String host;

	private Integer port;

	private String database;

	private String user;

	private String password;

	private Session session;

	public ErpConnection() throws IOException {
		setErpConnection(ErpPropertiesFile.getInputStream());
	}

	public ErpConnection(InputStream inStream) throws IOException {
		setErpConnection(inStream);
	}

	private void setErpConnection(InputStream inStream) throws IOException {
		Properties properties = new Properties();
		properties.load(inStream);

		host = properties.getProperty(HOST_PROPERTY);
		port = Integer.parseInt(properties.getProperty(PORT_PROPERTY));
		database = properties.getProperty(DATABASE_PROPERTY);
		user = properties.getProperty(USER_PROPERTY);
		password = properties.getProperty(PASSWORD_PROPERTY);

		session = new Session(host, port, database, user, password);
	}

	public String getHost() {
		return host;
	}

	public Integer getPort() {
		return port;
	}

	public String getDatabase() {
		return database;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public Session getSession() {
		return session;
	}

}
