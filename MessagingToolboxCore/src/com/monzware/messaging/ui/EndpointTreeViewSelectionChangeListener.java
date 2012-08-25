package com.monzware.messaging.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class EndpointTreeViewSelectionChangeListener implements ISelectionChangedListener {

	private final Action action;

	public EndpointTreeViewSelectionChangeListener(Action action) {
		this.action = action;
	}

	public void selectionChanged(SelectionChangedEvent event) {

		TreeSelection ts = (TreeSelection) event.getSelection();
		Object elm = ts.getFirstElement();

		action.setEnabled(elm instanceof EndpointSystem);
	}
}
