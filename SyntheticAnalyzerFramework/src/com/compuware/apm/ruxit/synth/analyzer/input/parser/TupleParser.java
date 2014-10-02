package com.compuware.apm.ruxit.synth.analyzer.input.parser;

import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;

public interface TupleParser {
	
	public Tuple parse (String line);

}
