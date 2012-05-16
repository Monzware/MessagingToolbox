package com.monzware.messaging.ui.jboss;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

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

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardEditableExtention;
import com.monzware.messaging.toolbox.jboss510.JBossEndpointImpl;
import com.monzware.messaging.toolbox.jboss510.JBossEndpointSystemImpl;
import com.monzware.messaging.toolbox.jboss510.classloader.JBossClientClassLoaderManager;

public class DestinationWizardPage extends WizardPage implements MessagingSystemWizardEditableExtention<JBossEndpointSystemImpl> {

	private JBossEndpointSystemImpl newSystem;
	private Table table;
	private Image endpointImage;
	private JBossEndpointSystemImpl oldSystem;

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

	private void getDestinationsFromServer(Set<Destination> distinations, JBossEndpointSystemImpl system) {
		Thread currentThread = Thread.currentThread();
		ClassLoader oldCL = currentThread.getContextClassLoader();

		String port = system.getPortNumber();
		String serverName = system.getServerName();

		String url = "jnp://" + serverName + ":" + port;

		try {

			ClassLoader urlClassLoader = JBossClientClassLoaderManager.getClassLoader(oldCL);

			currentThread.setContextClassLoader(urlClassLoader);

			Properties properties = new Properties();
			properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			properties.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");

			properties.put(Context.PROVIDER_URL, url);

			InitialContext jndiContext = new InitialContext(properties);

			NamingEnumeration<NameClassPair> queueList = jndiContext.list("/queue");
			while (queueList.hasMore()) {
				NameClassPair nc = queueList.next();
				distinations.add(new Destination("/queue/" + nc.getName()));
			}

			NamingEnumeration<NameClassPair> topicList = jndiContext.list("/topic");
			while (topicList.hasMore()) {
				NameClassPair nc = topicList.next();
				distinations.add(new Destination("/topic/" + nc.getName()));
			}

			setErrorMessage(null);
		} catch (NamingException e) {
			// statusLabel.setText("Unable to get destinations from server");
			setErrorMessage("Unable to get destinations from server: " + url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		currentThread.setContextClassLoader(oldCL);

	}

	public void setVisible(boolean visible) {

		if (visible) {
			addDestinations();
		}

		super.setVisible(visible);
	}

	public void setNewSystem(JBossEndpointSystemImpl newSystem) {
		this.newSystem = newSystem;
	}

	public void updateNewSystem() {

		if (table != null) {

			TableItem[] items = table.getItems();
			for (TableItem tableItem : items) {

				if (tableItem.getChecked()) {
					JBossEndpointImpl ep = new JBossEndpointImpl(newSystem, tableItem.getText());
					newSystem.add(ep);
				}
			}
		}
	}

	public void setOldSystem(JBossEndpointSystemImpl oldSystem) {
		this.oldSystem = oldSystem;
	}
}
