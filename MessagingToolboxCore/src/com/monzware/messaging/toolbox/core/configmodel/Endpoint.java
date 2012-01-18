package com.monzware.messaging.toolbox.core.configmodel;

public interface Endpoint {

	EndpointSender getEndpointSender();

	EndpointReceiver getEndpointReceiver();

	String getName();

	String getId();

}
