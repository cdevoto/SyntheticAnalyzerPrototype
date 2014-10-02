package com.compuware.apm.ruxit.synth.analyzer.model;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Attributes implements Iterable<Attribute<?>> {

	private Set<Attribute<?>> attributes = new LinkedHashSet<>();
	
	public static Builder newAttributes () {
		return new Builder();
	}
	
	private Attributes (Builder builder) {
		this.attributes.addAll(builder.attributes);
	}
	
	public boolean contains (Attribute<?> attribute) {
		return this.attributes.contains(attribute);
	}
	
	public boolean containsAll (Attributes attributes) {
		return this.attributes.containsAll(attributes.attributes);
	}

	public int size () {
		return this.attributes.size();
	}
	
	@Override
	public Iterator<Attribute<?>> iterator() {
		return this.attributes.iterator();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
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
		Attributes other = (Attributes) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Attributes [" + attributes + "]";
	}

	public static class Builder {
		private Set<Attribute<?>> attributes = new LinkedHashSet<>();

		private Builder () {}
		
		public Builder withAttribute(Attribute<?> attribute) {
			this.attributes.add(attribute);
			return this;
		}

		public Builder withAttributes(Attribute<?> ... attributes) {
			for (Attribute<?> attribute : attributes) {
			    this.attributes.add(attribute);
			}
			return this;
		}
	
		public Builder withAttributes(Set<Attribute<?>> attributes) {
			this.attributes.addAll(attributes);
			return this;
		}
		
		public Attributes build () {
			return new Attributes(this);
		}
	}

}
