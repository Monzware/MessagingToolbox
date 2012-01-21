package com.monzware.messaging.ui.jboss;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardExtention;
import com.monzware.messaging.toolbox.jboss.Activator;
import com.monzware.messaging.toolbox.jboss510.JBossEndpointSystemImpl;
import com.monzware.messaging.ui.preferences.jboss.VendorPreferenceConstants;

public class ServerWizardPage extends WizardPage implements MessagingSystemWizardExtention {

	private Text serverName;
	private Text port;
	private JBossEndpointSystemImpl system;

	public ServerWizardPage() {
		this("Test");
	}

	public ServerWizardPage(String pageName) {
		super(pageName);
	}

	public ServerWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {

		IPreferenceStore ps = Activator.getDefault().getPreferenceStore();
		String defaultServer = ps.getString(VendorPreferenceConstants.DEFAULTSERVER);
		String defaultPort = ps.getString(VendorPreferenceConstants.DEFAULTPORT);

		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText("&Server:");

		serverName = new Text(container, SWT.BORDER | SWT.SINGLE);
		serverName.setEditable(true);
		serverName.setText(defaultServer);

		system.setServerName(defaultServer);
		system.setPortNumber(defaultPort);

		serverName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				system.setServerName(serverName.getText());
			}
		});

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		serverName.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText("&Port:");

		port = new Text(container, SWT.BORDER | SWT.SINGLE);
		port.setEditable(true);
		port.setText(defaultPort);

		port.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				system.setPortNumber(port.getText());
			}
		});

		setControl(container);

	}

	@Override
	public void setEndpointSystem(EndpointSystem system) {
		this.system = (JBossEndpointSystemImpl) system;
	}

	@Override
	public void updateEndPointSystem() {
		system.setServerName(serverName.getText());
		system.setPortNumber(port.getText());
	}

}
