package org.opensixen.osgi.interfaces;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Locale;

import org.opensixen.osgi.interfaces.IService;

public interface IJasperFormater extends IService {

	/**
	 * Format qty
	 * @param locale
	 * @param qty
	 * @return
	 */
	public String formatQty(Locale locale, BigDecimal qty);
	
	/**
	 * Format amount with currency
	 * @param locale
	 * @param amount
	 * @param isoCode
	 * @return
	 */
	public String formatAmt(Locale locale, BigDecimal amount, String isoCode);		
	
	/**
	 * Format date
	 * @param locale
	 * @param date
	 * @return
	 */
	public String formatDate(Locale locale, Timestamp date);
	
	/**
	 * Format a location
	 * @param C_BPartner_Location_ID
	 * @return
	 */
	public String formatLoc(Integer C_BPartner_Location_ID);
	
	/**
	 * Format a full Address
	 * @param C_BPartner_ID
	 * @param C_Bpartner_Location_ID
	 * @param addTaxID
	 * @return
	 */
	public String formatLocation(int C_BPartner_ID, int C_Bpartner_Location_ID, boolean addTaxID);
	
	/**
	 * Get Ship To BPartner in InOut
	 * @param M_InOut_ID
	 * @return
	 */
	public String getShipToInOut(Integer M_InOut_ID);
	
	/**
	 * Get BPartner in Invoice
	 * @param C_Invoice_ID
	 * @return
	 */
	public String getInvoiceToInvoice(Integer C_Invoice_ID);
	

	/**
	 * Get BPartner in Order
	 * @param C_Invoice_ID
	 * @return
	 */
	public String getInvoiceToOrder(Integer C_Order_ID);
	
	/**
	 * Get Ship To BPartner in Order
	 * @param M_InOut_ID
	 * @return
	 */
	public String getShipToOrder(Integer C_Order_ID);
	
	/**
	 * Get Payment terms from this invoice
	 * @param locale
	 * @param C_Invoice_ID
	 * @param isoCode
	 * @return
	 */
	public String getPaymentTermInvoice(Locale locale, Integer C_Invoice_ID, String isoCode);
	
	/**
	 * Get Payment terms from this order
	 * @param locale
	 * @param C_Invoice_ID
	 * @param isoCode
	 * @return
	 */
	public String getPaymentTermOrder(Locale locale, Integer C_Order_ID, String isoCode);
	
	
	
}
