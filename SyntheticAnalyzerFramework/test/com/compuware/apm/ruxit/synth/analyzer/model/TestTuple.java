package com.compuware.apm.ruxit.synth.analyzer.model;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl.newTuple;

import org.junit.Test;

public class TestTuple {
	private static final Attribute<Long> ID = new Attribute<>(Long.class, "ID");
	private static final Attribute<String> NAME = new Attribute<>(String.class, "NAME");
	private static final Attribute<String> CITY = new Attribute<>(String.class, "CITY");
	
	private static final Attributes schema = Attributes.newAttributes()
			.withAttribute(ID)
			.withAttribute(NAME)
			.build();

	private static final Attributes altSchema = Attributes.newAttributes()
			.withAttribute(ID)
			.withAttribute(NAME)
			.withAttribute(CITY)
			.build();

	@Test
	public void testGetValue() {
		Tuple tuple = newTuple(schema)
				.withValue(ID, 1L)
				.withValue(NAME, "one")
				.build();
		long id = tuple.get(ID);
		String name = tuple.get(NAME);
		
		assertThat(id, equalTo(1L));
		assertThat(name, equalTo("one"));
	}
	
	@Test
	public void testToString() {
		Tuple tuple = newTuple(schema)
				.withValue(ID, 1L)
				.withValue(NAME, "one")
				.build();
		assertThat(tuple.toString(), equalTo("Tuple [ID=1, NAME=one]"));
	}

	@Test
	public void testEquality() {
		Tuple tuple1 = newTuple(schema)
				.withValue(ID, 1L)
				.withValue(NAME, "one")
				.build();

		Tuple tuple2 = newTuple(schema)
				.withValue(ID, 1L)
				.withValue(NAME, "one")
				.build();
		
		assertThat(tuple1, equalTo(tuple2));
		assertThat(tuple1.hashCode(), equalTo(tuple2.hashCode()));
	}
	
	@Test
	public void testGetAttributes() {
		Tuple tuple1 = newTuple(schema)
				.withValue(ID, 1L)
				.withValue(NAME, "one")
				.build();

		assertThat(tuple1.getAttributes(), equalTo(schema));
	}

	@Test
	public void testProject() {
		Tuple tuple = newTuple(altSchema)
				.withValue(ID, 1L)
				.withValue(NAME, "one")
				.withValue(CITY, "Detroit")
				.build();

		Attributes schema1 = Attributes.newAttributes()
				.withAttribute(ID)
				.build();
		Tuple expected1 = newTuple(schema1)
				.withValue(ID, 1L)
				.build();
		Tuple actual1 = tuple.project(schema1);

		assertThat(actual1, equalTo(expected1));
		
		Attributes schema2 = Attributes.newAttributes()
				.withAttribute(NAME)
				.withAttribute(CITY)
				.build();
		Tuple expected2 = newTuple(schema2)
				.withValue(NAME, "one")
				.withValue(CITY, "Detroit")
				.build();
		Tuple actual2 = tuple.project(schema2);

		assertThat(actual2, equalTo(expected2));
	}
	
	@Test
	public void testSchemaValidation() {
		try {
		   newTuple(schema)
				.withValue(ID, 1L)
				.withValue(NAME, "one")
				.withValue(CITY, "Detroit")
				.build();
		   fail("Expected an IllegalArgumentException because the schema does not support the CITY attribute.");
		} catch (IllegalArgumentException ex) {}
		
		try {
		   newTuple(schema)
				.withValue(ID, 1L)
				.build();
		   fail("Expected an IllegalStateException because no value was specified for the NAME attribute.");
		} catch (IllegalStateException ex) {}
	}
}
