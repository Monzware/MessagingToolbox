package com.monzware.messaging.toolbox.providers;

import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public interface ObjectFactory {

	EndpointSystem createEndpointSystem(String providerExtensionId);

}
