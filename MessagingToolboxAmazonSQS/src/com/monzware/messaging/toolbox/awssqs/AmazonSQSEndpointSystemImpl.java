package com.monzware.messaging.toolbox.awssqs;

import java.util.ArrayList;
import java.util.Collection;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSystem;

public class AmazonSQSEndpointSystemImpl implements EndpointSystem {

	private final Collection<AmazonSQSEndpointImpl> eps = new ArrayList<AmazonSQSEndpointImpl>();
	private String systemName;
	private String accessKeyID;
	private String secretAccessKey;
	private String providerExtensionId;

	public AmazonSQSEndpointSystemImpl(String providerExtensionId) {
		this.providerExtensionId = providerExtensionId;

	}

	public AmazonSQSEndpointSystemImpl(String providerExtensionId, String systemName, String accessKeyID, String secretAccessKey) {
		this(providerExtensionId);
		this.systemName = systemName;
		this.setAccessKeyID(accessKeyID);
		this.setSecretAccessKey(secretAccessKey);
	}

	public void add(AmazonSQSEndpointImpl ep) {
		eps.add(ep);

	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public Collection<? extends Endpoint> getEndpoints() {
		return eps;
	}

	@Override
	public String getSystemName() {
		return systemName;
	}

	public String toString() {
		return getSystemName();
	}

	@Override
	public void setSystemName(String systemName) {
		this.systemName = systemName;

	}

	@Override
	public String getProviderId() {
		return providerExtensionId;
	}

	public String getAccessKeyID() {
		return accessKeyID;
	}

	public void setAccessKeyID(String accessKeyID) {
		this.accessKeyID = accessKeyID;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}
}
