package com.compuware.apm.ruxit.synth.analyzer.resptime.util;

import java.util.List;

import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;

public class SimpleParserUtil {
	
	public static String toString (Tuple tuple) {
		if (tuple == null || tuple == TupleImpl.NULL) {
			return null;
		}
		StringBuilder buf = new StringBuilder();
		boolean firstAttribute = true;
		for (Attribute<?> attribute : tuple.getAttributes()) {
			if (firstAttribute) {
				firstAttribute = false;
			} else {
				buf.append("|");
			}
			Object value = tuple.get(attribute);
			if (value instanceof Double) {
				buf.append(String.format("%,.4f", (Double) value));
			} else {
				buf.append(value);
			}
		}
		return buf.toString();
	}

	public static String toString(List<Tuple> tuples) {
		StringBuilder buf = new StringBuilder();
		for (Tuple tuple : tuples) {
			buf.append(SimpleParserUtil.toString(tuple));
			buf.append("\n");
		}
		String inputString = buf.toString();
		return inputString;
	}
	
	private SimpleParserUtil () {}
}
