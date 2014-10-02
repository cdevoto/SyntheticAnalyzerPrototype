package com.compuware.apm.ruxit.synth.analyzer.resptime.util;

import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.output.AnalyzerEvent;
import com.compuware.apm.ruxit.synth.analyzer.resptime.strategy.AbstractResponseTimeStrategy;

public class ResponseTimeStrategyUtil {

	public static String toString (AnalyzerEvent event) {
		String eventType = event.getType().toString();
		Tuple eventSource = event.getSource();
		long eventTime = eventSource.get(AbstractResponseTimeStrategy.EVENT_TIME);
		Tuple eventKey = eventSource.get(AbstractResponseTimeStrategy.EVENT_KEY);
		Tuple eventTuple = eventSource.get(AbstractResponseTimeStrategy.EVENT_TUPLE);
		String eventString = "[time=" + eventTime + ", type=" + eventType + ", key=" + SimpleParserUtil.toString(eventKey) + ", tuple=" + SimpleParserUtil.toString(eventTuple) + "]"; 
        return eventString;		
	}
	
	private ResponseTimeStrategyUtil () {}

}
