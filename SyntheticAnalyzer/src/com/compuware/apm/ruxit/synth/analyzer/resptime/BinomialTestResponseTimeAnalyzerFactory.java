package com.compuware.apm.ruxit.synth.analyzer.resptime;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.strategy.BinomialTestResponseTimeStrategyFactory.*;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategyFactory;

public class BinomialTestResponseTimeAnalyzerFactory extends AbstractResponseTimeAnalyzerFactory {

	public static Builder newResponseTimeAnalyzerFactory () {
		return new Builder ();
	}

	private BinomialTestResponseTimeAnalyzerFactory (Builder builder) {
		super(builder);
	}

	protected AnalysisStrategyFactory createAnalysisStrategyFactory() {
		AnalysisStrategyFactory strategyFactory = newResponseTimeStrategyFactory()
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
			return new BinomialTestResponseTimeAnalyzerFactory(this);
		}
    	
    	
    }
}
