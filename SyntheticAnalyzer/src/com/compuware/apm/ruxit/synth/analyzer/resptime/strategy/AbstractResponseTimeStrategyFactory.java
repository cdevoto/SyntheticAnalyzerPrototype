package com.compuware.apm.ruxit.synth.analyzer.resptime.strategy;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.PROPERTIES;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.ATTRIBUTES;

import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeThresholdConfig;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategyFactory;
import com.compuware.apm.ruxit.synth.util.BuilderUtil;

public abstract class AbstractResponseTimeStrategyFactory implements AnalysisStrategyFactory {
	protected Tuple config;
	protected Attributes keyAttributes;
	protected ResponseTimeThresholdConfig thresholds;

	protected AbstractResponseTimeStrategyFactory (Builder builder) {
		this.config = builder.config;
		this.keyAttributes = builder.keyAttributes;
		this.thresholds = builder.thresholds;
	}

	public final Tuple getConfig () {
		return this.config;
	}
	
	public static abstract class Builder {
		protected Tuple config;
		protected Attributes keyAttributes;
		protected ResponseTimeThresholdConfig thresholds;
		
		protected Builder () {}
		
		public final Builder withConfig (Tuple config) {
	    	if (!config.getAttributes().containsAll(PROPERTIES)) {
	    		throw new IllegalArgumentException("The specified key contains attributes which are not a part of the accepted schema for response time observations.");
	    	}
	    	this.config = config;
	    	return this;
		}
		
		public final Builder withKeyAttributes (Attributes keyAttributes) {
	    	if (!ATTRIBUTES.containsAll(keyAttributes)) {
	    		throw new IllegalArgumentException("The specified key contains attributes which are not a part of the accepted schema for response time observations.");
	    	}
			this.keyAttributes = keyAttributes;
			return this;
		}
		
	    public final Builder withResponseTimeThresholdConfig (ResponseTimeThresholdConfig thresholds) {
	    	this.thresholds = thresholds;
	    	return this;
	    }

	    public AbstractResponseTimeStrategyFactory build () {
			BuilderUtil.validateNotNull("config", config);
			BuilderUtil.validateNotNull("keyAttributes", keyAttributes);
			BuilderUtil.validateNotNull("thresholds", thresholds);
			return doBuild();
		}
		
	    protected abstract AbstractResponseTimeStrategyFactory doBuild ();
	}

}
