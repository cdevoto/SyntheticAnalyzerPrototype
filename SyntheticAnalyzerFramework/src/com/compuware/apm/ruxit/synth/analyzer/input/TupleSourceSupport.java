package com.compuware.apm.ruxit.synth.analyzer.input;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;

public class TupleSourceSupport implements TupleSource {

	private Set<TupleListener> listeners = new CopyOnWriteArraySet<>();
	
	public TupleSourceSupport() {}
	
	@Override
	public void addTupleListener (TupleListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		this.listeners.add(listener);
	}
	
	@Override
	public void removeTupleListener (TupleListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		this.listeners.remove(listener);
	}
	
	public void notify (Tuple tuple) {
		if (tuple == null) {
			throw new NullPointerException();
		}
		for (TupleListener listener : listeners) {
			listener.onTupleReceived(tuple);
		}
	}

	@Override
	public String toString() {
		return "TupleSourceSupport [listeners=" + listeners + "]";
	}
}
