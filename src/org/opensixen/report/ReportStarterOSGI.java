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

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.compiere.report.ReportStarter;
import org.compiere.util.CLogger;
import org.opensixen.osgi.BundleProxyClassLoader;


/**
 * ReportStarterOSGI
 * 
 *  Report Starter for OSGi bundles
 *
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 */
public class ReportStarterOSGI extends ReportStarter {
	private CLogger log = CLogger.getCLogger(getClass()); 
	
	
	/* (non-Javadoc)
	 * @see org.compiere.report.ReportStarter#setupCLasspath(org.compiere.report.ReportStarter.JasperData)
	 */
	@Override
	protected void setupCLasspath(JasperData data) {
		URL reportDir = BundleProxyClassLoader.findResourceInAll(data.getReportDir().getPath()); 
		java.net.URLClassLoader ucl = new java.net.URLClassLoader(new java.net.URL[]{reportDir}, getClass().getClassLoader());		
		net.sf.jasperreports.engine.util.JRResourcesUtil.setThreadClassLoader(ucl);
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.compiere.report.ReportStarter#processReport(java.io.File)
	 */
	@Override
	protected JasperData processReport(File reportFile) {
		String jasperName = reportFile.getName();
		String resourceName = reportFile.getPath();
        int pos = jasperName.indexOf('.');
        if (pos!=-1) jasperName = jasperName.substring(0, pos);
		
		File reportDir = new File(reportFile.getParent());
		
		JasperReport jasperReport;
		try {
			InputStream stream = BundleProxyClassLoader.findResourceInAll(resourceName).openStream();
			jasperReport = (JasperReport)JRLoader.loadObject(stream);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Can't get report source.", e);
			return null;
		}
        
        JasperData data = new JasperData(jasperReport, reportDir, jasperName, null);
        return data;
	}
	/* (non-Javadoc)
	 * @see org.compiere.report.ReportStarter#JWScorrectClassPath()
	 */
	@Override
	protected void JWScorrectClassPath() {
		log.info("Nada que hacer.");
	}

	
	
	
	
}

