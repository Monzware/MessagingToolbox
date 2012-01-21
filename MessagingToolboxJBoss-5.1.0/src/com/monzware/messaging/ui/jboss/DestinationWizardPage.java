package com.monzware.messaging.ui.jboss;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.wizards.MessagingSystemWizardExtention;
import com.monzware.messaging.toolbox.jboss.Activator;
import com.monzware.messaging.toolbox.jboss510.JBossEndpointImpl;
import com.monzware.messaging.toolbox.jboss510.JBossEndpointSystemImpl;
import com.monzware.messaging.ui.preferences.jboss.VendorPreferenceConstants;

public class DestinationWizardPage extends WizardPage implements MessagingSystemWizardExtention {

	private JBossEndpointSystemImpl system;
	private Table table;
	private Label statusLabel;

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

		/*
		 * Text systemName = new Text(container, SWT.BORDER | SWT.SINGLE);
		 * systemName.setEditable(true);
		 * 
		 * GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		 * systemName.setLayoutData(gd);
		 */

		table = new Table(container, SWT.CHECK | SWT.SINGLE | SWT.BORDER);

		// table.setItemCount(100);

		/*
		 * table.addListener(SWT.SetData, new Listener() { public void
		 * handleEvent(Event event) { TableItem item = (TableItem) event.item;
		 * int index = table.indexOf(item); item.setText("Item " + index);
		 * System.out.println(item.getText()); } });
		 */

		GridData gd = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(gd);

		statusLabel = new Label(container, SWT.NULL);

		statusLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));

		gd = new GridData(GridData.FILL_HORIZONTAL);
		statusLabel.setLayoutData(gd);

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
			IPreferenceStore ps = Activator.getDefault().getPreferenceStore();
			String configPath = ps.getString(VendorPreferenceConstants.P_PATH);

			File file = new File(configPath + "\\client\\jbossall-client.jar");
			System.out.println("Exist: " + file.exists());

			URL[] urls = new URL[1];
			urls[0] = file.toURI().toURL();
			URLClassLoader urlClassLoader = new URLClassLoader(urls, oldCL);

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

	@Override
	public void setEndpointSystem(EndpointSystem system) {
		this.system = (JBossEndpointSystemImpl) system;

	}

	@Override
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
