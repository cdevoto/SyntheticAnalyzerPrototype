package com.compuware.apm.ruxit.synth.analyzer.resptime.config;

import java.util.concurrent.TimeUnit;

import com.compuware.apm.ruxit.synth.analyzer.model.AbstractTuple;
import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;

public class ResponseTimeConfigTuple extends AbstractTuple {
	
	public static Builder newResponseTimeConfigTuple () {
		return new Builder();
	}

	public ResponseTimeConfigTuple(Builder builder) {
		super(builder);
	}

	@Override
	protected Tuple.Builder newTupleBuilder(
			Attributes attributes) {
		return newResponseTimeConfigTuple();
	}
	
	public static class Builder extends AbstractTuple.Builder {

		private Builder () {
			super(ResponseTimeConfigProperties.PROPERTIES);
		}
		
		@Override
		public <T> Builder withValue(Attribute<T> attribute, T value) {
			if (attribute instanceof TimeUnitAttribute) {
				TimeUnit timeUnit = ((TimeUnitAttribute) attribute).getTimeUnit();
				Long convertedValue = TimeUnit.MILLISECONDS.convert((Long) value, timeUnit);
				super.withUncheckedValue(attribute, convertedValue);
				return this;
			}
			super.withValue(attribute, value);
			return this;
		}
		
		@Override
		public Builder withUncheckedValue(@SuppressWarnings("rawtypes") Attribute attribute, Object value) {
			if (attribute instanceof TimeUnitAttribute) {
				TimeUnit timeUnit = ((TimeUnitAttribute) attribute).getTimeUnit();
				Long convertedValue = TimeUnit.MILLISECONDS.convert((Long) value, timeUnit);
				super.withUncheckedValue(attribute, convertedValue);
				return this;
			}
			super.withUncheckedValue(attribute, value);
			return this;
		}

		@Override
		protected Tuple doBuild() {
			return new ResponseTimeConfigTuple(this);
		}

	}
}
