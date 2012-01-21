package com.monzware.messaging.toolbox.jboss510;

import java.util.Collection;

import org.eclipse.ui.IMemento;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.configmodel.PersistenceConstants;
import com.monzware.messaging.toolbox.providers.ConfigurationPersistance;

public class ConfigurationPersistanceImpl implements ConfigurationPersistance {

	private static final String SERVERPORT_ELEMENT = "Port";
	private static final String SERVERNAME_ELEMENT = "ServerName";

	@Override
	public EndpointSystem getEndpointSystemFromConfiguration(String pluginId, String systemName, IMemento esMemento) {

		String serverName = esMemento.getString(SERVERNAME_ELEMENT);
		String serverPort = esMemento.getString(SERVERPORT_ELEMENT);

		JBossEndpointSystemImpl es = new JBossEndpointSystemImpl(pluginId, systemName, serverName, serverPort);

		IMemento endpoint = esMemento.getChild(PersistenceConstants.ENDPOINTS_ELEMENT);

		for (IMemento eMemento : endpoint.getChildren(PersistenceConstants.ENDPOINT_ELEMENT)) {

			String epName = eMemento.getString(PersistenceConstants.ENDPOINTNAME_ELEMENT);
			JBossEndpointImpl ep = new JBossEndpointImpl(es, epName);
			es.add(ep);

		}

		return es;
	}

	@Override
	public void addEndpointSystemToConfiguration(EndpointSystem system, IMemento endpointSystemMemento) {

		JBossEndpointSystemImpl sys = (JBossEndpointSystemImpl) system;

		endpointSystemMemento.putString(PersistenceConstants.SYSTEMNAME_ELEMENT, sys.getSystemName());

		endpointSystemMemento.putString(SERVERNAME_ELEMENT, sys.getServerName());
		endpointSystemMemento.putString(SERVERPORT_ELEMENT, sys.getPortNumber());

		endpointSystemMemento.putString(PersistenceConstants.PROVIDER_PLUGINID_ELEMENT, system.getProviderId());

		Collection<? extends Endpoint> endpoints = sys.getEndpoints();

		IMemento endpointsMementoChild = endpointSystemMemento.createChild(PersistenceConstants.ENDPOINTS_ELEMENT);

		for (Endpoint endpoint : endpoints) {
			IMemento endpointMementoChild = endpointsMementoChild.createChild(PersistenceConstants.ENDPOINT_ELEMENT);
			endpointMementoChild.putString(PersistenceConstants.ENDPOINTNAME_ELEMENT, endpoint.getName());
		}
	}
}
