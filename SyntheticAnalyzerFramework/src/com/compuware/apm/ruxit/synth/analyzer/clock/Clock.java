package com.compuware.apm.ruxit.synth.analyzer.clock;


public interface Clock {
	
	public void addClockListener (ClockListener listener);
	public void removeClockListener (ClockListener listener);

}
