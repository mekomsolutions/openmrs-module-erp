package org.openmrs.module.erp;

import org.openmrs.api.context.Context;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.emrapi.utils.ModuleProperties;
import org.openmrs.module.erp.api.ErpInvoiceService;
import org.openmrs.module.erp.api.ErpOrderService;
import org.openmrs.module.erp.api.impl.odoo.OdooSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(ErpConstants.COMPONENT_ERP_CONTEXT)
public class ErpContext extends ModuleProperties {
	
	@Autowired
	@Qualifier(ErpConstants.COMPONENT_ODOO_ORDER_SERVICE)
	protected ErpOrderService erpOrderService;
	
	@Autowired
	@Qualifier(ErpConstants.COMPONENT_ODOO_INVOICE_SERVICE)
	protected ErpInvoiceService erpInvoiceService;
	
	public ErpOrderService getErpOrderService() {
		return erpOrderService;
	}
	
	public ErpInvoiceService getErpInvoiceService() {
		return erpInvoiceService;
	}
}
