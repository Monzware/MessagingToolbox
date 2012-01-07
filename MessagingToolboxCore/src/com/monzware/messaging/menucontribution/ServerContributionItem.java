package com.monzware.messaging.menucontribution;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.CompoundContributionItem;

import com.monzware.messaging.toolbox.core.popup.actions.SendToEndpointAction;

public final class ServerContributionItem extends CompoundContributionItem {
	public ServerContributionItem() {
		super();
	}

	protected IContributionItem[] getContributionItems() {

		List<MenuManager> toReturn = new ArrayList<MenuManager>();
		final MenuManager endPointMenuManager = new MenuManager("Intern test2", null);

		endPointMenuManager.setRemoveAllWhenShown(true);
		IMenuListener menuDynamicListener = new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				
				endPointMenuManager.add(new SendToEndpointAction("queue/dlg"));

				endPointMenuManager.add(new SendToEndpointAction("queue/messagedistribution_contact_out"));

			}
		};
		endPointMenuManager.addMenuListener(menuDynamicListener);
		toReturn.add(endPointMenuManager);

		return (IContributionItem[]) toReturn.toArray(new IContributionItem[toReturn.size()]);
	}
}