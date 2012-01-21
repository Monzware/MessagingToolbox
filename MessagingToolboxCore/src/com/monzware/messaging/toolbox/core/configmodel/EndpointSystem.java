package com.monzware.messaging.toolbox.core.configmodel;

import java.util.Collection;

public interface EndpointSystem {

	public boolean isActive();

	public Collection<? extends Endpoint> getEndpoints();

	public String getSystemName();

	public void setSystemName(String text);

	// public void setProviderName(String systemName);

	// public void setProviderId(String providerId);

	public String getProviderId();

}
