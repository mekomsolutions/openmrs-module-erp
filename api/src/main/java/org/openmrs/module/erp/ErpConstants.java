package org.openmrs.module.erp;

/**
 * Contains module's constants.
 */
public class ErpConstants {
	
	private ErpConstants() {
	}
	
	// Module resources
	
	public static final String MODULE_NAME = "Erp";
	
	public static final String MODULE_ARTIFACT_ID = "erp";
	
	public static final String MODULE_PRIVILEGE = "Get ERP objects";
	
	public static final String ERP_PROPERTY_FILE_NAME = "erp.properties";
	
	public static final String COMPONENT_ERP_CONTEXT = MODULE_ARTIFACT_ID + ".ErpContext";
	
	public static final String COMPONENT_ODOO_ORDER_SERVICE = MODULE_ARTIFACT_ID + ".OdooOrderService";
	
	public static final String COMPONENT_ODOO_INVOICE_SERVICE = MODULE_ARTIFACT_ID + ".OdooInvoiceService";
	
	public static final String COMPONENT_ERP_CONNECTION = ".ErpConnection";
	
	// REST resources
	
	public static final String ERP_ORDER_URI = "/order";
	
	public static final String ERP_INVOICE_URI = "/invoice";
	
}
