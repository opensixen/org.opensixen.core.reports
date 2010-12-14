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

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.opensixen.model.ColumnDefinition;
import org.opensixen.model.GroupDefinition;
import org.opensixen.model.GroupVariable;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;


import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.DJGroupVariable;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;

/**
 * 
 * 
 * 
 * @author Eloy Gomez
 * Indeos Consultoria http://www.indeos.es
 *
 */
public abstract class AbstractDynamicReport  {

	public static Style STYLE_LEFT;
	
	private JasperPrint jasperPrint;


	private FastReportBuilder report; 
	
	private HashMap<String, AbstractColumn> m_columns = new HashMap<String, AbstractColumn>();

	private Properties ctx;


	public static Style detailStyle;


	public static Style headerStyle;


	public static Style headerVariables;


	public static Style groupVariables;


	public static Style titleStyle;


	public static Style oddRowStyle;


	public static Style detailGrStyle;


	public static Style detailLeftStyle;
	
	
	static	{
		detailStyle = new Style("detail");
		detailStyle.setFont(Font.ARIAL_SMALL);
		
		detailLeftStyle = new Style("detail.left");
		detailLeftStyle.setFont(Font.ARIAL_SMALL);
		detailLeftStyle.setHorizontalAlign(HorizontalAlign.LEFT);

		detailGrStyle = new Style("detailGR");
		detailGrStyle.setFont(Font.ARIAL_SMALL_BOLD);
		detailGrStyle.setTextColor(Color.BLACK);
		detailGrStyle.setHorizontalAlign(HorizontalAlign.LEFT);
		detailGrStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		
		
		headerStyle = new Style("header");
		headerStyle.setFont(Font.ARIAL_SMALL_BOLD);
		headerStyle.setBorderBottom(Border.PEN_1_POINT);
		headerStyle.setBackgroundColor(Color.gray);
		headerStyle.setTextColor(Color.white);
		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
		headerStyle.setTransparency(Transparency.OPAQUE);

		headerVariables = new Style("headerVariables");
		headerVariables.setFont(Font.ARIAL_SMALL_BOLD);
		headerVariables.setBorderBottom(Border.THIN);
		headerVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
		headerVariables.setVerticalAlign(VerticalAlign.TOP);

		groupVariables = new Style("groupVariables");
		groupVariables.setFont(Font.ARIAL_SMALL_BOLD);
		groupVariables.setTextColor(Color.BLUE);
		groupVariables.setBorderBottom(Border.THIN);
		groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
		groupVariables.setVerticalAlign(VerticalAlign.BOTTOM);

		titleStyle = new Style("titleStyle");
		titleStyle.setFont(new Font(18, Font._FONT_VERDANA, true));
		
		Style importeStyle = new Style();
		importeStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
		
		oddRowStyle = new Style();
		oddRowStyle.setBorder(Border.NO_BORDER);
		oddRowStyle.setBackgroundColor(Color.LIGHT_GRAY);
		oddRowStyle.setTransparency(Transparency.OPAQUE);

		
	}
	
	protected AbstractDynamicReport(Properties ctx)	{
		this.ctx = ctx;
	}
	
	
	public Properties getCtx()	{
		return ctx;
	}
	
	protected abstract ColumnDefinition[] getColumns();
	public abstract String getTitle();
	protected abstract JRDataSource getDataSource();
	
	protected void initReport()	{
		
	}
	
	protected String[] getOrderColumns()	{
		return null;
	}
	
	
	public String getSubTitle()	{
		return null;
	}

	protected List<GroupDefinition> getGroupDefinitions()	{
		return null;
	}
	

	
	public JasperPrint createReport()	{
		
		report = new FastReportBuilder();
		Integer margin = new Integer(20);
		report.setDetailHeight(new Integer(15)).setLeftMargin(margin)
		.setRightMargin(margin).setTopMargin(margin).setBottomMargin(margin)
		.setPrintBackgroundOnOddRows(true)
		.setGrandTotalLegend("Total")
		.setGrandTotalLegendStyle(headerVariables)
		.setOddRowBackgroundStyle(oddRowStyle);
		
		for (ColumnDefinition coldef:getColumns())	{
			AbstractColumn col = createColumn(coldef);
			if (col != null)	{
				report.addColumn(col);
				m_columns.put(coldef.getName(), col);
			}
		}

		// Añadimos grupos
		for (DJGroup group:getGroups())	{
			report.addGroup(group);
		}
		
		report.setTitle(getTitle());
		report.setSubtitle(getSubTitle());
		report.setPrintBackgroundOnOddRows(true);
		report.setUseFullPageWidth(true);
		
		DynamicReport dr = report.build();
		
		JRDataSource ds = getDataSource();
		try {
			jasperPrint = DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds);
			return jasperPrint;
		}
		catch (Exception e)	{
			e.printStackTrace();
			return null;
		}
			
	}
	
	
	
	protected AbstractColumn createColumn(ColumnDefinition definition)	{
		ColumnBuilder col = ColumnBuilder.getNew();
		col.setColumnProperty(definition.getName(), definition.getClazz().getName());
		// Obtenemos la traduccion para establecerla como nombre de columna
		col.setTitle(definition.getTitle());
		if (definition.getPattern() != null)	{
			col.setPattern(definition.getPattern());
		}
		col.setWidth(definition.getSize());

		//col.setStyle(definition.getStyle());
		
		col.setHeaderStyle(headerStyle);
		try {
			return col.build();
		} catch (ColumnBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}


	}
	
	private List<DJGroup> getGroups()	{
		ArrayList<DJGroup> djGroups = new ArrayList<DJGroup>();
		if (getGroupDefinitions() == null)	{
			return djGroups;
		}
		
		
		for (GroupDefinition definition:getGroupDefinitions() )	{
			GroupBuilder builder = new GroupBuilder();

			for (String colName: definition.getGroupColumns())	{
				AbstractColumn col = m_columns.get(colName);
				builder.setCriteriaColumn((PropertyColumn) col);
				// Añadimos header groups
				if (definition.getHeaderVariables() != null)	{
					for (GroupVariable group: definition.getHeaderVariables())	{
						AbstractColumn gcol = m_columns.get(group.getName());
						DJGroupVariable variable = new DJGroupVariable(gcol, getDJCalculation(group.getCalculation()), getStyle(group.getStyle()) );
	
						builder.addHeaderVariable(variable);
					}
				}
				// Añadimos footer groups				
				if (definition.getFooterVariables() != null)	{
					for (GroupVariable group: definition.getFooterVariables())	{
						AbstractColumn gcol = m_columns.get(group.getName());
						DJGroupVariable variable = new DJGroupVariable(gcol,  getDJCalculation(group.getCalculation()), getStyle(group.getStyle()) );
						builder.addFooterVariable(variable);
					}
				}
			}
			
			djGroups.add(builder.build());

		}
		return djGroups;
	}
	
	
	public void viewReport()	{
		JasperPrint print = createReport();
		if (print == null)	{
			return;			
		}
		
		JasperViewer.viewReport(print, false);
	}
	
	public DJCalculation getDJCalculation(int calculation)	{
		switch (calculation) {
		case GroupVariable.SUM:
			return DJCalculation.SUM;

		default:
			return null;
		}
	}
	
	public Style getStyle(String style)	{
		if (style == GroupVariable.STYLE_DEFAULT)	{
			return groupVariables;
		}
		return null;
	}
	
}
