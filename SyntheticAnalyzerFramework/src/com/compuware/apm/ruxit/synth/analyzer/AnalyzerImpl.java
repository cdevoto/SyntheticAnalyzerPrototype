package com.compuware.apm.ruxit.synth.analyzer;

import static com.compuware.apm.ruxit.synth.util.BuilderUtil.validateNotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.compuware.apm.ruxit.synth.analyzer.clock.ClockService;
import com.compuware.apm.ruxit.synth.analyzer.input.TupleSourceService;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.output.AnalyzerEvent;
import com.compuware.apm.ruxit.synth.analyzer.output.AnalyzerEvent.Type;
import com.compuware.apm.ruxit.synth.analyzer.output.EventListener;
import com.compuware.apm.ruxit.synth.analyzer.output.EventSourceSupport;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategy;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategyFactory;

public class AnalyzerImpl implements Analyzer {

	private Attributes keyAttributes;
	private ClockService clockService;
	private TupleSourceService tupleSourceService;
	private AnalysisStrategyFactory strategyFactory;
	private long strategyTimeOut;
	private Tuple config;
	
	private EventSourceSupport eventSupport = new EventSourceSupport();
	private Map<Tuple, AnalysisStrategy> strategies = new ConcurrentHashMap<>();

	public static Builder newAnalyzer () {
		return new Builder();
	}
	
	private AnalyzerImpl (Builder builder) {
		this.keyAttributes = builder.keyAttributes;
		this.clockService = builder.clockService;
		this.tupleSourceService = builder.tupleSourceService;
		this.strategyFactory = builder.strategyFactory;
		this.strategyTimeOut = builder.strategyTimeout;
		this.config = builder.config;
		
		this.clockService.addClockListener(this);
		this.tupleSourceService.addTupleListener(this);
	}
	
	@Override
	public void onEvent(AnalyzerEvent event) {
		this.eventSupport.notify(event);
	}

	@Override
	public void onTupleReceived(Tuple tuple) {
		// Notify the appropriate strategy; create the strategy if it doesn't exist
		if (tuple == null) {
			return;
		}
		Tuple key = tuple.project(this.keyAttributes);
		AnalysisStrategy strategy = this.strategies.get(key);
		if (strategy == null) {
			strategy = createStrategy(key);
		}
		strategy.onTupleReceived(tuple);
	}

	@Override
	public void onClockTick(long time) {
		// Age out old strategies
		for (AnalysisStrategy strategy : strategies.values()) {
			if (time - strategy.getTimeOfLastTuple() > this.strategyTimeOut) {
				removeStrategy(strategy);
			}
		}
	}

	@Override
	public void onStrategyStateChanged(AnalysisStrategy strategy) {
		if (strategy.getState() == AnalysisStrategy.State.IDLE) {
			removeStrategy(strategy);
		}
		
	}

	@Override
	public void addEventListener(EventListener listener, Type... types) {
		this.eventSupport.addEventListener(listener, types);
	}

	@Override
	public void addEventListener(EventListener listener) {
		this.eventSupport.addEventListener(listener);
	}

	@Override
	public void removeEventListener(EventListener listener, Type... types) {
		this.eventSupport.removeEventListener(listener, types);
	}

	@Override
	public void removeEventListener(EventListener listener) {
		this.eventSupport.removeEventListener(listener);
	}

	@Override
	public void start() {
		this.clockService.start();
		this.tupleSourceService.start();
	}

	@Override
	public void stop() {
		this.clockService.stop();
		this.tupleSourceService.stop();
	}

	@Override
	public ClockService getClockService() {
		return this.clockService;
	}

	@Override
	public TupleSourceService getTupleSourceService() {
		return this.tupleSourceService;
	}

	@Override
	public AnalysisStrategyFactory getAnalysisStrategyFactory() {
		return this.strategyFactory;
	}

	@Override
	public Attributes getKeyAttributes() {
		return keyAttributes;
	}

	@Override
	public long getStrategyTimeout() {
		return this.strategyTimeOut;
	}
	
	@Override
	public Tuple getConfig() {
		return this.config;
	}

	private AnalysisStrategy createStrategy(Tuple key) {
		AnalysisStrategy strategy;
		strategy = this.strategyFactory.newAnalysisStrategy(key);
		strategy.addEventListener(this);
		strategy.addStrategyListener(this);
		this.clockService.addClockListener(strategy);
		this.strategies.put(key, strategy);
		return strategy;
	}

	private void removeStrategy(AnalysisStrategy strategy) {
		strategy.removeEventListener(this);
		strategy.removeStrategyListener(this);
		clockService.removeClockListener(strategy);
		this.strategies.remove(strategy.getKey());
	}

	public static class Builder {
		private Attributes keyAttributes;
		private ClockService clockService;
		private TupleSourceService tupleSourceService;
		private AnalysisStrategyFactory strategyFactory;
		private Tuple config;
		private Long strategyTimeout;
		
		private Builder () {}
		
		public Builder withKeyAttributes (Attributes keyAttributes) {
			this.keyAttributes = keyAttributes;
			return this;
		}
		
		public Builder withClockService (ClockService clockService) {
			this.clockService = clockService;
			return this;
		}
		
		public Builder withTupleSourceService (TupleSourceService tupleSourceService) {
			this.tupleSourceService = tupleSourceService;
			return this;
		}
		
		public Builder withStrategyFactory (AnalysisStrategyFactory strategyFactory) {
			this.strategyFactory = strategyFactory;
			return this;
		}
		
		public Builder withStrategyTimeout (long strategyTimeout) {
			this.strategyTimeout = strategyTimeout;
			return this;
		}
		
		public Builder withConfig (Tuple config) {
			this.config = config;
			return this;
		}
		
		public AnalyzerImpl build () {
			validateNotNull("keyAttributes", keyAttributes);
			validateNotNull("clockService", clockService);
			validateNotNull("tupleSourceService", tupleSourceService);
			validateNotNull("strategyFactory", strategyFactory);
			validateNotNull("strategyTimeout", strategyTimeout);
			return new AnalyzerImpl(this);
		}
		
 	}
}
