package com.compuware.apm.ruxit.synth.analyzer.model;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class TestAttribute {
	private static final Attribute<Long> ID = new Attribute<>(Long.class, "ID");
	private static final Attribute<String> NAME = new Attribute<>(String.class, "NAME");

	@Test
	public void testEquality() {
		final Attribute<Long> ID2 = new Attribute<>(Long.class, "ID");
		final Attribute<String> NAME2 = new Attribute<>(String.class, "NAME");
		
		assertThat(ID2, is(ID));
		assertThat(NAME2, is(NAME));
		assertThat(ID2.hashCode(), equalTo(ID.hashCode()));
		assertThat(NAME2.hashCode(), equalTo(NAME.hashCode()));
	}
	
	@Test
	public void testGetName() {
		assertThat(ID.getName(), equalTo("ID"));
		assertThat(NAME.getName(), equalTo("NAME"));
	}

	@Test
	public void testGetType() {
		assertEquals(ID.getType(), Long.class);
		assertEquals(NAME.getType(), String.class);
	}
}
