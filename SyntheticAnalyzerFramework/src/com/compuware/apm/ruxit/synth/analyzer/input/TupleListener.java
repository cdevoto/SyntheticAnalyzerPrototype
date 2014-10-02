package com.compuware.apm.ruxit.synth.analyzer.input;

import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;

public interface TupleListener {
	
	public void onTupleReceived (Tuple tuple);

}
