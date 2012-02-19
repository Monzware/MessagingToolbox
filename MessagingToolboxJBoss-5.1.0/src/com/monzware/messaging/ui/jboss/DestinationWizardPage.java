package com.monzware.messaging.ui.jboss;

import java.net.MalformedURLException;
import java.util.Properties;

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

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardExtention;
import com.monzware.messaging.toolbox.jboss510.JBossEndpointImpl;
import com.monzware.messaging.toolbox.jboss510.JBossEndpointSystemImpl;
import com.monzware.messaging.toolbox.jboss510.classloader.JBossClientClassLoaderManager;

public class DestinationWizardPage extends WizardPage implements MessagingSystemWizardExtention {

	private JBossEndpointSystemImpl system;
	private Table table;
	private Image endpointImage;

	// private Label statusLabel;

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
						lookupDestinations();
					}
				});

			}
		});

		setControl(container);
	}

	private void lookupDestinations() {

		table.removeAll();

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

				TableItem item = new TableItem(table, SWT.NONE);
				item.setText("/queue/" + nc.getName());
				item.setImage(endpointImage);
			}

			NamingEnumeration<NameClassPair> topicList = jndiContext.list("/topic");
			while (topicList.hasMore()) {
				NameClassPair nc = topicList.next();
				System.out.println(nc.getName());
				nc.getClassName();

				TableItem item = new TableItem(table, SWT.NONE);
				item.setText("/topic/" + nc.getName());
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
			lookupDestinations();
		}

		super.setVisible(visible);
	}

	public void setEndpointSystem(EndpointSystem system) {
		this.system = (JBossEndpointSystemImpl) system;

	}

	public void updateEndPointSystem() {

		if (table != null) {

			TableItem[] items = table.getItems();
			for (TableItem tableItem : items) {

				if (tableItem.getChecked()) {
					JBossEndpointImpl ep = new JBossEndpointImpl(system, tableItem.getText());
					system.add(ep);
				}
			}
		}
	}
}
