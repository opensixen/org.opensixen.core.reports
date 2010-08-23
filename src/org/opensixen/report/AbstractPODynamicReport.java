/**
 * 
 */
package org.opensixen.report;

import java.util.List;
import java.util.Properties;

import net.sf.jasperreports.engine.JRDataSource;

import org.adempiere.util.ModelInterfaceGenerator;
import org.compiere.model.MColumn;
import org.compiere.model.MTable;
import org.compiere.model.PO;
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
		if (m_column.getAD_Reference_ID() == 12) {
			definition.setPattern("#,##0.00 €");
		}

		return super.createColumn(definition);

	}
	
	protected QParam[] getQParams()	{
		return null;
	}
	

}
