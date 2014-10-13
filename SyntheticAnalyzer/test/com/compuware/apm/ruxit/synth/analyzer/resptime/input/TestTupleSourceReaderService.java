package com.compuware.apm.ruxit.synth.analyzer.resptime.input;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.input.TupleSourceReaderService.newTupleSourceReaderService;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.input.parser.SimpleResponseTimeTupleParser.newResponseTimeTupleParser;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.ATTRIBUTES;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.RESPONSE_TIME;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.STEP_ID;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.TEST_DEF_ID;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.TEST_TIME;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.compuware.apm.ruxit.synth.analyzer.input.TupleListener;
import com.compuware.apm.ruxit.synth.analyzer.input.parser.TupleParser;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;

public class TestTupleSourceReaderService {

	@Test
	public void test() {
		final TupleParser parser =  newResponseTimeTupleParser()
				.withSchema(ATTRIBUTES)
				.build();
		final InputSource inputSource = new ClassPathInputSource(getClass().getPackage(), "test-tuples-1.txt");
		final TupleSourceReaderService tupleSource = newTupleSourceReaderService()
				.withInputSource(inputSource)
				.withTupleParser(parser)
				.build();
				
		final Set<Tuple> actual = new LinkedHashSet<>();
		
		tupleSource.addTupleListener(new TupleListener () {

			@Override
			public void onTupleReceived(Tuple tuple) {
				actual.add(tuple);
			}
			
		});
		
		final Tuple.Builder tupleBuilder = TupleImpl.newTuple(ATTRIBUTES);
		Set<Tuple> expected = new LinkedHashSet<>();
		
		Tuple tuple = tupleBuilder
				.withValue(TEST_TIME, 5L)
				.withValue(TEST_DEF_ID, "100")
				.withValue(STEP_ID, "1")
				.withValue(RESPONSE_TIME, 0.5)
				.build();
		expected.add(tuple);
		tupleBuilder.clearValues();
				
		tuple = tupleBuilder
				.withValue(TEST_TIME, 10L)
				.withValue(TEST_DEF_ID, "200")
				.withValue(STEP_ID, "2")
				.withValue(RESPONSE_TIME, 0.8)
				.build();
		expected.add(tuple);
		tupleBuilder.clearValues();

		tuple = tupleBuilder
				.withValue(TEST_TIME, 15L)
				.withValue(TEST_DEF_ID, "300")
				.withValue(STEP_ID, "3")
				.withValue(RESPONSE_TIME, 2.8)
				.build();
		expected.add(tuple);
		tupleBuilder.clearValues();
		
		tupleSource.start();
		tupleSource.stop();
		
		assertThat(actual, is(expected));
	}

}
