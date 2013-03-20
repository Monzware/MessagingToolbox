package com.monzware.messaging.toolbox.jboss510;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiver;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverException;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiverMessage;
import com.monzware.messaging.toolbox.core.model.EndpointReceiverTextMessage;
import com.monzware.messaging.toolbox.jboss.Constants;
import com.monzware.messaging.toolbox.jboss510.classloader.JBossClientClassLoaderManager;

public class JBossEndpointReceiver implements EndpointReceiver {

	private final String url;
	private final JBossEndpointImpl ep;

	public JBossEndpointReceiver(JBossEndpointSystemImpl es, JBossEndpointImpl ep) {
		this.ep = ep;
		String port = es.getPortNumber();
		String serverName = es.getServerName();

		url = "jnp://" + serverName + ":" + port;
	}

	public Collection<EndpointReceiverMessage> getMessages() throws EndpointReceiverException {

		Thread currentThread = Thread.currentThread();
		ClassLoader oldCL = currentThread.getContextClassLoader();

		try {
			ClassLoader urlClassLoader = JBossClientClassLoaderManager.getClassLoader(oldCL);

			currentThread.setContextClassLoader(urlClassLoader);

			InitialContext jndiContext = createInitialContext(url);

			Destination dest = (Destination) jndiContext.lookup(ep.getName());

			ConnectionFactory facory = (ConnectionFactory) jndiContext.lookup("XAConnectionFactory");

			Connection connection = facory.createConnection();
			connection.start();
			Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);		

			Collection<EndpointReceiverMessage> result = new ArrayList<EndpointReceiverMessage>();

			MessageConsumer consumer = session.createConsumer(dest);

			long wait = 2000;
			
			Message message;
			while ((message = consumer.receive(wait)) != null) {				

				String messageId = message.getJMSMessageID();
				long jmsTimestamp = message.getJMSTimestamp();

				String userName = message.getStringProperty(Constants.USERNAME_KEY);

				Calendar timeStamp = new GregorianCalendar();
				timeStamp.setTimeInMillis(jmsTimestamp);

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String messageText = textMessage.getText();
					result.add(new EndpointReceiverTextMessage(messageId, messageText, timeStamp, userName));
				}
				
				wait = 100;
			}

			session.rollback();

			consumer.close();
			session.close();
			connection.close();

			return result;

		} catch (NameNotFoundException e) {
			throw new EndpointReceiverException(e.getMessage());
		} catch (NamingException e) {
			throw new EndpointReceiverException(e.getRootCause().getMessage());
		} catch (MalformedURLException e) {
			throw new EndpointReceiverException("MalformedURLException");
		} catch (JMSException e) {
			throw new EndpointReceiverException("JMSException");
		} finally {
			currentThread.setContextClassLoader(oldCL);
		}
	}

	public int size() {

		return 0;
	}

	public void clear() throws EndpointReceiverException {
		Thread currentThread = Thread.currentThread();
		ClassLoader oldCL = currentThread.getContextClassLoader();

		try {
			ClassLoader urlClassLoader = JBossClientClassLoaderManager.getClassLoader(oldCL);

			currentThread.setContextClassLoader(urlClassLoader);

			InitialContext jndiContext = createInitialContext(url);

			Destination dest = (Destination) jndiContext.lookup(ep.getName());

			ConnectionFactory facory = (ConnectionFactory) jndiContext.lookup("XAConnectionFactory");

			Connection connection = facory.createConnection();
			Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

			MessageConsumer consumer = session.createConsumer(dest);

			connection.start();			

			long wait = 2000;
			
			while ((consumer.receive(wait)) != null) {
				wait = 100;
			}

			session.commit();

			consumer.close();
			session.close();
			connection.close();

		} catch (NameNotFoundException e) {
			throw new EndpointReceiverException(e.getMessage());
		} catch (NamingException e) {
			throw new EndpointReceiverException(e.getRootCause().getMessage());
		} catch (MalformedURLException e) {
			throw new EndpointReceiverException("MalformedURLException");
		} catch (JMSException e) {
			throw new EndpointReceiverException("JMSException");
		} finally {
			currentThread.setContextClassLoader(oldCL);
		}
	}

	private InitialContext createInitialContext(String url) throws NamingException {
		Properties properties = new Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		properties.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");

		properties.put(Context.PROVIDER_URL, url);

		InitialContext jndiContext = new InitialContext(properties);
		return jndiContext;
	}
}
