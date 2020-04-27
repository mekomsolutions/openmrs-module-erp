package org.openmrs.module.erp;

import org.openmrs.module.emrapi.utils.ModuleProperties;
import org.openmrs.module.erp.api.ErpInvoiceService;
import org.openmrs.module.erp.api.ErpOrderService;
import org.openmrs.module.erp.api.ErpPartnerService;
import org.openmrs.module.erp.api.ErpProductService;
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
	
	@Autowired
	@Qualifier(ErpConstants.COMPONENT_ODOO_PARTNER_SERVICE)
	protected ErpPartnerService erpPartnerService;
	
	@Autowired
	@Qualifier(ErpConstants.COMPONENT_ODOO_PRODUCT_SERVICE)
	protected ErpProductService erpProductService;
	
	public ErpOrderService getErpOrderService() {
		return erpOrderService;
	}
	
	public ErpInvoiceService getErpInvoiceService() {
		return erpInvoiceService;
	}
	
	public ErpPartnerService getErpPartnerService() {
		return erpPartnerService;
	}
	
	public ErpProductService getErpProductService() {
		return erpProductService;
	}
}
