package com.monzware.messaging.ui.editor;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiver;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverException;

public class EndpointEditorContentProvider implements IStructuredContentProvider {

	private final EndpointReceiver endpointReceiver;
	private final EndpointEditor endpointEditor;
	private final Endpoint endpoint;

	public EndpointEditorContentProvider(EndpointEditor endpointEditor, Endpoint endpoint) {
		this.endpointEditor = endpointEditor;
		this.endpoint = endpoint;
		this.endpointReceiver = endpoint.getEndpointReceiver();
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
			Object[] result = endpointReceiver.getMessages().toArray();
			endpointEditor.setEnabled(true);
			return result;
		} catch (EndpointReceiverException e) {
			endpointEditor.setEnabled(false);
			return new Object[0];
		}
	}
	
	public Endpoint getEndpoint() {
		return endpoint;
	}
}
