package com.compuware.apm.ruxit.synth.analyzer.resptime.input;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.compuware.apm.ruxit.synth.analyzer.input.TupleListener;
import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;

public class TestTupleSourceInputService {

	@Test
	public void test() {
		final TupleSourceInputService tupleSource = new TupleSourceInputService();
		final Set<Tuple> actual = new LinkedHashSet<>();
		
		tupleSource.addTupleListener(new TupleListener () {

			@Override
			public void onTupleReceived(Tuple tuple) {
				actual.add(tuple);
			}
			
		});
		
		final Attribute<Long> ID = new Attribute<>(Long.class, "ID");  
		final Attribute<String> NAME = new Attribute<>(String.class, "NAME");
		final Attributes schema = Attributes.newAttributes()
				.withAttribute(ID)
				.withAttribute(NAME)
				.build();
		
		final Tuple.Builder tupleBuilder = TupleImpl.newTuple(schema);
		Set<Tuple> expected = new LinkedHashSet<>();
		
		tupleSource.start();
		
		Tuple tuple = tupleBuilder
				.withValue(ID, 1L)
				.withValue(NAME, "one")
				.build();
		expected.add(tuple);
		tupleSource.notify(tuple);
		tupleBuilder.clearValues();
				
		tuple = tupleBuilder
				.withValue(ID, 2L)
				.withValue(NAME, "two")
				.build();
		expected.add(tuple);
		tupleSource.notify(tuple);
		tupleBuilder.clearValues();

		tuple = tupleBuilder
				.withValue(ID, 3L)
				.withValue(NAME, "three")
				.build();
		expected.add(tuple);
		tupleSource.notify(tuple);
		tupleBuilder.clearValues();
		
		tupleSource.stop();
		
		assertThat(actual, is(expected));
		
	}

}
