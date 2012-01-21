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

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.model.ProviderConfiguration;

public class MessagingSystemWizardStartPage extends WizardPage {

	private Text systemName;
	private Combo type;
	private final List<ProviderConfiguration> providerConfigurations;

	public MessagingSystemWizardStartPage(List<ProviderConfiguration> providerConfigurations) {
		super("Messaging system");
		this.providerConfigurations = providerConfigurations;

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

		for (ProviderConfiguration pc : providerConfigurations) {
			type.add(pc.toString());
		}

		type.select(0);
	}

	public ProviderConfiguration getProviderConfiguration() {
		int selectionIndex = type.getSelectionIndex();
		return providerConfigurations.get(selectionIndex);
	}

	public void updateEndPointSystem() {
		
		ProviderConfiguration vc = getProviderConfiguration();
		EndpointSystem endpointSystem = vc.getEndpointSystem();
		endpointSystem.setSystemName(systemName.getText());

	}
}
