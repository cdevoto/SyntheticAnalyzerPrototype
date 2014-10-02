package com.compuware.apm.ruxit.synth.analyzer.input;

public interface TupleSource {
	
	public void addTupleListener (TupleListener listener);
	public void removeTupleListener (TupleListener listener);

}
