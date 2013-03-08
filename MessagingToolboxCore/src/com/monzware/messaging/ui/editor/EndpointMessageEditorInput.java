package com.monzware.messaging.ui.editor;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverMessage;

public class EndpointMessageEditorInput extends PlatformObject implements IEditorInput, IPersistableElement {

	private final EndpointReceiverMessage message;
	private final Endpoint endpoint;

	public EndpointMessageEditorInput(Endpoint endpoint, EndpointReceiverMessage message) {
		this.endpoint = endpoint;
		this.message = message;
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;// super.getAdapter(adapter);
	}	

	public boolean exists() {
		return message != null;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return endpoint.getEndpointsystem().getSystemName() + "->" + endpoint.getName() + "->" + (message == null ? "?" : message.getMessageId());
	}	

	public IPersistableElement getPersistable() {
		return message != null ? this : null;
	}

	public String getToolTipText() {
		return getName();
	}

	public void saveState(IMemento memento) {
		EndpointMessageEditorInputFactory.saveState(memento, this);
	}

	public String getFactoryId() {
		return EndpointMessageEditorInputFactory.getFactoryId();
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public EndpointReceiverMessage getMessage() {
		return message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endpoint == null) ? 0 : endpoint.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EndpointMessageEditorInput other = (EndpointMessageEditorInput) obj;
		if (endpoint == null) {
			if (other.endpoint != null)
				return false;
		} else if (!endpoint.equals(other.endpoint))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}
}
