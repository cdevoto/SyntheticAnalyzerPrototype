package com.compuware.apm.ruxit.synth.analyzer;

import com.compuware.apm.ruxit.synth.analyzer.clock.ClockListener;
import com.compuware.apm.ruxit.synth.analyzer.clock.ClockService;
import com.compuware.apm.ruxit.synth.analyzer.input.TupleListener;
import com.compuware.apm.ruxit.synth.analyzer.input.TupleSourceService;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.output.EventListener;
import com.compuware.apm.ruxit.synth.analyzer.output.EventSource;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategyFactory;
import com.compuware.apm.ruxit.synth.analyzer.strategy.StrategyListener;

public interface Analyzer extends Service, EventSource, ClockListener, TupleListener, EventListener, StrategyListener {

	public ClockService getClockService();
	public TupleSourceService getTupleSourceService();
	public AnalysisStrategyFactory getAnalysisStrategyFactory();
	public Attributes getKeyAttributes();
	public long getStrategyTimeout();
	public Tuple getConfig();
}
