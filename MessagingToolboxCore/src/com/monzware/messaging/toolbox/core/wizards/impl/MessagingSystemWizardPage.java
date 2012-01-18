package com.monzware.messaging.toolbox.core.wizards.impl;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.monzware.messaging.toolbox.core.configmodel.impl.EndpointSystemImpl;
import com.monzware.messaging.toolbox.core.model.VendorConfiguration;

public class MessagingSystemWizardPage extends WizardPage {

	private Text systemName;
	private Combo type;
	private final List<VendorConfiguration> vendorConfigurations;
	private final EndpointSystemImpl system;

	public MessagingSystemWizardPage(List<VendorConfiguration> vendorConfigurations, EndpointSystemImpl system) {
		super("Messaging system");
		this.vendorConfigurations = vendorConfigurations;
		this.system = system;

		setTitle("Define Messaging system");
		setDescription("Configure a Messaging system");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("&System name:");

		systemName = new Text(container, SWT.BORDER | SWT.SINGLE);
		systemName.setEditable(true);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		systemName.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText("&Type:");

		type = new Combo(container, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);

		type.setFocus();

		gd = new GridData(GridData.FILL_HORIZONTAL);
		type.setLayoutData(gd);

		initialize();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {

		for (VendorConfiguration vc : vendorConfigurations) {
			type.add(vc.toString());
		}

		type.select(0);
	}

	public VendorConfiguration getVendorConfiguration() {
		int selectionIndex = type.getSelectionIndex();
		return vendorConfigurations.get(selectionIndex);
	}

	public void updateEndPointSystem() {

		int index = type.getSelectionIndex();
		VendorConfiguration vc = vendorConfigurations.get(index);

		system.setSystemName(systemName.getText());		
		
		system.setVendorId(vc.getSystemId());
		system.setVendorName(vc.getSystemName());

	}
}
