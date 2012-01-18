package com.monzware.messaging.ui;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.monzware.messaging.toolbox.EndpointManager;
import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.toolbox.core.configmodel.impl.EndpointImpl;
import com.monzware.messaging.toolbox.core.configmodel.impl.EndpointSystemImpl;

public class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	private final EndpointTreeView endpointTreeView;

	public ViewContentProvider(EndpointTreeView endpointTreeView) {
		this.endpointTreeView = endpointTreeView;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(endpointTreeView.getViewSite())) {

			EndpointManager endpointManager = MessagingToolboxPlugin.getDefault().getEndpointManager();
			Collection<EndpointSystemImpl> endpointSystems = endpointManager.getEndpointSystems();
			return endpointSystems.toArray();

		}
		return getChildren(parent);
	}

	public Object getParent(Object child) {

		if (child instanceof EndpointImpl) {
			EndpointImpl ep = (EndpointImpl) child;
			return ep.getEndpointsystem();
		}

		return null;
	}

	public Object[] getChildren(Object parent) {

		if (parent instanceof EndpointSystemImpl) {
			EndpointSystemImpl eps = (EndpointSystemImpl) parent;
			return eps.getEndpoints().toArray();
		}

		return new Object[0];
	}

	public boolean hasChildren(Object parent) {

		if (parent instanceof EndpointSystemImpl) {
			EndpointSystemImpl eps = (EndpointSystemImpl) parent;
			return !eps.getEndpoints().isEmpty();
		}
		return false;
	}

}
