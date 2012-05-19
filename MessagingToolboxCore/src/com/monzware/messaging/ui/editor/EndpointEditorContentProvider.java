package com.monzware.messaging.ui.editor;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiver;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverException;

public class EndpointEditorContentProvider implements IStructuredContentProvider {

	private final EndpointReceiver endpointReceiver;

	public EndpointEditorContentProvider(EndpointReceiver endpointReceiver) {
		this.endpointReceiver = endpointReceiver;
	}

	public void dispose() {

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public Object[] getElements(Object inputElement) {

		if (endpointReceiver == null) {
			return new Object[0];
		}

		try {
			return endpointReceiver.getMessages().toArray();
		} catch (EndpointReceiverException e) {
			return new Object[0];
		}
	}
}
