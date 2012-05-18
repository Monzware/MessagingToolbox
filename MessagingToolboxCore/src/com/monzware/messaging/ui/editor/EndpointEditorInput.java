package com.monzware.messaging.ui.editor;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;

public class EndpointEditorInput extends PlatformObject implements IEditorInput, IPersistableElement {

	private final Endpoint endpoint;

	public EndpointEditorInput(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	public Object getAdapter(Class adapter) {
		return null;// super.getAdapter(adapter);
	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return endpoint.getEndpointsystem().getSystemName() + " -> " + endpoint.getName();
	}

	public IPersistableElement getPersistable() {
		return this;
	}

	public String getToolTipText() {
		return getName();
	}

	public void saveState(IMemento memento) {
		EndpointEditorInputFactory.saveState(memento, this);
	}

	public String getFactoryId() {
		return EndpointEditorInputFactory.getFactoryId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getEndpoint() == null) ? 0 : getEndpoint().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EndpointEditorInput other = (EndpointEditorInput) obj;
		if (getEndpoint() == null) {
			if (other.getEndpoint() != null)
				return false;
		} else if (!getEndpoint().equals(other.getEndpoint()))
			return false;
		return true;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}
}
