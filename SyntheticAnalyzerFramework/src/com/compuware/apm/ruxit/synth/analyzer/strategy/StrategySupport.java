package com.compuware.apm.ruxit.synth.analyzer.strategy;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class StrategySupport implements StrategySource {

	private Set<StrategyListener> listeners = new CopyOnWriteArraySet<>();
	
	public StrategySupport() {}
	
	@Override
	public void addStrategyListener (StrategyListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		this.listeners.add(listener);
	}
	
	@Override
	public void removeStrategyListener (StrategyListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		this.listeners.remove(listener);
	}
	
	public void notify (AnalysisStrategy strategy) {
		if (strategy == null) {
			throw new NullPointerException();
		}
		for (StrategyListener listener : listeners) {
			listener.onStrategyStateChanged(strategy);
		}
	}

	@Override
	public String toString() {
		return "StrategySourceSupport [listeners=" + listeners + "]";
	}
}
