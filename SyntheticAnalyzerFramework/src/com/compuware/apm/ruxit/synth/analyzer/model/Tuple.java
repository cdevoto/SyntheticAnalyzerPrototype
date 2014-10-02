package com.compuware.apm.ruxit.synth.analyzer.model;




public interface Tuple {

	public static Tuple NULL = TupleImpl.newTuple(Attributes.newAttributes().build()).build();

	public Attributes getAttributes();
	public Tuple project(Attributes attributes);
	public <T> T get(Attribute<T> attribute);
	
	public static interface Builder {
		public void clearValues ();
		public <T> Builder withValue(Attribute<T> attribute, T value);
		public Builder withUncheckedValue(@SuppressWarnings("rawtypes") Attribute attribute, Object value);
		public Tuple build ();
	}

}
