package com.compuware.apm.ruxit.synth.analyzer.input;

import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;

public abstract class AbstractTupleSourceService implements TupleSourceService {
	
	private TupleSourceSupport tupleSupport = new TupleSourceSupport();
	
	protected AbstractTupleSourceService() {}

	@Override
	public final void addTupleListener(TupleListener listener) {
		this.tupleSupport.addTupleListener(listener);
	}

	@Override
	public final void removeTupleListener(TupleListener listener) {
		this.tupleSupport.removeTupleListener(listener);
	}
	
	protected void notifyTupleListeners (Tuple tuple) {
		this.tupleSupport.notify(tuple);
	}

}
