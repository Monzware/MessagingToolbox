package com.monzware.messaging.toolbox.awssqs;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiver;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class AmazonSQSEndpointImpl implements Endpoint {

	private final AmazonSQSEndpointSystemImpl es;
	private final String epName;

	public AmazonSQSEndpointImpl(AmazonSQSEndpointSystemImpl es, String epName) {
		this.es = es;
		this.epName = epName;
	}

	public EndpointSender getEndpointSender() {
		return new AmazonSQSEndpointSender(es, epName);
	}

	public EndpointReceiver getEndpointReceiver() {
		return null;
	}

	public String getName() {
		return epName;
	}

	public String toString() {
		return getName();
	}

	public EndpointSystem getEndpointsystem() {
		return es;
	}
}
