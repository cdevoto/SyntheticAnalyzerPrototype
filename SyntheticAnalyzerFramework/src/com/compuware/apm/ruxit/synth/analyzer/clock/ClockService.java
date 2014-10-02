package com.compuware.apm.ruxit.synth.analyzer.clock;

import com.compuware.apm.ruxit.synth.analyzer.Service;

public interface ClockService extends Clock, Service {
	
	public long getTime ();
	public void notify(long time);

}
