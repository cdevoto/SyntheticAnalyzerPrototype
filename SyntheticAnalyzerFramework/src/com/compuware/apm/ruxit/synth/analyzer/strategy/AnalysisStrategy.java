package com.compuware.apm.ruxit.synth.analyzer.strategy;

import com.compuware.apm.ruxit.synth.analyzer.clock.ClockListener;
import com.compuware.apm.ruxit.synth.analyzer.input.TupleListener;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.output.EventSource;

public interface AnalysisStrategy extends TupleListener, ClockListener, EventSource, StrategySource {
	public static enum Status {
		NORMAL,
		ALERT
	}

	public static enum State {
		ACTIVE,
		IDLE
	}

	public Tuple getKey ();
	public long getTimeOfLastTuple ();
	public long getTimeOfLastClockTick ();
	public Status getStatus();
	public State getState();

}
