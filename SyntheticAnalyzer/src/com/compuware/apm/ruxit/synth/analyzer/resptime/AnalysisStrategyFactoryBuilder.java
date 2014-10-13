package com.compuware.apm.ruxit.synth.analyzer.resptime;

import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeThresholdConfig;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategyFactory;

public interface AnalysisStrategyFactoryBuilder {
	
	public AnalysisStrategyFactory build (Attributes keyAttributes, Tuple config, ResponseTimeThresholdConfig thresholds);

}
