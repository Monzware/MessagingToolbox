package com.monzware.messaging.toolbox.core.configmodel;

public interface EndpointSender {

	public void sendMessage(String message) throws EndpointSenderException;

}
