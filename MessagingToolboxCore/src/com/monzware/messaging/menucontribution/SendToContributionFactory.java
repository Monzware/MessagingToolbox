package com.monzware.messaging.menucontribution;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.menus.AbstractContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

public class SendToContributionFactory extends AbstractContributionFactory {

	public SendToContributionFactory() {
		super("popup:org.eclipse.ui.popup.any?after=additions", null);
	}

	public void createContributionItems(final IServiceLocator serviceLocator, IContributionRoot additions) {

		IContributionItem item = new SendToContributionItem();
		
				
		additions.addContributionItem(item, new IFileOnlyExpression()  );
	}
}