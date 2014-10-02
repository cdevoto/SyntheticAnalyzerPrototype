package com.compuware.apm.ruxit.synth.analyzer.model;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractTuple implements Tuple {
	
	private Attributes attributes;
	private Map<Attribute<?>, Object> values = new LinkedHashMap<>();
	
	protected AbstractTuple (Builder builder) {
		this.attributes = builder.attributes;
		this.values.putAll(builder.values);
	}

	@Override
	public Attributes getAttributes() {
		return this.attributes;
	}
	
	protected Map<Attribute<?>, Object> getValues () {
		return this.values;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Tuple project(Attributes attributes) {
		if (!this.attributes.containsAll(attributes)) {
			throw new IllegalArgumentException("The specified attribute set contains attributes which are not included in the schema for this tuple");
		}
		Tuple.Builder builder = newTupleBuilder(attributes);
		for (Attribute attribute : attributes) {
			builder.withValue(attribute, get(attribute));
		}
		Tuple projection = builder.build();
		return projection;
	}
	
	protected abstract Tuple.Builder newTupleBuilder(Attributes attributes);

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Attribute<T> attribute) {
		if (!attributes.contains(attribute)) {
			throw new IllegalArgumentException(String.format("The observation schema for class %s does not support the %s attribute.", getClass().getName(), attribute.getName()));
		}
		return (T) values.get(attribute);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
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
		AbstractTuple other = (AbstractTuple) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder(this.getClass().getSimpleName() + " [");
		boolean firstAttribute = true;
		for (Attribute<?> attribute : attributes) {
			if (firstAttribute) {
				firstAttribute = false;
			} else {
			    buf.append(", ");
			}
			buf.append(attribute.getName()).append("=").append(values.get(attribute));
		}
	    buf.append("]");
	    return buf.toString();
	}

	public static abstract class Builder implements Tuple.Builder {
		private Attributes attributes;
		private Map<Attribute<?>, Object> values;
		
		protected Builder(Attributes schema) {
			if (schema == null) {
				throw new NullPointerException();
			}
			this.attributes = schema;
			this.values = new LinkedHashMap<>();
		}
		
		@Override
		public final void clearValues () {
			values.clear();
		}
		
		@Override
		public <T> Builder withValue(Attribute<T> attribute, T value) {
			if (!attributes.contains(attribute)) {
				throw new IllegalArgumentException(String.format("The tuple schema does not support the %s attribute.", attribute.getName()));
			}
			values.put(attribute, value);
			return this;
		}
		
		@Override
		public Builder withUncheckedValue(@SuppressWarnings("rawtypes") Attribute attribute, Object value) {
			if (!attributes.contains(attribute)) {
				throw new IllegalArgumentException(String.format("The tuple schema does not support the %s attribute.", attribute.getName()));
			}
			values.put(attribute, value);
			return this;
		}
		
		public final Tuple build () {
			validateValues();
			return doBuild();
		}
		
		protected abstract Tuple doBuild();
		
		private void validateValues() {
			for (Attribute<?> attribute : attributes) {
				if (!values.containsKey(attribute) || values.get(attribute) == null) {
					throw new IllegalStateException(String.format("Expected a value for the %s attribute.", attribute.getName()));
				}
			}
		}
	}

}
