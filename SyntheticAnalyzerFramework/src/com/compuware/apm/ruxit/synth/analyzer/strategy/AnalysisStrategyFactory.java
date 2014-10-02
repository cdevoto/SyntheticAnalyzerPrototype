package com.compuware.apm.ruxit.synth.analyzer.strategy;

import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;

public interface AnalysisStrategyFactory {
	
	public AnalysisStrategy newAnalysisStrategy (Tuple key);

}
