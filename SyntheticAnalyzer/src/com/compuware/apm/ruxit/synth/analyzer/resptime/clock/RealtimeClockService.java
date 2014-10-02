package com.compuware.apm.ruxit.synth.analyzer.resptime.clock;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.compuware.apm.ruxit.synth.analyzer.clock.AbstractClockService;
import com.compuware.apm.ruxit.synth.util.BuilderUtil;

public class RealtimeClockService extends AbstractClockService implements Runnable {

	private long initialDelay;
	private long period;
	private TimeUnit timeUnit;
	
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static Builder newRealtimeClock () {
		return new Builder();
	}
	
	private RealtimeClockService (Builder builder) {
		this.initialDelay = builder.initialDelay;
		this.period = builder.period;
		this.timeUnit = builder.timeUnit;
	}
	
	@Override
	public void start () {
		scheduler.scheduleAtFixedRate(this, this.initialDelay, this.period, this.timeUnit);
	}
	
	@Override
	public void stop () {
		scheduler.shutdown();
	}
	
	@Override
	public long getTime() {
		return System.currentTimeMillis();
	}
	
	@Override
	public void notify(long time) {
		notifyClockListeners(time);
	}

	@Override
	public void run() {
		long time = System.currentTimeMillis();
		notifyClockListeners(time);
	}
	
	public static class Builder {
		private long initialDelay = 0;
		private Long period;
		private TimeUnit timeUnit;
		
		private Builder () {}
		
		public Builder withInitialDelay (long initialDelay) {
			this.initialDelay = initialDelay;
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
		
		public RealtimeClockService build () {
			BuilderUtil.validateNotNull("period", period);
			BuilderUtil.validateNotNull("timeUnit", timeUnit);
			return new RealtimeClockService(this);
		}
	}

}
