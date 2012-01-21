package com.monzware.messaging.toolbox.jboss510;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.eclipse.jface.preference.IPreferenceStore;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;
import com.monzware.messaging.toolbox.jboss.Activator;
import com.monzware.messaging.ui.preferences.jboss.VendorPreferenceConstants;

public class JBossEndpointSender implements EndpointSender {

	private final String url;
	private final String epName;

	public JBossEndpointSender(JBossEndpointSystemImpl es, String epName) {
		this.epName = epName;
		String port = es.getPortNumber();
		String serverName = es.getServerName();

		url = "jnp://" + serverName + ":" + port;
	}

	@Override
	public void sendMessage(String message) {

		Thread currentThread = Thread.currentThread();
		ClassLoader oldCL = currentThread.getContextClassLoader();

		try {
			IPreferenceStore ps = Activator.getDefault().getPreferenceStore();
			String configPath = ps.getString(VendorPreferenceConstants.P_PATH);

			File file = new File(configPath + "\\client\\jbossall-client.jar");

			URL[] urls = new URL[1];
			urls[0] = file.toURI().toURL();
			URLClassLoader urlClassLoader = new URLClassLoader(urls, oldCL);

			currentThread.setContextClassLoader(urlClassLoader);

			Properties properties = new Properties();
			properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			properties.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");

			properties.put(Context.PROVIDER_URL, url);

			InitialContext jndiContext = new InitialContext(properties);

			Destination dest = (Destination) jndiContext.lookup(epName);

			ConnectionFactory facory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");

			Connection connection = facory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			TextMessage textMessage = session.createTextMessage(message);

			MessageProducer producer = session.createProducer(dest);
			producer.send(textMessage);

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}

		currentThread.setContextClassLoader(oldCL);

	}

}
