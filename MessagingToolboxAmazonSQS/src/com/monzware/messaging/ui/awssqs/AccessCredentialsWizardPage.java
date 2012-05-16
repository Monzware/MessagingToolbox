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
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardEditableExtention;
import com.monzware.messaging.ui.preferences.awssqs.VendorPreferenceConstants;

public class AccessCredentialsWizardPage extends WizardPage implements MessagingSystemWizardEditableExtention<AmazonSQSEndpointSystemImpl> {

	private Text accessKeyID;
	private Text secretAccessKey;
	private AmazonSQSEndpointSystemImpl newSystem;
	private AmazonSQSEndpointSystemImpl oldSystem;

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

		accessKeyID.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				newSystem.setAccessKeyID(accessKeyID.getText());
			}
		});

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		accessKeyID.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText("&Secret Access Key:");

		secretAccessKey = new Text(container, SWT.BORDER | SWT.SINGLE);
		secretAccessKey.setEditable(true);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		secretAccessKey.setLayoutData(gd);

		secretAccessKey.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				newSystem.setSecretAccessKey(secretAccessKey.getText());
			}
		});

		if (oldSystem == null) {
			accessKeyID.setText(defaultAccessKeyID);
			secretAccessKey.setText(defaultSecretAccessKey);
			newSystem.setAccessKeyID(defaultAccessKeyID);
			newSystem.setSecretAccessKey(defaultSecretAccessKey);
		} else {
			accessKeyID.setText(oldSystem.getAccessKeyID());
			secretAccessKey.setText(oldSystem.getSecretAccessKey());
			newSystem.setAccessKeyID(oldSystem.getAccessKeyID());
			newSystem.setSecretAccessKey(oldSystem.getSecretAccessKey());
		}

		setControl(container);

	}

	public void setNewSystem(AmazonSQSEndpointSystemImpl newSystem) {
		this.newSystem = newSystem;
	}

	public void updateNewSystem() {
		newSystem.setAccessKeyID(accessKeyID.getText());
		newSystem.setSecretAccessKey(secretAccessKey.getText());
	}

	public void setOldSystem(AmazonSQSEndpointSystemImpl oldSystem) {
		this.oldSystem = oldSystem;
	}

}
