package com.compuware.apm.ruxit.synth.util;

import com.compuware.apm.ruxit.synth.analyzer.input.parser.ParseException;
import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;

public class ParserUtil {

	public static int parseInt (Attribute<?> attribute, String value) {
		return parseInt(attribute, value, null);
	}

	public static int parseInt (Attribute<?> attribute, String value, String tuple) {
		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException ex) {
			throw generateParseException(attribute, value, tuple, "integer");
		}
	}

	public static long parseLong (Attribute<?> attribute, String value) {
		return parseLong(attribute, value, null);
	}

	public static long parseLong (Attribute<?> attribute, String value, String tuple) {
		try {
			return Long.parseLong(value.trim());
		} catch (NumberFormatException ex) {
			throw generateParseException(attribute, value, tuple, "long");
		}
	}

	public static double parseDouble (Attribute<?> attribute, String value) {
		return parseDouble(attribute, value, null);
	}
	
	public static double parseDouble (Attribute<?> attribute, String value, String tuple) {
		try {
			return Double.parseDouble(value.trim());
		} catch (NumberFormatException ex) {
			throw generateParseException(attribute, value, tuple, "double");
		}
	}

	public static boolean parseBoolean (Attribute<?> attribute, String value) {
		return parseBoolean(attribute, value, null);
	}

	public static boolean parseBoolean (Attribute<?> attribute, String value, String tuple) {
		value = value.toLowerCase();
		if ("true".equals(value) || "false".equals(value)) {
			return Boolean.parseBoolean(value);
		} else {
			throw generateParseException(attribute, value, tuple, "boolean");
		}
	}

	public static <T> T parse(Attribute<T> attribute, String value) {
        return parse(attribute, value, null);
	}	
	
	@SuppressWarnings("unchecked")
	public static <T> T parse(Attribute<T> attribute, String value, String tuple) {
		if (Integer.class.equals(attribute.getType())) {
			return (T) Integer.valueOf(parseInt(attribute, value, tuple));
		} else if (Long.class.equals(attribute.getType())) {
			return (T) Long.valueOf(parseLong(attribute, value, tuple));
		} else if (Double.class.equals(attribute.getType())) {
			return (T) Double.valueOf(parseDouble(attribute, value, tuple));
		} else if (Boolean.class.equals(attribute.getType())) {
			return (T) Boolean.valueOf(parseBoolean(attribute, value, tuple));
		} else if (String.class.equals(attribute.getType())) {
			return (T) value;
		} else {
			throw new IllegalArgumentException(String.format("Unsupported attribute type %s for attribute %s", attribute.getType(), attribute.getName()));
		}
	}
	
	private static ParseException generateParseException(Attribute<?> attribute,
			String value, String tuple, String expectedType) {
		String message = String.format("Expected a " + expectedType + " value for the %s attribute, but instead received '%s'.", attribute.getName(), value);
		if (tuple != null) {
			message += String.format("  The offending tuple is %s", tuple);
		}
		return new ParseException(message);
	}

	private ParserUtil() {}
	
}
