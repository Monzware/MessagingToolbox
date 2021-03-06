package com.monzware.messaging.toolbox.menucontribution;

import java.util.Collection;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.popup.actions.SendToEndpointAction;

public class EndpointsystemMenulistener implements IMenuListener {

	private final MenuManager endPointMenuManager;
	private final EndpointSystem endpointSystem;

	public EndpointsystemMenulistener(MenuManager endPointMenuManager, EndpointSystem endpointSystem) {
		this.endPointMenuManager = endPointMenuManager;
		this.endpointSystem = endpointSystem;
	}

	public void menuAboutToShow(IMenuManager manager) {

		Collection<? extends Endpoint> endpoints = endpointSystem.getEndpoints();
		for (Endpoint endpoint : endpoints) {
			endPointMenuManager.add(new SendToEndpointAction(endpoint));
		}
	}
}
