package com.monzware.messaging.ui.jboss;

public class Destination implements Comparable<Destination> {

	private final boolean queue;
	private final String name;

	public Destination(boolean queue, String name) {
		this.queue = queue;
		this.name = name;
	}

	public int compareTo(Destination o) {

		int compareTo = Boolean.valueOf(queue).compareTo(Boolean.valueOf(o.isQueue()));
		if (compareTo != 0) {
			return compareTo;
		}

		return name.compareTo(o.getName());
	}

	public boolean isQueue() {
		return queue;
	}

	public String getName() {
		return name;
	}
}
