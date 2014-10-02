package com.compuware.apm.ruxit.synth.analyzer.resptime.input;

import com.compuware.apm.ruxit.synth.analyzer.input.AbstractTupleSourceService;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;

public class TupleSourceInputService extends AbstractTupleSourceService {
	
	private boolean stopped = true;
	
	public TupleSourceInputService() {}

	@Override
	public void start() {
		this.stopped = false;
	}

	@Override
	public void stop() {
		this.stopped = true;
	}
	
	public void notify (Tuple tuple) {
		if (!this.stopped) {
			notifyTupleListeners(tuple);
		}
	}
}
