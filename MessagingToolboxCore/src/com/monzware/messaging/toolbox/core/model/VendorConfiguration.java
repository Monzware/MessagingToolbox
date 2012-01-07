package com.monzware.messaging.toolbox.core.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;

import com.monzware.messaging.toolbox.vendor.VendorFacade;

public class VendorConfiguration {

	private VendorFacade facade;
	private String systemName;
	private String systemId;

	private List<WizardPage> pages = new ArrayList<WizardPage>();

	public void setSystemName(String systemName) {
		this.systemName = systemName;

	}

	public void setFacade(VendorFacade facade) {
		this.facade = facade;

	}

	public void addConfigurationWizardPage(WizardPage page) {
		pages.add(page);

	}

	public List<WizardPage> getPages() {
		return pages;
	}

	public String toString() {
		return systemName;

	}

	public String getSystemName() {
		return systemName;

	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

}
