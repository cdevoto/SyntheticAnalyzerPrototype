package com.compuware.apm.ruxit.synth.analyzer.model;

public class Attribute <T> {

    private String name;
    private Class<T> type;
    private T defaultValue;

    public Attribute(Class<T> type, String name) {
    	this.type = type;
    	this.name = name;
    }
    
    public Attribute(Class<T> type, String name, T defaultValue) {
    	this.type = type;
    	this.name = name;
    	this.defaultValue = defaultValue;
    }

    public Class<?> getType () {
    	return this.type;
    }
    
    public String getName () {
    	return this.name;
    }
    
    public T getDefaultValue () {
    	return this.defaultValue;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		@SuppressWarnings("rawtypes")
		Attribute other = (Attribute) obj;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Attribute [name=" + name + ", type=" + type + ", defaultValue="
				+ defaultValue + "]";
	}


}
