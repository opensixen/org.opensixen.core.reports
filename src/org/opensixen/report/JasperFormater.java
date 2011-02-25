 /******* BEGIN LICENSE BLOCK *****
 * Versión: GPL 2.0/CDDL 1.0/EPL 1.0
 *
 * Los contenidos de este fichero están sujetos a la Licencia
 * Pública General de GNU versión 2.0 (la "Licencia"); no podrá
 * usar este fichero, excepto bajo las condiciones que otorga dicha 
 * Licencia y siempre de acuerdo con el contenido de la presente. 
 * Una copia completa de las condiciones de de dicha licencia,
 * traducida en castellano, deberá estar incluida con el presente
 * programa.
 * 
 * Adicionalmente, puede obtener una copia de la licencia en
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Este fichero es parte del programa opensiXen.
 *
 * OpensiXen es software libre: se puede usar, redistribuir, o
 * modificar; pero siempre bajo los términos de la Licencia 
 * Pública General de GNU, tal y como es publicada por la Free 
 * Software Foundation en su versión 2.0, o a su elección, en 
 * cualquier versión posterior.
 *
 * Este programa se distribuye con la esperanza de que sea útil,
 * pero SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita 
 * MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO. Consulte 
 * los detalles de la Licencia Pública General GNU para obtener una
 * información más detallada. 
 *
 * TODO EL CÓDIGO PUBLICADO JUNTO CON ESTE FICHERO FORMA PARTE DEL 
 * PROYECTO OPENSIXEN, PUDIENDO O NO ESTAR GOBERNADO POR ESTE MISMO
 * TIPO DE LICENCIA O UNA VARIANTE DE LA MISMA.
 *
 * El desarrollador/es inicial/es del código es
 *  FUNDESLE (Fundación para el desarrollo del Software Libre Empresarial).
 *  Indeos Consultoria S.L. - http://www.indeos.es
 *
 * Contribuyente(s):
 *  Eloy Gómez García <eloy@opensixen.org> 
 *
 * Alternativamente, y a elección del usuario, los contenidos de este
 * fichero podrán ser usados bajo los términos de la Licencia Común del
 * Desarrollo y la Distribución (CDDL) versión 1.0 o posterior; o bajo
 * los términos de la Licencia Pública Eclipse (EPL) versión 1.0. Una 
 * copia completa de las condiciones de dichas licencias, traducida en 
 * castellano, deberán de estar incluidas con el presente programa.
 * Adicionalmente, es posible obtener una copia original de dichas 
 * licencias en su versión original en
 *  http://www.opensource.org/licenses/cddl1.php  y en  
 *  http://www.opensource.org/licenses/eclipse-1.0.php
 *
 * Si el usuario desea el uso de SU versión modificada de este fichero 
 * sólo bajo los términos de una o más de las licencias, y no bajo los 
 * de las otra/s, puede indicar su decisión borrando las menciones a la/s
 * licencia/s sobrantes o no utilizadas por SU versión modificada.
 *
 * Si la presente licencia triple se mantiene íntegra, cualquier usuario 
 * puede utilizar este fichero bajo cualquiera de las tres licencias que 
 * lo gobiernan,  GPL 2.0/CDDL 1.0/EPL 1.0.
 *
 * ***** END LICENSE BLOCK ***** */
package org.opensixen.report;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.compiere.model.I_C_BPartner;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MInOut;
import org.compiere.model.MInvoice;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MPaySchedule;
import org.compiere.util.Env;
import org.opensixen.model.MVOpenItem;


/**
 * JasperFormater 
 *
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 */
public class JasperFormater {

	/** Online mode	*/
	private static boolean online = false;
	
	/**
	 * Get online mode
	 * @return
	 */
	public static boolean isOnline() {
		return online;
	}
	
	/**
	 * Set online mode
	 * @param online
	 */
	public static void setOnline(boolean online) {
		JasperFormater.online = online;
	}
	
	/**
	 * Format qty
	 * @param locale
	 * @param qty
	 * @return
	 */
	public static String formatQty(Locale locale, BigDecimal qty)		{
		NumberFormat formater = NumberFormat.getInstance(locale);
		String str = formater.format(qty);
		return str;
	}
		
	/**
	 * Format amount with currency
	 * @param locale
	 * @param amount
	 * @param isoCode
	 * @return
	 */
	public static String formatAmt(Locale locale, BigDecimal amount, String isoCode)	{		
		String str = formatQty(locale, amount);
		Currency currency = Currency.getInstance(isoCode);		
		return str + currency.getSymbol();
	}
	
	/**
	 * Format a location
	 * @param C_BPartner_Location_ID
	 * @return
	 */
	public static String formatLoc(Integer C_BPartner_Location_ID)	{
		if (isOnline())	{
			MBPartnerLocation bplocation = new MBPartnerLocation(Env.getCtx(), C_BPartner_Location_ID, null);
			MLocation location = bplocation.getLocation(false);
			StringBuffer address = new StringBuffer();
			address.append(location.toStringCR());
			String country = location.getCountry(true);
			if (country != null)	{
				address.append("\n").append(country);
			}
			return address.toString();
		}
		else {
			return "C\\ General Prim, 4 1ºA\nJunto a iglesia\n03158 - Catral (Alicante)\nSpain";
		}
	}
	

