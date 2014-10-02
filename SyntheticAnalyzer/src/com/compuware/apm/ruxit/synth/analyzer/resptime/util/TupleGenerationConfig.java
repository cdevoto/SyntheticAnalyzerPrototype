package com.compuware.apm.ruxit.synth.analyzer.resptime.util;

import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.util.BuilderUtil;

public class TupleGenerationConfig {
	private Tuple key;
	private long startTime;
	private double minResponseTime;
	private double responseTimeIncrement;
	private double maxResponseTime;
	private long interval;
	private int numCycles;
	private int taperTuples;
	
	public static Builder newTupleGenerationConfig () {
		return new Builder();
	}
	
	private TupleGenerationConfig (Builder builder) {
	    this.key = builder.key;
	    this.startTime = builder.startTime;
	    this.minResponseTime = builder.minResponseTime;
	    this.responseTimeIncrement = builder.responseTimeIncrement;
	    this.maxResponseTime = builder.maxResponseTime;
	    this.interval = builder.interval;
	    this.numCycles = builder.numCycles;
	    this.taperTuples = builder.taperTuples;
	}
	
	public Tuple getKey() {
		return key;
	}

	public long getStartTime() {
		return startTime;
	}

	public double getMinResponseTime() {
		return minResponseTime;
	}

	public double getResponseTimeIncrement() {
		return responseTimeIncrement;
	}

	public double getMaxResponseTime() {
		return maxResponseTime;
	}

	public long getInterval() {
		return interval;
	}

	public int getNumCycles() {
		return numCycles;
	}

	public int getTaperTuples() {
		return taperTuples;
	}

	public static class Builder {
		private Tuple key;
		private Long startTime;
		private Double minResponseTime;
		private Double responseTimeIncrement;
		private Double maxResponseTime;
		private Long interval;
		private int numCycles = 1;
		private int taperTuples = 0;
		
		private Builder () {}

		public Builder withKey(Tuple key) {
			this.key = key;
			return this;
		}

		public Builder withStartTime(long startTime) {
			this.startTime = startTime;
			return this;
		}

		public Builder withMinResponseTime(double minResponseTime) {
			this.minResponseTime = minResponseTime;
			return this;
		}

		public Builder withResponseTimeIncrement(double ResponseTimeIncrement) {
			this.responseTimeIncrement = ResponseTimeIncrement;
			return this;
		}

		public Builder withMaxResponseTime(double maxResponseTime) {
			this.maxResponseTime = maxResponseTime;
			return this;
		}

		public Builder withInterval(long interval) {
			this.interval = interval;
			return this;
		}

		public Builder withNumCycles(int numCycles) {
			this.numCycles = numCycles;
			return this;
		}

		public Builder withTaperTuples(int taperTuples) {
			this.taperTuples = taperTuples;
			return this;
		}
		
		public TupleGenerationConfig build() {
			BuilderUtil.validateNotNull("key", key);
			BuilderUtil.validateNotNull("startTime", startTime);
			BuilderUtil.validateNotNull("minResponseTime", minResponseTime);
			BuilderUtil.validateNotNull("responseTimeIncrement", responseTimeIncrement);
			BuilderUtil.validateNotNull("maxResponseTime", maxResponseTime);
			BuilderUtil.validateNotNull("interval", interval);
			validateGreaterThanEqual("startTime", startTime, 0);
			validateGreaterThanEqual("responseTimeIncrement", responseTimeIncrement, 0);
			validateGreaterThanEqual("taperTuples", taperTuples, 0);
			validateGreaterThan("minResponseTime", minResponseTime, 0);
			validateGreaterThan("minResponseTime", maxResponseTime, 0);
			validateGreaterThan("interval", interval, 0);
			validateGreaterThan("numCycles", numCycles, 0);
			validateGreaterThanEqual("maxResponseTime", maxResponseTime, "minResponseTime", minResponseTime);
			return new TupleGenerationConfig(this);
		}

		private void validateGreaterThanEqual(String name, long value, long minValue) {
			if (value < minValue) {
				throw new IllegalStateException(String.format("The value of the %s field must be greater than or equal to %d", name, minValue));
			}
		}
		
		private void validateGreaterThanEqual(String name, double value, double minValue) {
			if (value < minValue) {
				throw new IllegalStateException(String.format("The value of the %s field must be greater than or equal to %,.2f", name, minValue));
			}
		}

		private void validateGreaterThanEqual(String name1, double value1, String name2, double value2) {
			if (value1 < value2) {
				throw new IllegalStateException(String.format("The value of the %s field must be greater than or equal to the value of the %s field", name1, name2));
			}
		}

		private void validateGreaterThan(String name, long value, long minValue) {
			if (value <= minValue) {
				throw new IllegalStateException(String.format("The value of the %s field must be greater than %d", name, minValue));
			}
		}

		private void validateGreaterThan(String name, double value, double minValue) {
			if (value <= minValue) {
				throw new IllegalStateException(String.format("The value of the %s field must be greater than %,.1f", name, minValue));
			}
		}
	}
	
}