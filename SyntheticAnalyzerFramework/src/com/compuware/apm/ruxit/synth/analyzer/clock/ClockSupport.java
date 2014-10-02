package com.compuware.apm.ruxit.synth.analyzer.clock;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ClockSupport implements Clock {

	private Set<ClockListener> listeners = new CopyOnWriteArraySet<>();
	
	public ClockSupport() {}
	
	@Override
	public void addClockListener (ClockListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		this.listeners.add(listener);
	}
	
	@Override
	public void removeClockListener (ClockListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		this.listeners.remove(listener);
	}
	
	public void notify (long time) {
		for (ClockListener listener : listeners) {
			listener.onClockTick(time);
		}
	}

	@Override
	public String toString() {
		return "ClockSupport [listeners=" + listeners + "]";
	}

}
