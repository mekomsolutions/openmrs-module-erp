package org.openmrs.module.erp.api.impl.odoo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.erp.impl.odoo.MaintenanceRequest;

public class OdooJsonUtilsTest {
	
	@Test
	public void convertToList_shouldConvertTheTheDataToTheSpecifiedType() {
		Map<String, Object> data1 = new HashMap();
		data1.put("name", "name1");
		data1.put("duration", "1.0");
		data1.put("equipment_id", new Object[] { 1, "test1" });
		Map<String, Object> data2 = new HashMap();
		data2.put("name", "name2");
		data2.put("duration", "2.0");
		data2.put("equipment_id", new Object[] { 1, "test2" });
		List<MaintenanceRequest> requests = OdooJsonUtils.convertToList(new Object[] { data1, data2 },
		    MaintenanceRequest.class);
		Assert.assertEquals(2, requests.size());
		Assert.assertEquals("name1", requests.get(0).getName());
		Assert.assertNull(requests.get(0).getScheduleDate());
		Assert.assertEquals(new Double(1.0), requests.get(0).getDuration());
		Assert.assertEquals("test1", requests.get(0).getEquipmentName());
		Assert.assertEquals(1, requests.get(0).getEquipmentId().intValue());
		Assert.assertEquals(data1, requests.get(0).getData());
		
		Assert.assertEquals("name2", requests.get(1).getName());
		Assert.assertNull(requests.get(1).getScheduleDate());
		Assert.assertEquals(new Double(2.0), requests.get(1).getDuration());
		Assert.assertEquals("test2", requests.get(1).getEquipmentName());
		Assert.assertEquals(1, requests.get(1).getEquipmentId().intValue());
		Assert.assertEquals(data2, requests.get(1).getData());
	}
	
	@Test
	public void convertToList_shouldConvertFalseValuesToNull() {
		Map<String, Object> data1 = new HashMap();
		data1.put("name", "false");
		data1.put("schedule_date", "False");
		data1.put("duration", "false");
		data1.put("equipment_id", new Object[] { 1, "false" });
		Map<String, Object> data2 = new HashMap();
		data2.put("name", "false");
		data2.put("schedule_date", "False");
		data2.put("duration", "false");
		data2.put("equipment_id", new Object[] { 1, "false" });
		List<MaintenanceRequest> requests = OdooJsonUtils.convertToList(new Object[] { data1, data2 },
		    MaintenanceRequest.class);
		Assert.assertEquals(2, requests.size());
		Assert.assertNull(requests.get(0).getName());
		Assert.assertNull(requests.get(0).getScheduleDate());
		Assert.assertNull(requests.get(0).getDuration());
		Assert.assertNull(requests.get(0).getEquipmentName());
		Assert.assertEquals(1, requests.get(0).getEquipmentId().intValue());
		Assert.assertEquals(data1, requests.get(0).getData());
		
		Assert.assertNull(requests.get(1).getName());
		Assert.assertNull(requests.get(1).getScheduleDate());
		Assert.assertNull(requests.get(1).getDuration());
		Assert.assertNull(requests.get(1).getEquipmentName());
		Assert.assertEquals(1, requests.get(1).getEquipmentId().intValue());
		Assert.assertEquals(data2, requests.get(1).getData());
	}
	
}
