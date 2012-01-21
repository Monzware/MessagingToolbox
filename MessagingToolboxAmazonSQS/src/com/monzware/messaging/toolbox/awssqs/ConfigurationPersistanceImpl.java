package com.monzware.messaging.toolbox.awssqs;

import java.util.Collection;

import org.eclipse.ui.IMemento;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;
import com.monzware.messaging.toolbox.core.configmodel.PersistenceConstants;
import com.monzware.messaging.toolbox.providers.ConfigurationPersistance;

public class ConfigurationPersistanceImpl implements ConfigurationPersistance {

	private static final String SECRETACCESSKEY_ELEMENT = "SecretAccessKey";
	private static final String ACCESSKEYID_ELEMENT = "AccessKeyID";

	@Override
	public EndpointSystem getEndpointSystemFromConfiguration(String pluginId, String systemName, IMemento esMemento) {

		String accessKeyID = esMemento.getString(ACCESSKEYID_ELEMENT);
		String secretAccessKey = esMemento.getString(SECRETACCESSKEY_ELEMENT);

		AmazonSQSEndpointSystemImpl es = new AmazonSQSEndpointSystemImpl(pluginId, systemName, accessKeyID, secretAccessKey);

		IMemento endpoint = esMemento.getChild(PersistenceConstants.ENDPOINTS_ELEMENT);

		for (IMemento eMemento : endpoint.getChildren(PersistenceConstants.ENDPOINT_ELEMENT)) {

			String epName = eMemento.getString(PersistenceConstants.ENDPOINTNAME_ELEMENT);
			AmazonSQSEndpointImpl ep = new AmazonSQSEndpointImpl(es, epName);
			es.add(ep);

		}

		return es;
	}

	@Override
	public void addEndpointSystemToConfiguration(EndpointSystem system, IMemento endpointSystemMemento) {

		AmazonSQSEndpointSystemImpl sys = (AmazonSQSEndpointSystemImpl) system;

		endpointSystemMemento.putString(PersistenceConstants.SYSTEMNAME_ELEMENT, sys.getSystemName());

		endpointSystemMemento.putString(ACCESSKEYID_ELEMENT, sys.getAccessKeyID());
		endpointSystemMemento.putString(SECRETACCESSKEY_ELEMENT, sys.getSecretAccessKey());

		endpointSystemMemento.putString(PersistenceConstants.PROVIDER_PLUGINID_ELEMENT, system.getProviderId());

		Collection<? extends Endpoint> endpoints = sys.getEndpoints();

		IMemento endpointsMementoChild = endpointSystemMemento.createChild(PersistenceConstants.ENDPOINTS_ELEMENT);

		for (Endpoint endpoint : endpoints) {
			IMemento endpointMementoChild = endpointsMementoChild.createChild(PersistenceConstants.ENDPOINT_ELEMENT);
			endpointMementoChild.putString(PersistenceConstants.ENDPOINTNAME_ELEMENT, endpoint.getName());
		}
	}
}
