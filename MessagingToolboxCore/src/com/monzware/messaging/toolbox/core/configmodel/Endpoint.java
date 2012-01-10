package com.monzware.messaging.toolbox.core.configmodel;

import java.util.HashMap;
import java.util.Map;

public class Endpoint {

	private Map<String, String> properties = new HashMap<String, String>();

	private String name;
	private String id;

	private final EndpointSystem es;

	public Endpoint(EndpointSystem es, String name) {
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
	
	public String toString() {
		return getName();
	}

	public EndpointSystem getEndpointsystem() {
		return es;
	}

}
