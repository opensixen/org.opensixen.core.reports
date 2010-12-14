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

import java.util.List;
import java.util.Properties;

import net.sf.jasperreports.engine.JRDataSource;

import org.adempiere.util.ModelInterfaceGenerator;
import org.compiere.model.MColumn;
import org.compiere.model.MRefList;
import org.compiere.model.MRefTable;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.util.DisplayType;
import org.opensixen.model.ColumnDefinition;
import org.opensixen.model.POFactory;
import org.opensixen.model.QParam;

import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;

/**
 * @author harlock
 * 
 */
public abstract class AbstractPODynamicReport extends
		AbstractDynamicReport {

	/**
	 * @param ctx
	 */
	protected AbstractPODynamicReport(Properties ctx) {
		super(ctx);
	}

	private MTable table;

	protected abstract <T extends PO> Class<T> getReportClass();

	protected JRDataSource getDataSource() {
		List<PO> records = POFactory.getList(getCtx(), getReportClass(),
				getQParams(), getOrderColumns(), null);
		JRDataSource ds = new JRPODataSource(records);
		return ds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opensixen.report.AbstractDynamicJasperReport#initReport()
	 */
	@Override
	protected void initReport() {
		// Creamos un objeto vacio para obtener la tabla
		PO po = POFactory.get(getReportClass(), 0);

		table = MTable.get(getCtx(), po.get_TableName());
	}

	@Override
	protected AbstractColumn createColumn(ColumnDefinition definition) {
		MColumn m_column = table.getColumn(definition.getName());

		// Obtenemos la clase de la columna gracias a ModelInterfaceGenerator
		Class colClazz = ModelInterfaceGenerator.getClass(m_column.getColumnName(), m_column.getAD_Reference_ID(),m_column.getAD_Reference_Value_ID());
		definition.setClazz(colClazz);
		definition.setTitle(m_column.get_Translation(m_column.COLUMNNAME_Name));

		// Si es de tipo amount, añadimos formato.
		if (m_column.getAD_Reference_ID() == DisplayType.Amount) {
			definition.setPattern("#,##0.00 €");
		}
		
		if (m_column.getAD_Reference_ID() == DisplayType.Date) {
			definition.setPattern("dd/MM/yy");
		}
		
		

		return super.createColumn(definition);

	}
	
	protected QParam[] getQParams()	{
		return null;
	}
	

}
