/**
 * 
 */
package org.opensixen.report;

import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.Style;

/**
 * @author harlock
 *
 */
public class GroupVariable {

	private String name;
	
	private DJCalculation calculation;
	
	private Style style = AbstractDynamicReport.groupVariables;

	
	
	
	public GroupVariable(String name, DJCalculation calculation) {
		super();
		this.name = name;
		this.calculation = calculation;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the calculation
	 */
	public DJCalculation getCalculation() {
		return calculation;
	}

	/**
	 * @param calculation the calculation to set
	 */
	public void setCalculation(DJCalculation calculation) {
		this.calculation = calculation;
	}

	/**
	 * @return the style
	 */
	public Style getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(Style style) {
		this.style = style;
	}
		
}