	/**
	 * Format date
	 * @param locale
	 * @param date
	 * @return
	 */
	public static String formatDate(Locale locale, Timestamp date)		{
		DateFormat df = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale);
		String str = df.format(new Date(date.getTime()));
		return str;
	}
	
	
	/**
	 * Get Ship To BPartner in InOut
	 * @param M_InOut_ID
	 * @return
	 */
	public static String getShipToInOut(Integer M_InOut_ID)	{
		StringBuffer address = new StringBuffer();
		if (isOnline())	{
			MInOut inout = new MInOut(Env.getCtx(), M_InOut_ID, null);
			if (inout.isDropShip())	{
				I_C_BPartner bp = inout.getDropShip_BPartner();
				address.append("<b>").append(bp.getName()).append("</b>\n");
				address.append(formatLoc(inout.getDropShip_Location_ID()));
			}
			else {
				MBPartner bp = inout.getBPartner();
				address.append("<b>").append(bp.getName()).append("</b>\n");
				address.append(formatLoc(inout.getC_BPartner_Location_ID()));
			}
		}
		else {
			address.append("<b>Partner de Envio</b>\n");
			address.append(formatLoc(0));
		}
		return address.toString();
	}
	
	
	/**
	 * Get BPartner in Invoice
	 * @param C_Invoice_ID
	 * @return
	 */
	public static String getInvoiceToInvoice(Integer C_Invoice_ID)	{
		StringBuffer address = new StringBuffer();
		if (isOnline())	{
			MInvoice invoice = new MInvoice(Env.getCtx(), C_Invoice_ID, null);
			
			I_C_BPartner bp = invoice.getC_BPartner();
			address.append("<b>").append(bp.getName()).append("</b>\n");
			if (bp.getTaxID() != null)	{
				address.append(bp.getTaxID()).append("\n");
			}
			address.append(formatLoc(invoice.getC_BPartner_Location_ID()));
			
		}
		else {
			address.append("<b>Partner de Envio.</b>").append("\n");
			address.append("B00000001").append("\n");
			address.append(formatLoc(0));
		}
		
		return address.toString();
	}

	/**
	 * Get Payment terms from this invoice
	 * @param locale
	 * @param C_Invoice_ID
	 * @param isoCode
	 * @return
	 */
	public static String getPaymentTermInvoice(Locale locale, Integer C_Invoice_ID, String isoCode)	{
		StringBuffer term = new StringBuffer();
		
		if (isOnline())	{
			List<MVOpenItem> items = MVOpenItem.getFromInvoice(Env.getCtx(), C_Invoice_ID);
			for (MVOpenItem item:items)		{
				term.append(formatDate(locale, item.getDueDate())).append(": ").append(formatAmt(locale, item.getOpenAmt(), isoCode)).append("\n");
			}
		}
		else {
			
			term.append("01/01/1979 100,00€");		
		}
		return term.toString();		
	}
	
	/**
	 * Get BPartner in Order
	 * @param C_Invoice_ID
	 * @return
	 */
	public static String getInvoiceToOrder(Integer C_Order_ID)	{
		StringBuffer address = new StringBuffer();
		if (isOnline())	{
			MOrder order = new MOrder(Env.getCtx(), C_Order_ID, null);
			
			I_C_BPartner bp = order.getC_BPartner();
			address.append("<b>").append(bp.getName()).append("</b>\n");
			if (bp.getTaxID() != null)	{
				address.append(bp.getTaxID()).append("\n");
			}
			address.append(formatLoc(order.getC_BPartner_Location_ID()));
			
		}
		else {
			address.append("<b>Partner de Envio .</b>").append("\n");
			address.append("B00000001").append("\n");
			address.append(formatLoc(0));
		}
		
		return address.toString();
	}

	/**
	 * Get Ship To BPartner in Order
	 * @param M_InOut_ID
	 * @return
	 */
	public static String getShipToOrder(Integer C_Order_ID)	{
		StringBuffer address = new StringBuffer();
		if (isOnline())	{
			MOrder order = new MOrder(Env.getCtx(), C_Order_ID, null);
			if (order.isDropShip())	{
				I_C_BPartner bp = order.getDropShip_BPartner();
				address.append("<b>").append(bp.getName()).append("</b>\n");
				address.append(formatLoc(order.getDropShip_Location_ID()));
			}
			else {
				I_C_BPartner bp = order.getC_BPartner();
				address.append("<b>").append(bp.getName()).append("</b>\n");
				address.append(formatLoc(order.getC_BPartner_Location_ID()));
			}
		}
		else {
			address.append("<b>Partner de Envio</b>\n");
			address.append(formatLoc(0));
		}
		return address.toString();
	}
	
	/**
	 * Get Payment terms from this order
	 * @param locale
	 * @param C_Invoice_ID
	 * @param isoCode
	 * @return
	 */
	public static String getPaymentTermOrder(Locale locale, Integer C_Order_ID, String isoCode)	{
		StringBuffer term = new StringBuffer();
		
		if (isOnline())	{
			List<MVOpenItem> items = MVOpenItem.getFromOrder(Env.getCtx(), C_Order_ID);
			for (MVOpenItem item:items)		{
				term.append(formatDate(locale, item.getDueDate())).append(": ").append(formatAmt(locale, item.getOpenAmt(), isoCode)).append("\n");
			}
		}
		else {
			
			term.append("01/01/1979 100,00€");		
		}
		return term.toString();		
	}
}
