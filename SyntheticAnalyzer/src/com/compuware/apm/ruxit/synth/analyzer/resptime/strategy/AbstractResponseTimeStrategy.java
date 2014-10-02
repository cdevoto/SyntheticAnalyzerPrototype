package com.compuware.apm.ruxit.synth.analyzer.resptime.strategy;

import static com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl.newTuple;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.PROPERTIES;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.ATTRIBUTES;

import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.output.AnalyzerEvent;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeThresholdConfig;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AbstractAnalysisStrategy;
import com.compuware.apm.ruxit.synth.util.BuilderUtil;

public abstract class AbstractResponseTimeStrategy extends AbstractAnalysisStrategy {

	public static Attribute<Long> EVENT_TIME = new Attribute<>(Long.class, "EVENT_TIME");
	public static Attribute<Tuple> EVENT_KEY = new Attribute<>(Tuple.class, "EVENT_KEY");
	public static Attribute<Tuple> EVENT_TUPLE = new Attribute<>(Tuple.class, "EVENT_TUPLE");
	
    public static Attributes EVENT_SOURCE_SCHEMA = Attributes.newAttributes()
    		.withAttribute(EVENT_TIME)
    		.withAttribute(EVENT_KEY)
    		.withAttribute(EVENT_TUPLE)
    		.build();
	
    protected Status status = Status.NORMAL;
    protected State state = State.ACTIVE;

    protected Attributes keyAttributes;
	protected Tuple key;
    protected Tuple config;
    protected ResponseTimeThresholdConfig thresholds;
    
    protected long timeOfLastTuple = 0;
    protected long timeOfLastTick = 0;

    protected AbstractResponseTimeStrategy (Builder builder) {
    	this.keyAttributes = builder.keyAttributes;
    	this.key = builder.key;
    	this.config = builder.config;
    	this.thresholds = builder.thresholds;
    }
    
	@Override
	public synchronized final void onTupleReceived(Tuple tuple) {
		if (!tuple.project(this.keyAttributes).equals(this.key)) {
			throw new IllegalArgumentException();
		}
		handleOnTupleReceived(tuple);
	}    
    
	@Override
	public synchronized final void onClockTick(long time) {
		handleOnClockTick(time);
	}

	@Override
	public final Tuple getKey() {
		return this.key;
	}

	@Override
	public synchronized final long getTimeOfLastTuple() {
		return this.timeOfLastTuple;
	}
	
	@Override
	public synchronized final long getTimeOfLastClockTick() {
		return this.timeOfLastTick;
	}
	
	@Override
	public synchronized final Status getStatus() {
		return this.status;
	}
	
	@Override
	public synchronized final State getState() {
		return this.state;
	}

	protected final void generateEvent (AnalyzerEvent.Type type, long eventTime, Tuple tuple) {
		Tuple source = newTuple(EVENT_SOURCE_SCHEMA)
				.withValue(EVENT_TIME, eventTime)
				.withValue(EVENT_KEY, this.key)
				.withValue(EVENT_TUPLE, tuple)
				.build();
		if (type == AnalyzerEvent.Type.ALERT) {
			this.status = Status.ALERT;
		} else if (type == AnalyzerEvent.Type.RETURN_TO_NORMAL ||
				type == AnalyzerEvent.Type.TIME_OUT) {
			this.status = Status.NORMAL;
		}
		AnalyzerEvent event = new AnalyzerEvent(type, source);
		notifyEventListeners(event);
	}

	protected abstract void handleOnTupleReceived(Tuple tuple);

	protected abstract void handleOnClockTick(long time);
	
	public static abstract class Builder {
		protected Attributes keyAttributes;
		protected Tuple key;
	    protected Tuple config;
	    protected ResponseTimeThresholdConfig thresholds;
	    
	    protected Builder (Attributes keyAttributes) {
	    	if (!ATTRIBUTES.containsAll(keyAttributes)) {
	    		throw new IllegalArgumentException("The specified key contains attributes which are not a part of the accepted schema for response time observations.");
	    	}
	    	this.keyAttributes = keyAttributes;
	    	
	    }
	    
	    public Builder withKeyAttributes (Attributes keyAttributes) {
	    	return this;
	    }
	    
	    public Builder withKey (Tuple key) {
	    	this.key = key;
	    	return this;
	    }
		
	    public Builder withConfig (Tuple config) {
	    	if (!config.getAttributes().containsAll(PROPERTIES)) {
	    		throw new IllegalArgumentException("The specified key contains attributes which are not a part of the accepted schema for response time observations.");
	    	}
	    	this.config = config;
	    	return this;
	    }
	    
	    public Builder withResponseTimeThresholdConfig (ResponseTimeThresholdConfig thresholds) {
	    	this.thresholds = thresholds;
	    	return this;
	    }
	    
	    public AbstractResponseTimeStrategy build () {
	    	BuilderUtil.validateNotNull("keyAttributes", key);
	    	BuilderUtil.validateNotNull("key", key);
	    	BuilderUtil.validateNotNull("config", config);
	    	BuilderUtil.validateNotNull("thresholds", thresholds);
			if (!keyAttributes.equals(key.getAttributes())) {
	    		throw new IllegalArgumentException("The specified key is either missing attributes or contains extra attributes which are not a part of the accepted schema for the key.");
			}
	    	return doBuild();
	    }
	    
	    protected abstract AbstractResponseTimeStrategy doBuild ();
	}
	
}
