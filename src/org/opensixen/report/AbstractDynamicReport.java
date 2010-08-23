/**
 * 
 */
package org.opensixen.report;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.opensixen.model.ColumnDefinition;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;


import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
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
 * @author harlock
 * @param <T>
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
		headerVariables.setFont(Font.ARIAL_BIG_BOLD);
		headerVariables.setBorderBottom(Border.THIN);
		headerVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
		headerVariables.setVerticalAlign(VerticalAlign.TOP);

		groupVariables = new Style("groupVariables");
		groupVariables.setFont(Font.ARIAL_MEDIUM_BOLD);
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
						DJGroupVariable variable = new DJGroupVariable(gcol, group.getCalculation(), group.getStyle() );
	
						builder.addHeaderVariable(variable);
					}
				}
				// Añadimos footer groups				
				if (definition.getFooterVariables() != null)	{
					for (GroupVariable group: definition.getFooterVariables())	{
						AbstractColumn gcol = m_columns.get(group.getName());
						DJGroupVariable variable = new DJGroupVariable(gcol, group.getCalculation(), group.getStyle() );
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
}
