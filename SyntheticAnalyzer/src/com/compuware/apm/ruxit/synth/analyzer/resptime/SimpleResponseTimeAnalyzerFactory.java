package com.compuware.apm.ruxit.synth.analyzer.resptime;

import com.compuware.apm.ruxit.synth.analyzer.resptime.strategy.SimpleResponseTimeStrategyFactory;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategyFactory;

public class SimpleResponseTimeAnalyzerFactory extends AbstractResponseTimeAnalyzerFactory {

	public static Builder newResponseTimeAnalyzerFactory () {
		return new Builder ();
	}

	private SimpleResponseTimeAnalyzerFactory (Builder builder) {
		super(builder);
	}

	protected AnalysisStrategyFactory createAnalysisStrategyFactory() {
		AnalysisStrategyFactory strategyFactory = SimpleResponseTimeStrategyFactory.newResponseTimeStrategyFactory()
				.withConfig(this.config)
				.withKeyAttributes(this.keyAttributes)
				.withResponseTimeThresholdConfig(thresholds)
				.build();
		return strategyFactory;
	}

	public static class Builder extends AbstractResponseTimeAnalyzerFactory.Builder {
    	private Builder () {}

		@Override
		protected AbstractResponseTimeAnalyzerFactory doBuild() {
			return new SimpleResponseTimeAnalyzerFactory(this);
		}
    	
    	
    }
}
