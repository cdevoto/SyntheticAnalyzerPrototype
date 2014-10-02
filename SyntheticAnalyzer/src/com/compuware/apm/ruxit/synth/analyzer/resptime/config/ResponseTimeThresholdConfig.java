package com.compuware.apm.ruxit.synth.analyzer.resptime.config;

import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;

public interface ResponseTimeThresholdConfig {
	public double getThreshold (Tuple key);
}
