package com.compuware.apm.ruxit.synth.analyzer.resptime.input.parser;

import com.compuware.apm.ruxit.synth.analyzer.input.parser.AbstractTupleParser;
import com.compuware.apm.ruxit.synth.analyzer.input.parser.ParseException;
import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.util.ParserUtil;

public class SimpleResponseTimeTupleParser extends AbstractTupleParser {

	
	public static Builder newResponseTimeTupleParser () {
		return new Builder();
	}
	
	private SimpleResponseTimeTupleParser(Builder builder) {
		super(builder);
	}

	@Override
	public Tuple parse(String line) {
		String [] values = line.split("\\|");
		if (values.length != getSchema().size()) {
			throw new ParseException(String.format("Expected %d attributes, but found %d instead.  The offending tuple is %s.", getSchema().size(), values.length, line));
		}
		getTupleBuilder().clearValues();
		int idx = 0;
		for (Attribute<?> attribute : getSchema()) {
			getTupleBuilder().withUncheckedValue(attribute, ParserUtil.parse(attribute, values[idx++].trim(), line));
		}
        Tuple tuple = getTupleBuilder().build();
		return tuple;
	}
	
    public static class Builder extends AbstractTupleParser.Builder {

    	private Builder () {}
    	
    	public SimpleResponseTimeTupleParser doBuild () {
    		return new SimpleResponseTimeTupleParser(this);
    	}
    	
    }

}
