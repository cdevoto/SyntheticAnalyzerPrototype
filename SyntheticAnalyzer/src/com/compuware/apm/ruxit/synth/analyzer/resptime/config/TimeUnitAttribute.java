package com.compuware.apm.ruxit.synth.analyzer.resptime.config;

import java.util.concurrent.TimeUnit;

import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;

public class TimeUnitAttribute extends Attribute<Long> {

	private TimeUnit timeUnit;
	
	public TimeUnitAttribute(String name, TimeUnit timeUnit) {
		super(Long.class, name);
		this.timeUnit = timeUnit;
	}

	public TimeUnitAttribute(String name, TimeUnit timeUnit, Long defaultValue) {
		super(Long.class, name, TimeUnit.MILLISECONDS.convert(defaultValue, timeUnit));
		this.timeUnit = timeUnit;
	}
	
	public TimeUnit getTimeUnit () {
		return this.timeUnit;
	}
	
	public long toMillis (long value) {
		return TimeUnit.MILLISECONDS.convert(value, this.timeUnit);
	}

}
