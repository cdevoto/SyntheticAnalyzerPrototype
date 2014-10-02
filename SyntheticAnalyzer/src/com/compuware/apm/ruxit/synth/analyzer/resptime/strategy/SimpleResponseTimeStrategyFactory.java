package com.compuware.apm.ruxit.synth.analyzer.resptime.strategy;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.strategy.SimpleResponseTimeStrategy.newSimpleResponseTimeStrategy;

import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategy;

public class SimpleResponseTimeStrategyFactory extends AbstractResponseTimeStrategyFactory {

	public static Builder newResponseTimeStrategyFactory () {
		return new Builder();
	}
	
	private SimpleResponseTimeStrategyFactory (Builder builder) {
		super(builder);
	}

	@Override
	public AnalysisStrategy newAnalysisStrategy(Tuple key) {
		if (!keyAttributes.equals(key.getAttributes())) {
    		throw new IllegalArgumentException("The specified tuple does not match the key associated with this analysis strategy instance.");
		}
		AnalysisStrategy strategy = newSimpleResponseTimeStrategy(this.keyAttributes)
				.withKey(key)
				.withConfig(this.config)
				.withResponseTimeThresholdConfig(this.thresholds)
				.build();
		return strategy;
	}
	
	public static class Builder extends AbstractResponseTimeStrategyFactory.Builder {
		
		private Builder () {}

		@Override
		protected AbstractResponseTimeStrategyFactory doBuild() {
			return new SimpleResponseTimeStrategyFactory(this);
		}
		
    		
	}

}
