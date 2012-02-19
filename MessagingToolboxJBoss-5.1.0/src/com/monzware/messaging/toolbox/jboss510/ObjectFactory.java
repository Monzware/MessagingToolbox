package com.monzware.messaging.toolbox.jboss510;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class ObjectFactory implements com.monzware.messaging.toolbox.providers.ObjectFactory {

	public EndpointSystem createEndpointSystem(String providerExtensionId) {
		return new JBossEndpointSystemImpl(providerExtensionId);
	}

}
