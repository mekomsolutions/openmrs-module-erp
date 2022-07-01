package org.openmrs.module.erp.api.impl.odoo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.erp.impl.odoo.MaintenanceRequest;

public class OdooJsonUtilsTest {
	
	@Test
	public void convertToList_shouldConvertFalseValuesToNull() {
		Map<String, Object> data = new HashMap();
		data.put("name", "false");
		data.put("schedule_date", "False");
		data.put("duration", "false");
		data.put("equipment_id", new Object[] { 1, "false" });
		List<MaintenanceRequest> requests = OdooJsonUtils.convertToList(new Object[] { data, data },
		    MaintenanceRequest.class);
		Assert.assertEquals(2, requests.size());
		for (MaintenanceRequest r : requests) {
			Assert.assertNull(r.getName());
			Assert.assertNull(r.getScheduleDate());
			Assert.assertNull(r.getDuration());
			Assert.assertNull(r.getEquipmentName());
			Assert.assertEquals(1, r.getEquipmentId().intValue());
		}
	}
	
}
