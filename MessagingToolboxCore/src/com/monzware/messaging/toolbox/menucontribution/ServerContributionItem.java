package com.monzware.messaging.toolbox.menucontribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.CompoundContributionItem;

import com.monzware.messaging.toolbox.EndpointManager;
import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.toolbox.core.configmodel.impl.EndpointSystemImpl;

public final class ServerContributionItem extends CompoundContributionItem {
	public ServerContributionItem() {
		super();
	}

	protected IContributionItem[] getContributionItems() {

		List<MenuManager> toReturn = new ArrayList<MenuManager>();

		EndpointManager endpointManager = MessagingToolboxPlugin.getDefault().getEndpointManager();

		Collection<EndpointSystemImpl> endpointSystems = endpointManager.getEndpointSystems();
		for (EndpointSystemImpl endpointSystem : endpointSystems) {
			toReturn.add(getEndPointMenuManagerFromEndpointsystem(endpointSystem));
		}

		return (IContributionItem[]) toReturn.toArray(new IContributionItem[toReturn.size()]);
	}

	private MenuManager getEndPointMenuManagerFromEndpointsystem(EndpointSystemImpl endpointSystem) {
		final MenuManager endPointMenuManager = new MenuManager(endpointSystem.getSystemName(), null);
		endPointMenuManager.setRemoveAllWhenShown(true);
		IMenuListener menuDynamicListener = new EndpointsystemMenulistener(endPointMenuManager, endpointSystem);
		endPointMenuManager.addMenuListener(menuDynamicListener);
		return endPointMenuManager;
	}
}