package org.openmrs.module.erp.api;

import com.odoojava.api.Field;
import com.odoojava.api.FieldCollection;
import com.odoojava.api.FilterCollection;
import com.odoojava.api.ObjectAdapter;
import com.odoojava.api.OdooApiException;
import com.odoojava.api.Row;
import com.odoojava.api.RowCollection;
import com.odoojava.api.Session;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.erp.api.impl.odoo.OdooOrderServiceServiceImpl;
import org.openmrs.module.erp.api.utils.ErpProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class OdooOrderServiceImplTest {
	
	private void setErpProperties() {
		Properties erpProperties = new Properties();
		erpProperties.put("erp.host", "localhost");
		erpProperties.put("erp.port", "8069");
		erpProperties.put("erp.database", "odoo");
		erpProperties.put("erp.user", "admin");
		erpProperties.put("erp.password", "admin");
		ErpProperties.initalize(erpProperties);
		
	}
	
	private RowCollection getOrder() throws OdooApiException {
		
		RowCollection order = new RowCollection();
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		Map<String, Object> fieldData = new HashMap<String, Object>();
		fieldData.put("string", "Reference Order");
		fieldData.put("type", "string");
		fieldData.put("depends", new Object());
		fieldData.put("required", false);
		fieldData.put("sortable", true);
		fieldData.put("translate", false);
		fieldData.put("manual", false);
		fieldData.put("store", true);
		fieldData.put("readonly", true);
		fieldData.put("states", 1);
		fieldData.put("company_dependent", false);
		
		Field id = new Field("uuid", fieldData);
		Field name = new Field("name", fieldData);
		Field amountTotal = new Field("amount_total", fieldData);
		
		data.put("uuid", "101659fd-383a-4305-b512-51ea34f69908");
		data.put("name", "SO/001");
		data.put("amount_total", "3175.0");
		
		FieldCollection fields = new FieldCollection();
		fields.addAll(Arrays.asList(id, name, amountTotal));
		
		Row row = new Row(data, fields);
		order.add(row);
		
		return order;
	}
	
	@Test
	public void getOrdersTest() throws Exception {
		
		// Setup
		
		// set properties
		setErpProperties();
		
		// create mocked session
		Session session = mock(Session.class);
		ObjectAdapter objectAdapter = mock(ObjectAdapter.class);
		when(objectAdapter.searchAndReadObject(any(FilterCollection.class), any(String[].class))).thenReturn(getOrder());
		when(session.getObjectAdapter(any(String.class))).thenReturn(objectAdapter);
		
		OdooOrderServiceServiceImpl odooOrderService = new OdooOrderServiceServiceImpl(session);
		
		// Replay
		
		ArrayList<JSONObject> listPricesForOrderables = odooOrderService
		        .getErpOrdersByPatientUuid("101659fd-383a-4305-b512-51ea34f69908");
		
		// Verify
		
		Assert.assertEquals(String.valueOf(listPricesForOrderables.get(0).get("name")), "SO/001");
		Assert.assertEquals(String.valueOf(listPricesForOrderables.get(0).get("amount_total")), "3175.0");
	}
	
}
