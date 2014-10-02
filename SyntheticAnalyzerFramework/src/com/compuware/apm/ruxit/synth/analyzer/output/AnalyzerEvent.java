package com.compuware.apm.ruxit.synth.analyzer.output;

import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;

public class AnalyzerEvent {

	public static enum Type {
		ALERT,
		RETURN_TO_NORMAL,
		TIME_OUT
	}
	
	private Type type;
	private Tuple source;
	
	public AnalyzerEvent (Type type, Tuple source) {
		this.type = type;
		this.source = source;
	}
	
	public Type getType () {
		return this.type;
	}
	
	public Tuple getSource () {
		return this.source;
	}
}
