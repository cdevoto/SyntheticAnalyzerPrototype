package com.compuware.apm.ruxit.synth.analyzer.clock;

import com.compuware.apm.ruxit.synth.analyzer.clock.ClockListener;
import com.compuware.apm.ruxit.synth.analyzer.clock.ClockService;
import com.compuware.apm.ruxit.synth.analyzer.clock.ClockSupport;

public abstract class AbstractClockService implements ClockService {

	private ClockSupport clockSupport = new ClockSupport();

	protected AbstractClockService () {}
	
	@Override
	public final void addClockListener(ClockListener listener) {
		this.clockSupport.addClockListener(listener);
	}

	@Override
	public final void removeClockListener(ClockListener listener) {
		this.clockSupport.removeClockListener(listener);
	}

    protected final void notifyClockListeners (long time) {
    	this.clockSupport.notify(time);
    }
}
