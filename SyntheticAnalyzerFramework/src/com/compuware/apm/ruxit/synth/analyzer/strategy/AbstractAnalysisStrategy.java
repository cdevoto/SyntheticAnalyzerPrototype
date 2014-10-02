package com.compuware.apm.ruxit.synth.analyzer.strategy;

import com.compuware.apm.ruxit.synth.analyzer.output.AnalyzerEvent;
import com.compuware.apm.ruxit.synth.analyzer.output.AnalyzerEvent.Type;
import com.compuware.apm.ruxit.synth.analyzer.output.EventListener;
import com.compuware.apm.ruxit.synth.analyzer.output.EventSourceSupport;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategy;
import com.compuware.apm.ruxit.synth.analyzer.strategy.StrategyListener;
import com.compuware.apm.ruxit.synth.analyzer.strategy.StrategySupport;

public abstract class AbstractAnalysisStrategy implements AnalysisStrategy {

    private EventSourceSupport eventSupport = new EventSourceSupport();
    private StrategySupport strategySupport = new StrategySupport();

    protected AbstractAnalysisStrategy () {}
    
	@Override
	public final void addEventListener(EventListener listener, Type... types) {
		this.eventSupport.addEventListener(listener, types);
	}

	@Override
	public final void addEventListener(EventListener listener) {
		this.eventSupport.addEventListener(listener);
	}

	@Override
	public final void removeEventListener(EventListener listener, Type... types) {
		this.eventSupport.removeEventListener(listener, types);
	}

	@Override
	public final void removeEventListener(EventListener listener) {
		this.eventSupport.removeEventListener(listener);
	}
	
	@Override
	public final void addStrategyListener(StrategyListener listener) {
		this.strategySupport.addStrategyListener(listener);
	}
	
	@Override
	public final void removeStrategyListener(StrategyListener listener) {
		this.strategySupport.removeStrategyListener(listener);
	}
	
	protected void notifyEventListeners (AnalyzerEvent event) {
		this.eventSupport.notify(event);
	}
	
	protected void notifyStrategyListeners (AnalysisStrategy strategy) {
		this.strategySupport.notify(this);
	}
	
}
