package com.monzware.messaging.ui.awssqs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.monzware.messaging.toolbox.awssqs.AmazonSQSEndpointImpl;
import com.monzware.messaging.toolbox.awssqs.AmazonSQSEndpointSystemImpl;
import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardEditableExtention;

public class DestinationWizardPage extends WizardPage implements MessagingSystemWizardEditableExtention<AmazonSQSEndpointSystemImpl> {

	private AmazonSQSEndpointSystemImpl newSystem;
	private Table table;
	private Image endpointImage;
	private AmazonSQSEndpointSystemImpl oldSystem;

	public DestinationWizardPage() {
		super("Destinations");
		setDescription("Select destinations to use for system");
	}

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

					public void run() {
						addDestinations();
					}
				});
			}
		});

		setControl(container);
	}

	private void addDestinations() {

		table.removeAll();

		Set<Destination> distinations = new HashSet<Destination>();

		if (oldSystem != null) {
			Collection<? extends Endpoint> endpoints = oldSystem.getEndpoints();
			for (Endpoint endpoint : endpoints) {
				distinations.add(new Destination(endpoint.getName(), false));
			}
		}

		getDestinationsFromServer(distinations, newSystem);

		List<Destination> sortedDistinations = new ArrayList<Destination>(distinations);
		Collections.sort(sortedDistinations);

		for (Destination destination : sortedDistinations) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(destination.getName());
			item.setImage(endpointImage);
			item.setChecked(!destination.isNew());
		}
	}

	private void getDestinationsFromServer(Set<Destination> distinations, AmazonSQSEndpointSystemImpl newSystem2) {
		try {
			AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(newSystem.getAccessKeyID(), newSystem.getSecretAccessKey()));
			for (String queueUrl : sqs.listQueues().getQueueUrls()) {

				distinations.add(new Destination(queueUrl));
			}
			setErrorMessage(null);
		} catch (AmazonServiceException e) {
			if (e.getStatusCode() == 403) {
				setErrorMessage("Wrong credential for Amazon SQS");
			} else {
				setErrorMessage("Unable to get destinations from Amazon SQS");
			}
		} catch (AmazonClientException e) {
			setErrorMessage("Unable to get destinations from Amazon SQS.");
		} catch (Exception e) {
			setErrorMessage("Unable to get destinations from Amazon SQS because of unknown error");
		}
	}

	public void setVisible(boolean visible) {

		if (visible) {
			addDestinations();
		}

		super.setVisible(visible);
	}

	public void setNewSystem(AmazonSQSEndpointSystemImpl newSystem) {
		this.newSystem = newSystem;
	}

	public void updateNewSystem() {

		if (table != null) {

			TableItem[] items = table.getItems();
			for (TableItem tableItem : items) {

				if (tableItem.getChecked()) {
					AmazonSQSEndpointImpl ep = new AmazonSQSEndpointImpl(newSystem, tableItem.getText());
					newSystem.add(ep);
				}
			}
		}
	}

	public void setOldSystem(AmazonSQSEndpointSystemImpl oldSystem) {
		this.oldSystem = oldSystem;

	}
}
