package com.compuware.apm.ruxit.synth.analyzer.input.parser;

import static com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl.newTuple;

import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.util.BuilderUtil;

public abstract class AbstractTupleParser implements TupleParser {

	private Attributes schema;
	private Tuple.Builder tupleBuilder;
	
	protected AbstractTupleParser(Builder builder) {
		this.schema = builder.schema;
		this.tupleBuilder = newTuple(this.schema);
	}
	
	protected Attributes getSchema () {
		return this.schema;
	}
	
	protected Tuple.Builder getTupleBuilder () {
		return this.tupleBuilder;
	}

	
    public static abstract class Builder {
    	private Attributes schema;
    	
    	protected Builder () {}
    	
    	public final Builder withSchema (Attributes schema) {
    		this.schema = schema;
    		return this;
    	}
    	
    	public final TupleParser build () {
    		BuilderUtil.validateNotNull("schema", schema);
    		return doBuild();
    	}
    	
    	protected abstract TupleParser doBuild ();
    	
    }

}
