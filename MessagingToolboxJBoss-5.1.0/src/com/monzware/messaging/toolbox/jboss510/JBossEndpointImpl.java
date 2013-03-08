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

	public EndpointSender getEndpointSender() {
		return new JBossEndpointSender(es, epName);
	}

	public EndpointReceiver getEndpointReceiver() {
		return new JBossEndpointReceiver(es, this);
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

	public boolean hasReceiver() {
		return true;
	}
}
