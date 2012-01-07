package com.monzware.messaging.ui.jboss;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.wizards.intf.MessagingSystemWizardExtention;

public class ServerWizardPage extends WizardPage implements MessagingSystemWizardExtention {

	private Text serverName;
	private Text port;
	private EndpointSystem system;

	public ServerWizardPage() {
		this("Test");
	}

	public ServerWizardPage(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	public ServerWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText("&Server:");

		serverName = new Text(container, SWT.BORDER | SWT.SINGLE);
		serverName.setEditable(true);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		serverName.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText("&Port:");

		port = new Text(container, SWT.BORDER | SWT.SINGLE);
		port.setEditable(true);

		setControl(container);

	}

	@Override
	public void setEndpointSystem(EndpointSystem system) {
		this.system = system;
	}

	@Override
	public void updateEndPointSystem() {
		system.setServerName(serverName.getText());
		system.setPortNumber(port.getText());
	}

}
