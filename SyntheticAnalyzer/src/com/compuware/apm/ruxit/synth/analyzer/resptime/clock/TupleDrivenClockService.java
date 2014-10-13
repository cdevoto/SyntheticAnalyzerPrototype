package com.compuware.apm.ruxit.synth.analyzer.resptime.clock;

import java.util.concurrent.TimeUnit;

import com.compuware.apm.ruxit.synth.analyzer.clock.AbstractClockService;
import com.compuware.apm.ruxit.synth.analyzer.input.TupleListener;
import com.compuware.apm.ruxit.synth.analyzer.input.TupleSource;
import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.util.BuilderUtil;

public class TupleDrivenClockService extends AbstractClockService implements TupleListener {

	private Attribute<Long> timeAttribute;
	private TupleSource tupleSource;
	private long period;
	private TimeUnit timeUnit;
	private long interval;

	private boolean stopped = true;
	private Long lastTick;

	public static Builder newSimulatedClock () {
		return new Builder();
	}
	
	private TupleDrivenClockService (Builder builder) {
		if (builder.startTime != null) {
			this.lastTick = builder.startTime;
		}
		this.timeAttribute = builder.timeAttribute;
		this.tupleSource = builder.tupleSource;
		this.period = builder.period;
		this.timeUnit = builder.timeUnit;
		this.interval = TimeUnit.MILLISECONDS.convert(this.period, this.timeUnit);
	}

	@Override
	public void start() {
		this.stopped = false;
		this.tupleSource.addTupleListener(this);
		
	}

	@Override
	public void stop() {
		this.stopped = true;
		this.tupleSource.removeTupleListener(this);
	}


	@Override
	public void onTupleReceived(Tuple tuple) {
		Long time = tuple.get(this.timeAttribute);
		notify(time);
	}
	
	public void notify (long time) {
		if (!this.stopped) {
			if (this.lastTick == null) {
				this.lastTick = time;
				return;
			}
			for (long nextTick = this.lastTick + this.interval; nextTick <= time; nextTick += this.interval) {
				notifyClockListeners(nextTick);
				this.lastTick = nextTick;
			}
		}
	}
	
	public long getTime () {
		if (this.lastTick == null) {
			return 0;
		}
		return this.lastTick;
	}
	
	public static class Builder {
		private Long startTime;
		private Attribute<Long> timeAttribute;
		private TupleSource tupleSource;
		private Long period;
		private TimeUnit timeUnit;
		
		private Builder () {}
		
		public Builder withTimeAttribute(Attribute<Long> timeAttribute) {
			this.timeAttribute = timeAttribute;
			return this;
		}
		
		public Builder withTupleSource (TupleSource tupleSource) {
			this.tupleSource = tupleSource;
			return this;
		}
		
		public Builder withStartTime (long startTime) {
			this.startTime = startTime;
			return this;
		}
		
		public Builder withPeriod (long period) {
			this.period = period;
			return this;
		}
		
		public Builder withTimeUnit (TimeUnit timeUnit) {
			this.timeUnit = timeUnit;
			return this;
		}
		
		public TupleDrivenClockService build () {
			BuilderUtil.validateNotNull("timeAttribute", timeAttribute);
			BuilderUtil.validateNotNull("tupleSource", tupleSource);
			BuilderUtil.validateNotNull("period", period);
			BuilderUtil.validateNotNull("timeUnit", timeUnit);
			return new TupleDrivenClockService(this);
		}
	}
	
	
	

}
