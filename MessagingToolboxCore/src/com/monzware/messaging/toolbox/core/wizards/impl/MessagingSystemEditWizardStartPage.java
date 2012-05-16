package com.monzware.messaging.toolbox.core.wizards.impl;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class MessagingSystemEditWizardStartPage extends WizardPage {

	private Text systemName;
	private Text type;
	private final EndpointSystem oldEndpointSystem;
	private final EndpointSystem newEndpointSystem;
	private final String providerName;

	public MessagingSystemEditWizardStartPage(EndpointSystem oldEndpointSystem, EndpointSystem newEndpointSystem, String providerName) {
		super("Messaging system");
		this.oldEndpointSystem = oldEndpointSystem;
		this.newEndpointSystem = newEndpointSystem;
		this.providerName = providerName;

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

		type = new Text(container, SWT.BORDER | SWT.SINGLE);
		type.setEditable(false);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		type.setLayoutData(gd);

		setControl(container);

		initialize();
	}

	private void initialize() {
		systemName.setText(oldEndpointSystem.getSystemName());
		type.setText(providerName);

	}

	public void updateEndPointSystem() {
		newEndpointSystem.setSystemName(systemName.getText()); 
	}
}
