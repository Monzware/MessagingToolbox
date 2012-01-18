package com.monzware.messaging.toolbox.core.wizards.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
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
import org.osgi.framework.Bundle;

import com.monzware.messaging.toolbox.EndpointManager;
import com.monzware.messaging.toolbox.MessagingToolboxPlugin;
import com.monzware.messaging.toolbox.core.configmodel.impl.EndpointSystemImpl;
import com.monzware.messaging.toolbox.core.model.VendorConfiguration;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardExtention;
import com.monzware.messaging.toolbox.vendor.VendorFacade;

public class MessagingSystemWizard extends Wizard implements INewWizard {

	private List<VendorConfiguration> vendorConfigurations = new ArrayList<VendorConfiguration>();

	private EndpointSystemImpl system = new EndpointSystemImpl();

	private MessagingSystemWizardPage page = new MessagingSystemWizardPage(vendorConfigurations, system);

	/**
	 * Constructor for SampleNewWizard.
	 */
	public MessagingSystemWizard() {
		super();
		setNeedsProgressMonitor(true);

		setWindowTitle("Add Message system");

		IExtensionRegistry registry = Platform.getExtensionRegistry();

		IExtensionPoint point = registry.getExtensionPoint("com.monzware.messaging.vendor");

		if (point == null)
			return;
		IExtension[] extensions = point.getExtensions();

		for (IExtension iExtension : extensions) {

			try {

				VendorConfiguration vc = new VendorConfiguration();
				vendorConfigurations.add(vc);

				String pluginName = iExtension.getContributor().getName();
				Bundle bundle = Platform.getBundle(pluginName);

				IConfigurationElement[] configurationElements = iExtension.getConfigurationElements();
				for (IConfigurationElement iConfigurationElement : configurationElements) {

					if (iConfigurationElement.getName().equals("facade")) {

						String vendorName = iConfigurationElement.getAttribute("name");
						String facadeClass = iConfigurationElement.getAttribute("class");

						Class<?> loadClass = bundle.loadClass(facadeClass);
						VendorFacade facade = (VendorFacade) loadClass.newInstance();

						vc.setSystemName(vendorName);
						vc.setSystemId(pluginName);
						vc.setFacade(facade);

					} else if (iConfigurationElement.getName().equals("configurationwizardpage")) {

						String systemName = iConfigurationElement.getAttribute("name");
						String pageClass = iConfigurationElement.getAttribute("class");

						Class<?> loadClass = bundle.loadClass(pageClass);

						WizardPage page = (WizardPage) loadClass.newInstance();
						page.setWizard(this);
						page.setTitle(systemName);

						if (page instanceof MessagingSystemWizardExtention) {
							MessagingSystemWizardExtention extPage = (MessagingSystemWizardExtention) page;
							extPage.setEndpointSystem(system);
						}

						vc.addConfigurationWizardPage(page);

					}

				}
			} catch (Exception e) {
				System.out.println("Error " + e);
			}

		}
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		VendorConfiguration vendorConfiguration = this.page.getVendorConfiguration();

		List<WizardPage> pages = vendorConfiguration.getPages();

		if (pages.isEmpty()) {
			return null;
		}

		if (page == this.page) {
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
		addPage(page);
		super.addPages();
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	public boolean performFinish() {

		page.updateEndPointSystem();

		VendorConfiguration vendorConfiguration = page.getVendorConfiguration();

		List<WizardPage> pages = vendorConfiguration.getPages();
		for (WizardPage wizardPage : pages) {
			if (wizardPage instanceof MessagingSystemWizardExtention) {
				MessagingSystemWizardExtention extPage = (MessagingSystemWizardExtention) wizardPage;
				extPage.updateEndPointSystem();
			}
		}

		EndpointManager endpointManager = MessagingToolboxPlugin.getDefault().getEndpointManager();
		endpointManager.addEndpointSystem(system);
		endpointManager.saveState();

		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

}