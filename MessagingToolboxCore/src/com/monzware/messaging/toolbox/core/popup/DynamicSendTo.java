package com.monzware.messaging.toolbox.core.popup;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;

public class DynamicSendTo extends  CompoundContributionItem {
	
	public DynamicSendTo() {
		System.out.println("Test");
	}

	@Override
	protected IContributionItem[] getContributionItems() {
		 IContributionItem[] list = new IContributionItem[0];
		  /*  Map parms = new HashMap();
		    parms.put("groupBy", "Severity");
		    list[0] = new CommandContributionItem(null, 
		            "org.eclipse.ui.views.problems.grouping",
		            parms, null, null, null, "Severity", null,
		            null, CommandContributionItem.STYLE_PUSH);
		 
		    parms = new HashMap();
		    parms.put("groupBy", "None");
		    list[1] = new CommandContributionItem(null,
		            "org.eclipse.ui.views.problems.grouping",
		            parms, null, null, null, "None", null, null,
		            CommandContributionItem.STYLE_PUSH);*/
		    return list;
	}
	
}

