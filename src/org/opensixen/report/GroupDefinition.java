/**
 * 
 */
package org.opensixen.report;

import ar.com.fdvs.dj.domain.entities.DJGroupVariable;

public class GroupDefinition {
	private String[] groupColumns;
	private GroupVariable[] headerVariables;
	private GroupVariable[] footerVariables;
	
	/**
	 * @return the groupColumns
	 */
	public String[] getGroupColumns() {
		return groupColumns;
	}

	/**
	 * @param groupColumns the groupColumns to set
	 */
	public void setGroupColumns(String[] groupColumns) {
		this.groupColumns = groupColumns;
	}

	/**
	 * @return the headerVariables
	 */
	public GroupVariable[] getHeaderVariables() {
		return headerVariables;
	}

	/**
	 * @param headerVariables the headerVariables to set
	 */
	public void setHeaderVariables(GroupVariable[] headerVariables) {
		this.headerVariables = headerVariables;
	}

	/**
	 * @return the footerVariables
	 */
	public GroupVariable[] getFooterVariables() {
		return footerVariables;
	}

	/**
	 * @param footerVariables the footerVariables to set
	 */
	public void setFooterVariables(GroupVariable[] footerVariables) {
		this.footerVariables = footerVariables;
	}
	 
	
	
	
}