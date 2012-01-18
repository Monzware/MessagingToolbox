package com.monzware.messaging.toolbox.core.configmodel.impl;

import java.util.HashMap;
import java.util.Map;

import com.monzware.messaging.toolbox.core.configmodel.Endpoint;
import com.monzware.messaging.toolbox.core.configmodel.EndpointReceiver;
import com.monzware.messaging.toolbox.core.configmodel.EndpointSender;

public class EndpointImpl implements Endpoint {

	private Map<String, String> properties = new HashMap<String, String>();

	private String name;
	private String id;

	private final EndpointSystemImpl es;

	public EndpointImpl(EndpointSystemImpl es, String name) {
		this.es = es;
		this.name = name;
		id = "" + System.currentTimeMillis();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EndpointSender getEndpointSender() {
		return new EndpointSender() {

			@Override
			public void sendMessage(String message) {
				System.out.println("Send message " + message);

			}
		};

	}
	
	public EndpointReceiver getEndpointReceiver() {
		return null;
	}

	public String toString() {
		return getName();
	}

	public EndpointSystemImpl getEndpointsystem() {
		return es;
	}

}
