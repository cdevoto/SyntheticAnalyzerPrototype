package com.compuware.apm.ruxit.synth.analyzer.resptime;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.strategy.BinomialTestResponseTimeStrategyFactory.*;

import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeThresholdConfig;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategyFactory;

public class BinomialTestResponseTimeAnalyzerFactory extends AbstractResponseTimeAnalyzerFactory {

	public static Builder newResponseTimeAnalyzerFactory () {
		return new Builder ();
	}

	private BinomialTestResponseTimeAnalyzerFactory (Builder builder) {
		super(builder);
	}

	protected AnalysisStrategyFactory createAnalysisStrategyFactory(Attributes keyAttributes, Tuple config, ResponseTimeThresholdConfig thresholds) {
		AnalysisStrategyFactory strategyFactory = newResponseTimeStrategyFactory()
				.withConfig(config)
				.withKeyAttributes(keyAttributes)
				.withResponseTimeThresholdConfig(thresholds)
				.build();
		return strategyFactory;
	}

	public static class Builder extends AbstractResponseTimeAnalyzerFactory.Builder {
    	private Builder () {}

		@Override
		protected AbstractResponseTimeAnalyzerFactory doBuild() {
			return new BinomialTestResponseTimeAnalyzerFactory(this);
		}
    	
    	
    }
}
