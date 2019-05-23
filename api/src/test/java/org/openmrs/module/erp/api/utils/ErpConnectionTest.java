package org.openmrs.module.erp.api.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.util.OpenmrsUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ErpConnectionTest {

	@Before
	public void before() {
		TestHelper.createErpPropertiesFile();
	}

	@After
	public void after() {
		TestHelper.removeErpPropertiesFile();
	}

	@Test
	public void shouldHandleExceptions() throws IOException {
		TestHelper.createErpPropertiesFile();
		ErpConnection erpConnection = new ErpConnection(ErpPropertiesFile.getInputStream());
		Assert.assertEquals("localhost", erpConnection.getHost());
	}

	@Test(expected = IOException.class)
	public void shouldThrowIOException() throws IOException {
		TestHelper.removeErpPropertiesFile();
		ErpConnection erpConnection = new ErpConnection(ErpPropertiesFile.getInputStream());
	}

	@Test(expected = FileNotFoundException.class)
	public void shouldThrowFileNotFoundException() throws IOException {
		File file = new File(OpenmrsUtil.getApplicationDataDirectory(), "erp.properties");
		updatePosiXPermissions(file);
		TestHelper.createErpPropertiesFile();
		FileInputStream inStream = new FileInputStream(file);
		ErpConnection erpConnection = new ErpConnection(inStream);
	}

	private static File updatePosiXPermissions(File file) {
		file.setExecutable(false);
		file.setReadable(false);
		file.setWritable(false);
		return file;
	}
}
