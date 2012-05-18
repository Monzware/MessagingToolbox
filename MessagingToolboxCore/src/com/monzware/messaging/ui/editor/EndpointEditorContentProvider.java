package com.monzware.messaging.ui.editor;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiver;

public class EndpointEditorContentProvider implements IStructuredContentProvider {

	public EndpointEditorContentProvider(EndpointReceiver endpointReceiver) {

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	public Object[] getElements(Object inputElement) {
		return new Object[0];
	}

}
