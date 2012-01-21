package com.monzware.messaging.ui.awssqs;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.monzware.messaging.toolbox.awssqs.AmazonSQSEndpointImpl;
import com.monzware.messaging.toolbox.awssqs.AmazonSQSEndpointSystemImpl;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardExtention;

public class DestinationWizardPage extends WizardPage implements MessagingSystemWizardExtention {

	private AmazonSQSEndpointSystemImpl system;
	private Table table;
	private Image endpointImage;

	public DestinationWizardPage() {
		super("Destinations");
		setDescription("Select destinations to use for system");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 9;

		endpointImage = new Image(container.getDisplay(), getClass().getResourceAsStream("/icons/endpoint.png"));

		table = new Table(container, SWT.CHECK | SWT.SINGLE | SWT.BORDER);

		GridData gd = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(gd);

		Image image = new Image(container.getDisplay(), getClass().getResourceAsStream("/icons/refresh.gif"));

		Button button = new Button(container, SWT.PUSH);
		button.setImage(image);

		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				BusyIndicator.showWhile(e.display, new Runnable() {

					@Override
					public void run() {
						lookupDestinations();
					}
				});
			}
		});

		setControl(container);
	}

	private void lookupDestinations() {

		table.removeAll();

		AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(system.getAccessKeyID(), system.getSecretAccessKey()));

		try {
			for (String queueUrl : sqs.listQueues().getQueueUrls()) {

				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(queueUrl);
				item.setImage(endpointImage);
			}
			setErrorMessage(null);
		} catch (AmazonServiceException e) {
			if (e.getStatusCode() == 403) {
				setErrorMessage("Wrong credential for Amazon SQS");
			} else {
				setErrorMessage("Unable to get destinations from Amazon SQS");
			}
		}
	}

	public void setVisible(boolean visible) {

		if (visible) {
			lookupDestinations();
		}

		super.setVisible(visible);
	}

	@Override
	public void setEndpointSystem(EndpointSystem system) {
		this.system = (AmazonSQSEndpointSystemImpl) system;

	}

	@Override
	public void updateEndPointSystem() {

		if (table != null) {

			TableItem[] items = table.getItems();
			for (TableItem tableItem : items) {

				if (tableItem.getChecked()) {
					AmazonSQSEndpointImpl ep = new AmazonSQSEndpointImpl(system, tableItem.getText());
					system.add(ep);
				}
			}
		}
	}
}
