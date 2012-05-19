package com.monzware.messaging.toolbox.core.configmodel;

import java.util.Collection;

public interface EndpointReceiver {

	public Collection<EndpointReceiverMessage> getMessages() throws EndpointReceiverException;

	public int size() throws EndpointReceiverException;

}
