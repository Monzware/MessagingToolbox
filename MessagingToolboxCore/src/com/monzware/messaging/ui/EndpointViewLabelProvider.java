package com.monzware.messaging.ui;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class EndpointViewLabelProvider extends CellLabelProvider {

	private final Display display;

	public EndpointViewLabelProvider(Display display) {
		this.display = display;
	}

	public String getText(Object obj) {
		return obj.toString();
	}

	public Image getImage(Object obj) {

		if (obj instanceof EndpointSystem) {
			return new Image(display, getClass().getResourceAsStream("/icons/databases.png"));
		} else if (obj instanceof Endpoint) {
			return new Image(display, getClass().getResourceAsStream("/icons/database2.png"));
		}

		return null;
	}

	public String getToolTipText(Object obj) {
		if (obj instanceof EndpointSystem) {
			return ((EndpointSystem) obj).getTooltipText();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipShift(java.lang
	 * .Object)
	 */
	public Point getToolTipShift(Object object) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipDisplayDelayTime
	 * (java.lang.Object)
	 */
	public int getToolTipDisplayDelayTime(Object object) {
		return 500;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipTimeDisplayed
	 * (java.lang.Object)
	 */
	public int getToolTipTimeDisplayed(Object object) {
		return 10000;
	}

	public void update(ViewerCell cell) {
		cell.setText(cell.getElement().toString());

	}

}