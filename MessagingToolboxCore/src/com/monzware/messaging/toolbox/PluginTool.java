package com.monzware.messaging.toolbox;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.osgi.framework.Bundle;

public class PluginTool {

	private final Bundle bundle;

	public PluginTool(Bundle bundle) {
		this.bundle = bundle;
	}

	public WizardPage getConfigurationWizardPage(IConfigurationElement iConfigurationElement, Wizard messagingSystemWizard) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String systemName = iConfigurationElement.getAttribute("name");
		String pageClass = iConfigurationElement.getAttribute("class");

		Class<?> loadClass = bundle.loadClass(pageClass);

		WizardPage page = (WizardPage) loadClass.newInstance();
		page.setWizard(messagingSystemWizard);
		page.setTitle(systemName);

		return page;
	}
}
