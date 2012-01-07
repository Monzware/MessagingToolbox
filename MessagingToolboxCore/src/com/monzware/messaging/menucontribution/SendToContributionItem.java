package com.monzware.messaging.menucontribution;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.CompoundContributionItem;

public class SendToContributionItem extends CompoundContributionItem {

	public SendToContributionItem() {
		super();
	}

	protected IContributionItem[] getContributionItems() {
		List<MenuManager> result = new ArrayList<MenuManager>();

		final MenuManager menuDynamic = new MenuManager("Send message to", null);

		menuDynamic.setRemoveAllWhenShown(true);
		IMenuListener menuDynamicListener = new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {

				menuDynamic.add(getServersMenus());

			}

			private IContributionItem getServersMenus() {
				IContributionItem item = new ServerContributionItem();
				return item;
			}
		};
		menuDynamic.addMenuListener(menuDynamicListener);
		result.add(menuDynamic);

		return (IContributionItem[]) result.toArray(new IContributionItem[result.size()]);
	}
}