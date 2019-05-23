package org.openmrs.module.erp;

/**
 * Contains module's constants.
 */
public class ErpConstants {
	
	// Module resources
	
	public final static String MODULE_NAME = "Erp";
	
	public final static String MODULE_ARTIFACT_ID = "erp";
	
	public static final String MODULE_PRIVILEGE = "ERP PRIVILEGE";
	
	public static final String COMPONENT_ERP_CONTEXT = MODULE_ARTIFACT_ID + ".ErpContext";
	
	public static final String COMPONENT_ODOO_ORDER_SERVICE = MODULE_ARTIFACT_ID + ".OdooOrderService";
	
	public static final String COMPONENT_ODOO_INVOICE_SERVICE = MODULE_ARTIFACT_ID + ".OdooInvoiceService";
	
	// REST resources
	
	public final static String ERP_ORDER_URI = "/order";
	
	public final static String ERP_INVOICE_URI = "/invoice";
	
}
