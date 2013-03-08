package com.monzware.messaging.toolbox.jboss510;

import java.net.MalformedURLException;
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
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSenderException;
import com.monzware.messaging.toolbox.jboss.Constants;
import com.monzware.messaging.toolbox.jboss510.classloader.JBossClientClassLoaderManager;

public class JBossEndpointSender implements EndpointSender {

	private final String url;
	private final String epName;

	public JBossEndpointSender(JBossEndpointSystemImpl es, String epName) {
		this.epName = epName;
		String port = es.getPortNumber();
		String serverName = es.getServerName();

		url = "jnp://" + serverName + ":" + port;
	}

	public void sendMessage(String message) throws EndpointSenderException {

		Thread currentThread = Thread.currentThread();
		ClassLoader oldCL = currentThread.getContextClassLoader();

		try {
			ClassLoader urlClassLoader = JBossClientClassLoaderManager.getClassLoader(oldCL);
			
			String userName = System.getProperty("user.name");

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
			
			textMessage.setStringProperty(Constants.USERNAME_KEY, userName);

			MessageProducer producer = session.createProducer(dest);
			producer.send(textMessage);

			producer.close();
			session.close();
			connection.close();

		} catch (NameNotFoundException e) {
			throw new EndpointSenderException(e.getMessage());
		} catch (NamingException e) {
			throw new EndpointSenderException(e.getRootCause().getMessage());
		} catch (MalformedURLException e) {
			throw new EndpointSenderException("MalformedURLException");
		} catch (JMSException e) {
			throw new EndpointSenderException("JMSException");
		} finally {
			currentThread.setContextClassLoader(oldCL);
		}
	}

}
