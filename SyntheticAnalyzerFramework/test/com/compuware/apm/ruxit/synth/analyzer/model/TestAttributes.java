package com.compuware.apm.ruxit.synth.analyzer.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Test;

public class TestAttributes {
	private static final Attribute<Long> ID = new Attribute<>(Long.class, "ID");
	private static final Attribute<String> NAME = new Attribute<>(String.class, "NAME");
	private static final Attribute<String> CITY = new Attribute<>(String.class, "CITY");

	@Test
	public void testEquality() {
		final Attributes attributes1 = Attributes.newAttributes()
				.withAttribute(ID)
				.withAttribute(NAME)
				.build();
		final Attributes attributes2 = Attributes.newAttributes()
				.withAttribute(ID)
				.withAttribute(NAME)
				.build();

		assertThat(attributes1, equalTo(attributes2));
		assertThat(attributes1.hashCode(), equalTo(attributes2.hashCode()));
	}

	@Test
	public void testContains() {
		final Attributes attributes1 = Attributes.newAttributes()
				.withAttribute(ID)
				.withAttribute(NAME)
				.withAttribute(CITY)
				.build();
		final Attributes attributes2 = Attributes.newAttributes()
				.withAttribute(ID)
				.withAttribute(NAME)
				.build();

		assertThat(attributes1.containsAll(attributes2), equalTo(true));
		assertThat(attributes2.containsAll(attributes1), equalTo(false));
		assertThat(attributes2.contains(ID), equalTo(true));
		assertThat(attributes2.contains(CITY), equalTo(false));
	}

	@Test
	public void testSize() {
		final Attributes attributes1 = Attributes.newAttributes()
				.withAttribute(ID)
				.withAttribute(NAME)
				.withAttribute(CITY)
				.build();

		assertThat(attributes1.size(), equalTo(3));
	}

	@Test
	public void testIterator() {
		final Attributes attributes1 = Attributes.newAttributes()
				.withAttribute(ID)
				.withAttribute(NAME)
				.withAttribute(CITY)
				.build();

		Iterator<Attribute<?>> it = attributes1.iterator();
		assertThat(it.hasNext(), equalTo(true));
		assertThat(ID.equals(it.next()), equalTo(true));
		assertThat(it.hasNext(), equalTo(true));
		assertThat(NAME.equals(it.next()), equalTo(true));
		assertThat(it.hasNext(), equalTo(true));
		assertThat(CITY.equals(it.next()), equalTo(true));
		assertThat(it.hasNext(), equalTo(false));
	}
	
}
