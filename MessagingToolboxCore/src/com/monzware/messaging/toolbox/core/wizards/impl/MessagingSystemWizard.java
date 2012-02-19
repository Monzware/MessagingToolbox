package com.monzware.messaging.toolbox.core.wizards.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.monzware.messaging.toolbox.EndpointManager;
import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.toolbox.core.model.ProviderConfiguration;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardExtention;

public class MessagingSystemWizard extends Wizard implements INewWizard {

	private List<ProviderConfiguration> providerConfigurations = new ArrayList<ProviderConfiguration>();

	private MessagingSystemWizardStartPage startPage = null;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public MessagingSystemWizard() {
		super();
		setNeedsProgressMonitor(true);

		setWindowTitle("Add Message system");

		IExtensionRegistry registry = Platform.getExtensionRegistry();

		IExtensionPoint providersExtensionPoint = registry.getExtensionPoint("com.monzware.messaging.providers");

		if (providersExtensionPoint == null)
			return;
		IExtension[] extensions = providersExtensionPoint.getExtensions();

		for (IExtension providerExtension : extensions) {

			try {

				ProviderConfiguration pc = new ProviderConfiguration(providerExtension, this);
				providerConfigurations.add(pc);

			} catch (Exception e) {
				System.out.println("Error " + e);
			}
		}

		startPage = new MessagingSystemWizardStartPage(providerConfigurations);
	}

	public IWizardPage getNextPage(IWizardPage page) {
		ProviderConfiguration providerConfiguration = startPage.getProviderConfiguration();

		List<WizardPage> pages = providerConfiguration.getProviderWizardPages();

		if (pages.isEmpty()) {
			return null;
		}

		if (page == startPage) {
			WizardPage wizardPage = pages.get(0);
			return wizardPage;
		}

		for (int i = 0; i < pages.size(); i++) {

			WizardPage wizardPage = pages.get(i);

			if (i == pages.size() - 1) {
				return null;
			} else if (wizardPage == page) {
				return pages.get(i + 1);
			}
		}

		return null;
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		addPage(startPage);
		super.addPages();
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {

		startPage.updateEndPointSystem();

		ProviderConfiguration providerConfiguration = startPage.getProviderConfiguration();

		List<WizardPage> pages = providerConfiguration.getProviderWizardPages();
		for (WizardPage wizardPage : pages) {
			if (wizardPage instanceof MessagingSystemWizardExtention) {
				MessagingSystemWizardExtention extPage = (MessagingSystemWizardExtention) wizardPage;
				extPage.updateEndPointSystem();
			}
		}

		EndpointManager endpointManager = MessagingToolboxPlugin.getDefault().getEndpointManager();
		endpointManager.addEndpointSystem(providerConfiguration.getEndpointSystem());
		endpointManager.saveState();

		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

}