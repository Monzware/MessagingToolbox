package com.monzware.messaging.toolbox.awssqs;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class ObjectFactory implements com.monzware.messaging.toolbox.providers.ObjectFactory {

	

	@Override
	public EndpointSystem createEndpointSystem(String providerExtensionId) {
		return new AmazonSQSEndpointSystemImpl(providerExtensionId);
	}

}
