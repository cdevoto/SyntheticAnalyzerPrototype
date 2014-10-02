package com.compuware.apm.ruxit.synth.analyzer.resptime;

import com.compuware.apm.ruxit.synth.analyzer.clock.ClockService;
import com.compuware.apm.ruxit.synth.analyzer.input.TupleSourceService;

public interface ClockServiceBuilder {
	
	public ClockService build (TupleSourceService tupleSourceService);

}
