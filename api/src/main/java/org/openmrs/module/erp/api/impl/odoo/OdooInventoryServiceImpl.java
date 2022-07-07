package org.openmrs.module.erp.api.impl.odoo;

import static java.util.Arrays.asList;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.erp.api.ErpInventoryService;
import org.openmrs.module.erp.impl.odoo.InventoryAdjustment;
import org.springframework.beans.factory.annotation.Autowired;

public class OdooInventoryServiceImpl extends BaseOpenmrsService implements ErpInventoryService {
	
	public static final String MODEL_INVENTORY = "stock.inventory";
	
	private OdooClient odooClient;
	
	/**
	 * Sets the odooClient
	 * 
	 * @param odooClient the odooClient to set
	 */
	@Autowired
	public void setOdooClient(OdooClient odooClient) {
		this.odooClient = odooClient;
	}
	
	@Override
	public List<InventoryAdjustment> getInventoryAdjustments() throws APIException {
		try {
			Object[] adjustments = odooClient.searchAndRead(MODEL_INVENTORY, asList("state", "=", "confirm"), null);
			return OdooJsonUtils.convertToList(adjustments, InventoryAdjustment.class);
		}
		catch (XmlRpcException e) {
			throw new APIException(e);
		}
		catch (MalformedURLException e) {
			throw new APIException(e);
		}
	}
	
}
