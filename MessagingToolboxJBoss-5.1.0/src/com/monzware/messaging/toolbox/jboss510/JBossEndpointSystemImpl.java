package com.monzware.messaging.toolbox.jboss510;

import java.util.ArrayList;
import java.util.Collection;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class JBossEndpointSystemImpl implements EndpointSystem {

	private final Collection<JBossEndpointImpl> eps = new ArrayList<JBossEndpointImpl>();
	private String systemName;
	private String serverName;
	private String serverPort;
	private String providerExtensionId;

	public JBossEndpointSystemImpl(String providerExtensionId) {
		this.providerExtensionId = providerExtensionId;

	}

	public JBossEndpointSystemImpl(String providerExtensionId, String systemName, String serverName, String serverPort) {
		this(providerExtensionId);
		this.systemName = systemName;
		this.serverName = serverName;
		this.serverPort = serverPort;
	}

	public void add(JBossEndpointImpl ep) {
		eps.add(ep);

	}

	public boolean isActive() {
		return false;
	}

	public String getServerName() {
		return serverName;
	}

	public String getPortNumber() {
		return serverPort;
	}

	public Collection<? extends Endpoint> getEndpoints() {
		return eps;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setPortNumber(String serverPort) {
		this.serverPort = serverPort;

	}

	public String toString() {
		return getSystemName();
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;

	}

	public String getProviderId() {
		return providerExtensionId;
	}

	public String getTooltipText() {
		return serverName + ":" + serverPort;
	}
}
