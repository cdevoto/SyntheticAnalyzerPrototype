package com.compuware.apm.ruxit.synth.util;

public class BuilderUtil {

	public static void validateNotNull (String field, Object value) {
		if (value == null) {
			throw new IllegalStateException(String.format("Expected a value for the '%s' field.", field));
		}
	}
	
	private BuilderUtil() {}

}
