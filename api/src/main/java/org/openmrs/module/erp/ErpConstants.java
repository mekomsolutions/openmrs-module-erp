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
	
	public static final String COMPONENT_ODOO_PARTNER_SERVICE = MODULE_ARTIFACT_ID + ".OdooPartnerService";
	
	public static final String COMPONENT_ODOO_SESSION = MODULE_ARTIFACT_ID + ".OdooSession";
	
	public static final String COMPONENT_ERP_ACTIVATOR = MODULE_ARTIFACT_ID + ".ErpActivator";
	
	// Odoo connection resources
	
	public static final String HOST_PROPERTY = "erp.host";
	
	public static final String PORT_PROPERTY = "erp.port";
	
	public static final String DATABASE_PROPERTY = "erp.database";
	
	public static final String USER_PROPERTY = "erp.user";
	
	public static final String PASSWORD_PROPERTY = "erp.password";
	
	// REST resources
	
	public static final String ERP_URI = "/erp";
	
	public static final String ERP_ORDER_URI = "/order";
	
	public static final String ERP_INVOICE_URI = "/invoice";
	
	public static final String ERP_PARTNER_URI = "/partner";
}
