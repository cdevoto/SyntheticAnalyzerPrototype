package com.compuware.apm.ruxit.synth.analyzer.resptime.input.parser;

import static com.compuware.apm.ruxit.synth.analyzer.model.Attributes.newAttributes;
import static com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl.newTuple;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.input.parser.SimpleResponseTimeThresholdParser.newSimpleResponseTimeThresholdParser;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.STEP_ID;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.TEST_DEF_ID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeThresholdConfig;
import com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes;

public class TestSimpleResponseTimeThresholdParser {

	@Test
	public void test() throws IOException {
		SimpleResponseTimeThresholdParser parser = newSimpleResponseTimeThresholdParser()
				.build();
		Properties props = new Properties();
		try (InputStream in = TestSimpleResponseTimeThresholdParser.class.getResourceAsStream("thresholds.properties")) {
			props.load(in);
		}
		ResponseTimeThresholdConfig config = parser.parse(props);
		
		Attributes keyAttributes = newAttributes()
				.withAttribute(TEST_DEF_ID)
				.withAttribute(STEP_ID)
				.build();
		
		Tuple key = newTuple(keyAttributes)
				.withValue(TEST_DEF_ID, "1")
				.withValue(STEP_ID, "1")
				.build();
		
		assertThat(config.getThreshold(key), equalTo(1.0));

		key = newTuple(keyAttributes)
				.withValue(TEST_DEF_ID, "1")
				.withValue(STEP_ID, "2")
				.build();
		
		assertThat(config.getThreshold(key), equalTo(1.0));
	
		key = newTuple(keyAttributes)
				.withValue(TEST_DEF_ID, "2")
				.withValue(STEP_ID, "1")
				.build();
		
		assertThat(config.getThreshold(key), equalTo(1.0));
	
		key = newTuple(keyAttributes)
				.withValue(TEST_DEF_ID, "2")
				.withValue(STEP_ID, "2")
				.build();
		
		assertThat(config.getThreshold(key), equalTo(1.0));
	
		key = newTuple(keyAttributes)
				.withValue(TEST_DEF_ID, "3")
				.withValue(STEP_ID, "1")
				.build();
		
		assertThat(config.getThreshold(key), equalTo(1.0));
	
	}

}
