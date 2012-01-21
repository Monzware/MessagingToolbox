package com.monzware.messaging.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class EndpointViewLabelProvider extends LabelProvider {

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
}