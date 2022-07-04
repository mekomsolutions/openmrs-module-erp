package org.openmrs.module.erp.api;

import org.apache.xmlrpc.XmlRpcException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.impl.odoo.OdooPartnerServiceImpl;
import org.openmrs.module.erp.api.impl.odoo.OdooClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.openmrs.module.erp.api.utils.TestHelper.getOdooRecord;
import static org.powermock.api.mockito.PowerMockito.when;

public class OdooPartnerServiceImplTest {
	
	private String[] fields = { "id", "name", "uuid", "create_date" };
	
	private ErpPartnerService odooPartnerService;
	
	@Before
	public void setup() throws XmlRpcException {
		// Setup mocks
		
		OdooClient odooSession = mock(OdooClient.class);
		when(odooSession.getUid()).thenReturn("1");
		when(odooSession.execute(any(String.class), any(String.class), any(), any())).thenReturn(getOdooRecord());
		when(odooSession.getDomainFields(any())).thenReturn(new ArrayList<>(asList(fields)));
		
		odooPartnerService = new OdooPartnerServiceImpl(odooSession);
	}
	
	@Test
	public void getPartnerByIdShouldReturnPartnerTest() {
		Map<String, Object> partners = odooPartnerService.getErpPartnerById("1");
		
		// Verify
		Assert.assertEquals("REC/001", String.valueOf(partners.get("name")));
		Assert.assertEquals("1", String.valueOf(partners.get("id")));
	}
	
	@Test
	public void getPartnersByFiltersShouldReturnPartners() {
		
		Filter filter = new Filter("amount_total", ">", "100");
		
		List<Map<String, Object>> partners = odooPartnerService.getErpPartnersByFilters(Collections.singletonList(filter));
		
		// Verify
		Assert.assertEquals("REC/001", String.valueOf(partners.get(0).get("name")));
		Assert.assertEquals("1", String.valueOf(partners.get(0).get("id")));
	}
	
}
