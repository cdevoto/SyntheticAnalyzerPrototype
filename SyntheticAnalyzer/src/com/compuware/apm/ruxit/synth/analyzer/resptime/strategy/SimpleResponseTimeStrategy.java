package com.compuware.apm.ruxit.synth.analyzer.resptime.strategy;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.RESPONSE_TIME;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.TEST_TIME;

import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;
import com.compuware.apm.ruxit.synth.analyzer.output.AnalyzerEvent;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties;

public class SimpleResponseTimeStrategy extends AbstractResponseTimeStrategy {

    public static Builder newSimpleResponseTimeStrategy (Attributes keyAttributes) {
    	return new Builder(keyAttributes);
    }
    
    private SimpleResponseTimeStrategy (Builder builder) {
    	super(builder);
    }
    
	@Override
	protected final void handleOnTupleReceived(Tuple tuple) {
		this.timeOfLastTuple = tuple.get(TEST_TIME);
		double responseTime = tuple.get(RESPONSE_TIME);
		double threshold = thresholds.getThreshold(this.key);
		if (status == Status.NORMAL && responseTime >= threshold) {
			generateEvent(AnalyzerEvent.Type.ALERT, this.timeOfLastTuple, tuple);
		} else if (status == Status.ALERT && responseTime < threshold) {
			generateEvent(AnalyzerEvent.Type.RETURN_TO_NORMAL, this.timeOfLastTuple, tuple);
		}
	}

	@Override
	protected final void handleOnClockTick(long time) {
		this.timeOfLastTick = time;
		if (status == Status.ALERT && time - config.get(ResponseTimeConfigProperties.MAX_QUEUE_TIME_WINDOW) > this.timeOfLastTuple) {
			generateEvent(AnalyzerEvent.Type.RETURN_TO_NORMAL, this.timeOfLastTick, TupleImpl.NULL);
			this.state = State.IDLE;
			notifyStrategyListeners(this);
		}
	}

	public static class Builder extends AbstractResponseTimeStrategy.Builder {
		
		private Builder (Attributes keyAttributes) {
			super(keyAttributes);
		}

		@Override
		protected final AbstractResponseTimeStrategy doBuild() {
			return new SimpleResponseTimeStrategy(this);
		}
	}

}
