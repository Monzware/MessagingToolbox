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

import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardEditableExtention;
import com.monzware.messaging.toolbox.jboss.Activator;
import com.monzware.messaging.toolbox.jboss510.JBossEndpointSystemImpl;
import com.monzware.messaging.ui.preferences.jboss.VendorPreferenceConstants;

public class ServerWizardPage extends WizardPage implements MessagingSystemWizardEditableExtention<JBossEndpointSystemImpl> {

	private Text serverName;
	private Text port;
	private JBossEndpointSystemImpl newSystem;
	private JBossEndpointSystemImpl oldSystem;

	public ServerWizardPage() {
		this("Test");
	}

	public ServerWizardPage(String pageName) {
		super(pageName);
	}

	public ServerWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

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

		serverName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				newSystem.setServerName(serverName.getText());
			}
		});

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		serverName.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText("&Port:");

		port = new Text(container, SWT.BORDER | SWT.SINGLE);
		port.setEditable(true);

		if (oldSystem == null) {
			IPreferenceStore ps = Activator.getDefault().getPreferenceStore();
			String defaultServer = ps.getString(VendorPreferenceConstants.DEFAULTSERVER);
			String defaultPort = ps.getString(VendorPreferenceConstants.DEFAULTPORT);

			serverName.setText(defaultServer);
			port.setText(defaultPort);
			newSystem.setServerName(defaultServer);
			newSystem.setPortNumber(defaultPort);

		} else {
			serverName.setText(oldSystem.getServerName());
			port.setText(oldSystem.getPortNumber());
			newSystem.setServerName(oldSystem.getServerName());
			newSystem.setPortNumber(oldSystem.getPortNumber());
		}

		port.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				newSystem.setPortNumber(port.getText());
			}
		});

		setControl(container);

	}

	public void setNewSystem(JBossEndpointSystemImpl newSystem) {
		this.newSystem = newSystem;
	}

	public void updateNewSystem() {
		newSystem.setServerName(serverName.getText());
		newSystem.setPortNumber(port.getText());
	}

	public void setOldSystem(JBossEndpointSystemImpl oldSystem) {
		this.oldSystem = oldSystem;
	}

}
