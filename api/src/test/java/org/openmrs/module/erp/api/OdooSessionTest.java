package org.openmrs.module.erp.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.erp.api.impl.odoo.OdooSession;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OdooSessionTest {
	
	private File erpPropFile;
	
	private OdooSession odooSession = new OdooSession();
	
	@Before
	public void before() throws FileNotFoundException {
		erpPropFile = ResourceUtils.getFile("classpath:erp.properties");
		updatePosiXPermissions(erpPropFile, false, true, true);
	}
	
	@Test
	public void shouldConstructErpConnection() throws IOException {
		odooSession.init(new FileInputStream(erpPropFile));
		Assert.assertEquals("http://localhost", odooSession.getHost());
	}
	
	@Test(expected = IOException.class)
	public void shouldThrowIOException() throws IOException {
		File nonExistingFile = ResourceUtils.getFile("classpath:does-not-exist.properties");
		odooSession.init(new FileInputStream(nonExistingFile));
	}
	
	@Test(expected = FileNotFoundException.class)
	public void shouldThrowFileNotFoundException() throws IOException {
		updatePosiXPermissions(erpPropFile, false, false, false);
		odooSession.init(new FileInputStream(erpPropFile));
	}
	
	private static File updatePosiXPermissions(File file, boolean exec, boolean read, boolean write) {
		file.setExecutable(exec);
		file.setReadable(read);
		file.setWritable(write);
		return file;
	}
}
