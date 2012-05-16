package com.monzware.messaging.toolbox.core.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.osgi.framework.Bundle;

import com.monzware.messaging.toolbox.PluginTool;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardExtention;
import com.monzware.messaging.toolbox.providers.ObjectFactory;

public class ProviderConfiguration {

	private String providerName;
	private String providerExtensionId;

	private EndpointSystem es = null;

	private List<WizardPage> providerWizardPages = null;
	private final Wizard messagingSystemWizard;
	private IConfigurationElement[] configurationElements;
	private Bundle bundle;
	private PluginTool pluginTool;

	public ProviderConfiguration(IExtension providerExtension, Wizard messagingSystemWizard) {
		this.messagingSystemWizard = messagingSystemWizard;
		this.configurationElements = providerExtension.getConfigurationElements();

		String pluginName = providerExtension.getContributor().getName();
		bundle = Platform.getBundle(pluginName);

		pluginTool = new PluginTool(bundle);

		providerExtensionId = providerExtension.getUniqueIdentifier();
		providerName = providerExtension.getLabel();
	}

	public List<WizardPage> getProviderWizardPages() {

		if (providerWizardPages == null) {
			providerWizardPages = new ArrayList<WizardPage>();
			try {

				for (IConfigurationElement iConfigurationElement : configurationElements) {

					if (iConfigurationElement.getName().equals("configurationwizardpage")) {
						WizardPage page = pluginTool.getConfigurationWizardPage(iConfigurationElement, messagingSystemWizard);

						if (page instanceof MessagingSystemWizardExtention) {
							@SuppressWarnings("unchecked")
							MessagingSystemWizardExtention<EndpointSystem> mswe = (MessagingSystemWizardExtention<EndpointSystem>) page;
							mswe.setNewSystem(getEndpointSystem());

						}
						providerWizardPages.add(page);
					}
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return providerWizardPages;
	}

	public String toString() {
		return providerName;

	}

	public String getProviderName() {
		return providerName;
	}

	public String getProviderExtensionId() {
		return providerExtensionId;
	}

	public EndpointSystem getEndpointSystem() {

		try {

			if (es == null) {

				for (IConfigurationElement iConfigurationElement : configurationElements) {
					if (iConfigurationElement.getName().equals("objectfactory")) {
						ObjectFactory factory = getObjectFactory(iConfigurationElement);
						es = factory.createEndpointSystem(getProviderExtensionId());
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return es;
	}

	private ObjectFactory getObjectFactory(IConfigurationElement iConfigurationElement) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String pageClass = iConfigurationElement.getAttribute("class");

		Class<?> loadClass = bundle.loadClass(pageClass);
		return (ObjectFactory) loadClass.newInstance();
	}
}
