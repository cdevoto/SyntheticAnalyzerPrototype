package com.compuware.apm.ruxit.synth.analyzer.resptime.config;

import java.util.HashMap;
import java.util.Map;

import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;

public class SimpleResponseTimeThresholdConfig implements ResponseTimeThresholdConfig {
	private Map<Tuple, Double> thresholds = new HashMap<>();
	
	public static Builder newSimpleResponseTimeThresholdConfig (Attributes keyAttributes) {
		return new Builder(keyAttributes);
	}
	
	private SimpleResponseTimeThresholdConfig (Builder builder) {
		this.thresholds.putAll(builder.thresholds);
	}
	
	public double getThreshold (Tuple key) {
		Double threshold = this.thresholds.get(key);
		if (threshold == null) {
			// We simulate defaulting to the Apdex value here.
			threshold = 1.0;
		}
		return threshold;
	}

	
	@Override
	public String toString() {
		return "SimpleResponseTimeThresholdConfig [thresholds=" + thresholds
				+ "]";
	}

	public static class Builder {
		private Attributes keyAttributes;
		private Map<Tuple, Double> thresholds = new HashMap<>();
		
		private Builder (Attributes keyAttributes) {
			if (keyAttributes == null) {
				throw new NullPointerException();
			}
			this.keyAttributes = keyAttributes;
		}
		
		public Builder withThreshold (Tuple key, Double value) {
			if (!keyAttributes.equals(key.getAttributes())) {
	    		throw new IllegalArgumentException("The specified key is either missing attributes or contains extra attributes which are not a part of the accepted schema for the key.");
			}
			this.thresholds.put(key,  value);
			return this;
		}
		
		public SimpleResponseTimeThresholdConfig build () {
			return new SimpleResponseTimeThresholdConfig(this);
		}
	}

}
