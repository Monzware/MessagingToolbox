package com.monzware.messaging.toolbox.core.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.WizardPage;
import org.osgi.framework.Bundle;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardExtention;
import com.monzware.messaging.toolbox.core.wizards.impl.MessagingSystemWizard;
import com.monzware.messaging.toolbox.providers.ObjectFactory;

public class ProviderConfiguration {

	private String providerName;
	private String providerExtensionId;

	private EndpointSystem es = null;

	private List<WizardPage> providerWizardPages = null;
	private final MessagingSystemWizard messagingSystemWizard;
	private IConfigurationElement[] configurationElements;
	private Bundle bundle;

	public ProviderConfiguration(IExtension providerExtension, MessagingSystemWizard messagingSystemWizard) {
		this.messagingSystemWizard = messagingSystemWizard;
		this.configurationElements = providerExtension.getConfigurationElements();

		String pluginName = providerExtension.getContributor().getName();
		bundle = Platform.getBundle(pluginName);

		providerExtensionId = providerExtension.getUniqueIdentifier();
		providerName = providerExtension.getLabel();
	}

	public List<WizardPage> getProviderWizardPages() {

		if (providerWizardPages == null) {
			providerWizardPages = new ArrayList<WizardPage>();
			try {

				for (IConfigurationElement iConfigurationElement : configurationElements) {

					if (iConfigurationElement.getName().equals("configurationwizardpage")) {
						WizardPage page = getConfigurationWizardPage(iConfigurationElement);

						if (page instanceof MessagingSystemWizardExtention) {
							MessagingSystemWizardExtention mswe = (MessagingSystemWizardExtention) page;
							mswe.setEndpointSystem(getEndpointSystem());

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

	private WizardPage getConfigurationWizardPage(IConfigurationElement iConfigurationElement) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String systemName = iConfigurationElement.getAttribute("name");
		String pageClass = iConfigurationElement.getAttribute("class");

		Class<?> loadClass = bundle.loadClass(pageClass);

		WizardPage page = (WizardPage) loadClass.newInstance();
		page.setWizard(messagingSystemWizard);
		page.setTitle(systemName);

		return page;
	}

}
