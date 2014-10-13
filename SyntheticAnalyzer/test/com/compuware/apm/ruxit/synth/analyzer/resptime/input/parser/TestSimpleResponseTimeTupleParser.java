package com.compuware.apm.ruxit.synth.analyzer.resptime.input.parser;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.input.parser.SimpleResponseTimeTupleParser.newResponseTimeTupleParser;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.ATTRIBUTES;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.RESPONSE_TIME;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.STEP_ID;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.TEST_DEF_ID;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.TEST_TIME;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.compuware.apm.ruxit.synth.analyzer.input.parser.TupleParser;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;


public class TestSimpleResponseTimeTupleParser {
	

	@Test
	public void test() {
		TupleParser parser = newResponseTimeTupleParser()
				.withSchema(ATTRIBUTES)
				.build();
		long now = System.currentTimeMillis();
		String tupleString = now + "|1|3|2.3";
		
		Tuple tuple = parser.parse(tupleString);
		assertThat(tuple.get(TEST_TIME), equalTo(now));
		assertThat(tuple.get(TEST_DEF_ID), equalTo("1"));
		assertThat(tuple.get(STEP_ID), equalTo("3"));
		assertThat(tuple.get(RESPONSE_TIME), equalTo(2.3));
		
	}

}
