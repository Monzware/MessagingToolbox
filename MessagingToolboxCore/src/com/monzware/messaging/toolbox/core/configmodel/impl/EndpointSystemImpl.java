package com.monzware.messaging.toolbox.core.configmodel.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class EndpointSystemImpl implements EndpointSystem {

	private Collection<Endpoint> endpoints = new ArrayList<Endpoint>();

	public Collection<Endpoint> getEndpoints() {
		return endpoints;
	}

	private String vendorName;
	private String vendorId;
	private String systemName;

	private String serverName;
	private String portNumber;
	private String systemId;

	public String getSystemId() {
		return systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String toString() {
		return getSystemName();
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public void addEndpoint(String text) {
		Endpoint ep = new EndpointImpl(this, text);
		endpoints.add(ep);

	}
}
