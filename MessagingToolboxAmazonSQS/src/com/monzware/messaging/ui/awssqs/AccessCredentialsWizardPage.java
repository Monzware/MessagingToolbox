package com.monzware.messaging.ui.awssqs;

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

import com.monzware.messaging.toolbox.awssqs.Activator;
import com.monzware.messaging.toolbox.awssqs.AmazonSQSEndpointSystemImpl;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardExtention;
import com.monzware.messaging.ui.preferences.awssqs.VendorPreferenceConstants;

public class AccessCredentialsWizardPage extends WizardPage implements MessagingSystemWizardExtention {

	private Text accessKeyID;
	private Text secretAccessKey;
	private AmazonSQSEndpointSystemImpl system;

	public AccessCredentialsWizardPage() {
		this("Test");
	}

	public AccessCredentialsWizardPage(String pageName) {
		super(pageName);
	}

	public AccessCredentialsWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	public void createControl(Composite parent) {

		IPreferenceStore ps = Activator.getDefault().getPreferenceStore();
		String defaultAccessKeyID = ps.getString(VendorPreferenceConstants.ACCESKEYID);
		String defaultSecretAccessKey = ps.getString(VendorPreferenceConstants.SECRETACCESSKEY);

		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NULL);
		label.setText("&Access Key ID:");

		accessKeyID = new Text(container, SWT.BORDER | SWT.SINGLE);
		accessKeyID.setEditable(true);
		accessKeyID.setText(defaultAccessKeyID);

		system.setAccessKeyID(defaultAccessKeyID);
		system.setSecretAccessKey(defaultSecretAccessKey);

		accessKeyID.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				system.setAccessKeyID(accessKeyID.getText());
			}
		});

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		accessKeyID.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText("&Secret Access Key:");

		secretAccessKey = new Text(container, SWT.BORDER | SWT.SINGLE);
		secretAccessKey.setEditable(true);
		secretAccessKey.setText(defaultSecretAccessKey);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		secretAccessKey.setLayoutData(gd);

		secretAccessKey.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				system.setSecretAccessKey(secretAccessKey.getText());
			}
		});

		setControl(container);

	}

	public void setEndpointSystem(EndpointSystem system) {
		this.system = (AmazonSQSEndpointSystemImpl) system;
	}

	public void updateEndPointSystem() {
		system.setAccessKeyID(accessKeyID.getText());
		system.setSecretAccessKey(secretAccessKey.getText());
	}

}
