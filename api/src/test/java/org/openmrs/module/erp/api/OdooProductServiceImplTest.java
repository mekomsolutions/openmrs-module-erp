package org.openmrs.module.erp.api;

import com.odoojava.api.FilterCollection;
import com.odoojava.api.ObjectAdapter;
import com.odoojava.api.OdooApiException;
import com.odoojava.api.Session;
import org.apache.xmlrpc.XmlRpcException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.erp.Filter;
import org.openmrs.module.erp.api.impl.odoo.OdooPartnerServiceImpl;
import org.openmrs.module.erp.api.impl.odoo.OdooSession;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import org.openmrs.module.erp.api.impl.odoo.OdooProductServiceImpl;
import static org.openmrs.module.erp.api.utils.TestHelper.getOdooRecord;
import static org.powermock.api.mockito.PowerMockito.when;

public class OdooProductServiceImplTest {
	
	private String[] fields = { "id", "uuid", "name", "actual_stock", "qty_available", "price", "mrp" };
	
	private ErpProductService odooProductService;
	
	@Before
	public void setup() throws XmlRpcException, OdooApiException {
		// Setup mocks
		Session session = mock(Session.class);
		ObjectAdapter objectAdapter = mock(ObjectAdapter.class);
		when(objectAdapter.searchAndReadObject(any(FilterCollection.class), any(String[].class)))
		        .thenReturn(getOdooRecord());
		when(objectAdapter.getFieldNames()).thenReturn(fields);
		when(session.getObjectAdapter(any(String.class))).thenReturn(objectAdapter);
		
		OdooSession odooSession = mock(OdooSession.class);
		when(odooSession.getSession()).thenReturn(session);
		
		odooProductService = new OdooProductServiceImpl(odooSession);
	}
	
	@Test
	public void getProductByIdShouldReturnPartnerTest() {
		Map<String, Object> products = odooProductService.getErpProductById("1");
		
		// Verify
		Assert.assertEquals("REC/001", String.valueOf(products.get("name")));
		Assert.assertEquals("1", String.valueOf(products.get("id")));
	}
	
	@Test
	public void getPartnersByFiltersShouldReturnPartners() {
		
		Filter filter = new Filter("actual_stock", ">", "1");
		
		List<Map<String, Object>> products = odooProductService.getErpProductByFilters(Collections.singletonList(filter));
		
		// Verify
		Assert.assertEquals("REC/001", String.valueOf(products.get(0).get("name")));
		Assert.assertEquals("1", String.valueOf(products.get(0).get("id")));
	}
	
}
