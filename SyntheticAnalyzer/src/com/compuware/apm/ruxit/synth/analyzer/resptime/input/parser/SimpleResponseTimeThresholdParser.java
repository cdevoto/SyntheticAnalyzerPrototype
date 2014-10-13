package com.compuware.apm.ruxit.synth.analyzer.resptime.input.parser;

import static com.compuware.apm.ruxit.synth.analyzer.model.Attributes.newAttributes;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.SimpleResponseTimeThresholdConfig.newSimpleResponseTimeThresholdConfig;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.STEP_ID;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.TEST_DEF_ID;

import java.util.Properties;

import com.compuware.apm.ruxit.synth.analyzer.input.parser.ParseException;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeThresholdConfig;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.SimpleResponseTimeThresholdConfig;

public class SimpleResponseTimeThresholdParser {
	
	public static Builder newSimpleResponseTimeThresholdParser () {
		return new Builder();
	}
	
	private SimpleResponseTimeThresholdParser (Builder builder) {}
	
	public ResponseTimeThresholdConfig parse (Properties props) {
		Attributes keyAttributes = newAttributes()
				.withAttribute(TEST_DEF_ID)
				.withAttribute(STEP_ID)
				.build();
		
		SimpleResponseTimeThresholdConfig.Builder builder = newSimpleResponseTimeThresholdConfig(keyAttributes); 
		for (Object keyObject : props.keySet()) {
			String keyString = (String) keyObject;
			String valueString = props.getProperty(keyString);
			Double value = null;
			try {
			    value = Double.parseDouble(valueString);
			} catch (NumberFormatException ex) {
				throw new ParseException(String.format("Expected a floating point value for the threshold corresponding to key %s.", keyString));
			}
			String [] keyFields = keyString.split("\\|");
			if (keyFields.length != 2) {
				throw new ParseException(String.format("Invalid key %s for configuration thresholds.", keyString));
			}
			Tuple key = TupleImpl.newTuple(keyAttributes)
					.withValue(TEST_DEF_ID, keyFields[0])
					.withValue(STEP_ID, keyFields[1])
					.build();
			builder.withThreshold(key, value);
		}
		return builder.build();
	}
	
	public static class Builder {
		private Builder () {}
		
		public SimpleResponseTimeThresholdParser build () {
			return new SimpleResponseTimeThresholdParser(this);
		}
 	}

}
