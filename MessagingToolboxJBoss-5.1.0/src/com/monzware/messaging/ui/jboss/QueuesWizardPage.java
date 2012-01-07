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
import org.eclipse.swt.widgets.Text;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.wizards.intf.MessagingSystemWizardExtention;
import com.monzware.messaging.toolbox.jboss.Activator;
import com.monzware.messaging.ui.preferences.jboss.VendorPreferenceConstants;

public class QueuesWizardPage extends WizardPage implements MessagingSystemWizardExtention {

	private Text systemName;
	private Text port;
	private EndpointSystem system;

	public QueuesWizardPage() {
		super("Queues");
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

		systemName = new Text(container, SWT.BORDER | SWT.SINGLE);
		systemName.setEditable(true);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		systemName.setLayoutData(gd);

		setControl(container);

		// lookupDestinations();

	}

	private void lookupDestinations() {

		Thread currentThread = Thread.currentThread();
		ClassLoader oldCL = currentThread.getContextClassLoader();

		try {

			String port = system.getPortNumber();
			String serverName = system.getServerName();

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

			properties.put(Context.PROVIDER_URL, "jnp://" + serverName + ":" + port);

			InitialContext jndiContext;

			jndiContext = new InitialContext(properties);

			NamingEnumeration<NameClassPair> list = jndiContext.list("/queue");
			while (list.hasMore()) {
				NameClassPair nc = list.next();
				System.out.println(nc.getName());
				nc.getClassName();
			}

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
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
		this.system = system;

	}

	@Override
	public void updateEndPointSystem() {
		system.getEndpoints().add(new Endpoint("Name1"));
		system.getEndpoints().add(new Endpoint("Name2"));
		system.getEndpoints().add(new Endpoint("Name3"));
		system.getEndpoints().add(new Endpoint("Name4"));
		system.getEndpoints().add(new Endpoint("Name5"));
		system.getEndpoints().add(new Endpoint("Name6"));
		system.getEndpoints().add(new Endpoint("Name7"));

	}

}
