package com.monzware.messaging.toolbox.core.wizards.impl;

import java.util.List;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import com.monzware.messaging.toolbox.EndpointManager;
import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.model.ProviderConfiguration;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardEditableExtention;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardExtention;

public class MessagingSystemEditWizard extends Wizard {

	private final EndpointSystem oldEndpointSystem;
	private ProviderConfiguration pc;
	private MessagingSystemEditWizardStartPage startPage;

	/**
	 * Constructor for SampleNewWizard.
	 * 
	 * @param oldEndpointSystem
	 */
	public MessagingSystemEditWizard(EndpointSystem oldEndpointSystem) {
		super();
		this.oldEndpointSystem = oldEndpointSystem;

		setNeedsProgressMonitor(true);

		setWindowTitle("Change Message system");

		IExtensionRegistry registry = Platform.getExtensionRegistry();

		IExtensionPoint providersExtensionPoint = registry.getExtensionPoint("com.monzware.messaging.providers");

		if (providersExtensionPoint == null)
			return;

		IExtension iExtension = getExtension(providersExtensionPoint, oldEndpointSystem.getProviderId());

		pc = new ProviderConfiguration(iExtension, this);

		String providerName = iExtension.getLabel();
		startPage = new MessagingSystemEditWizardStartPage(oldEndpointSystem, pc.getEndpointSystem(), providerName);
		addPage(startPage);

		List<WizardPage> providerWizardPages = pc.getProviderWizardPages();
		for (WizardPage page : providerWizardPages) {

			if (page instanceof MessagingSystemWizardEditableExtention) {
				@SuppressWarnings("unchecked")
				MessagingSystemWizardEditableExtention<EndpointSystem> mswe = (MessagingSystemWizardEditableExtention<EndpointSystem>) page;
				mswe.setOldSystem(oldEndpointSystem);
			}

			addPage(page);
		}
	}

	private IExtension getExtension(IExtensionPoint providersExtensionPoint, String providerId) {
		IExtension[] extensions = providersExtensionPoint.getExtensions();
		for (IExtension iExtension : extensions) {
			String providerExtensionId = iExtension.getUniqueIdentifier();
			if (providerExtensionId.equals(providerId)) {
				return iExtension;
			}
		}

		return null;
	}

	@Override
	public boolean performFinish() {
		EndpointSystem newEndpointSystem = pc.getEndpointSystem();

		startPage.updateEndPointSystem();

		List<WizardPage> pages = pc.getProviderWizardPages();
		for (WizardPage wizardPage : pages) {
			if (wizardPage instanceof MessagingSystemWizardExtention) {
				MessagingSystemWizardExtention<? extends EndpointSystem> extPage = (MessagingSystemWizardExtention<?>) wizardPage;
				extPage.updateNewSystem();
			}
		}

		EndpointManager endpointManager = MessagingToolboxPlugin.getDefault().getEndpointManager();
		endpointManager.replaceEndpointSystem(oldEndpointSystem, newEndpointSystem);
		endpointManager.saveState();
		return true;
	}
}