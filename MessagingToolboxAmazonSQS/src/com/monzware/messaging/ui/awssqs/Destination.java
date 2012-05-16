package com.monzware.messaging.ui.awssqs;

public class Destination implements Comparable<Destination> {

	private final String name;
	private final boolean isNew;

	public Destination(String name) {
		this(name, true);
	}

	public Destination(String name, boolean isNew) {
		this.name = name;
		this.isNew = isNew;
	}

	public int compareTo(Destination o) {
		return name.compareTo(o.getName());
	}

	public String getName() {
		return name;
	}

	public boolean isNew() {
		return isNew;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Destination other = (Destination) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
