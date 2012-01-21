package com.monzware.messaging.toolbox.jboss510;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiver;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class JBossEndpointImpl implements Endpoint {

	private final JBossEndpointSystemImpl es;
	private final String epName;

	public JBossEndpointImpl(JBossEndpointSystemImpl es, String epName) {
		this.es = es;
		this.epName = epName;
	}

	@Override
	public EndpointSender getEndpointSender() {
		return new JBossEndpointSender(es, epName);
	}

	@Override
	public EndpointReceiver getEndpointReceiver() {
		return null;
	}

	@Override
	public String getName() {
		return epName;
	}

	public String toString() {
		return getName();
	}

	@Override
	public EndpointSystem getEndpointsystem() {
		return es;
	}
}
