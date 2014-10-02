package com.compuware.apm.ruxit.synth.analyzer.output;


public interface EventSource {
	
	public void addEventListener (EventListener listener, AnalyzerEvent.Type ... types);
	public void addEventListener (EventListener listener);
	public void removeEventListener (EventListener listener, AnalyzerEvent.Type ... types);
	public void removeEventListener (EventListener listener);

}
