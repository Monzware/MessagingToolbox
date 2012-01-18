package com.monzware.messaging.toolbox.core.configmodel;

public interface EndpointSystem {

	public boolean isActive();

	public void addEndpoint(String text);

	public void setServerName(String defaultServer);

	public String getServerName();

	public String getPortNumber();

	public void setPortNumber(String defaultPort);

}
