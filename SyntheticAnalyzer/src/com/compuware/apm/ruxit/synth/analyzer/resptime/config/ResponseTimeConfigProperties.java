package com.compuware.apm.ruxit.synth.analyzer.resptime.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;

public class ResponseTimeConfigProperties {
	
	// Temporary debug flag until we integrate with a logging library
	public static final Attribute<Boolean> DEBUG = new Attribute<>(Boolean.class, "DEBUG", false);

	// Use the binomial test only if the sample size is 50 or less; otherwise, just check to see if the error rate exceeds the default threshold
	public static final Attribute<Integer> SAMPLE_SIZE_STRATEGY_THRESHOLD = new Attribute<>(Integer.class, "SAMPLE_SIZE_STRATEGY_THRESHOLD", 50);
	
	// The default threshold against which error rates will be compared for anomaly detection
	public static final Attribute<Double> DEFAULT_ANOMALY_THRESHOLD = new Attribute<>(Double.class, "DEFAULT_ANOMALY_THRESHOLD", 0.10);     
	
	// The default threshold against which error rates will be compared for return-to-normal detection
	public static final Attribute<Double> DEFAULT_RETURN_TO_NORMAL_THRESHOLD = new Attribute<>(Double.class, "DEFAULT_RETURN_TO_NORMAL_THRESHOLD", 0.05); 
	
	// To even test whether an anomaly exists, there must be at least 3 observations in the queue. 
	public static final Attribute<Integer> MIN_SAMPLE_SIZE = new Attribute<>(Integer.class, "MIN_SAMPLE_SIZE", 3); 
	
	// Don't accept observations that are more than 15 minutes out of order.
	public static final Attribute<Long> OUT_OF_ORDER_THRESHOLD = new TimeUnitAttribute("OUT_OF_ORDER_THRESHOLD", TimeUnit.MINUTES, 15L); 
	
	// Don't hold more than 50 observations in the queue
	public static final Attribute<Integer> MAX_QUEUE_SIZE = new Attribute<>(Integer.class, "MAX_QUEUE_SIZE", 50);
	
	// Don't hold observations older than 3 hours in the queue
	public static final Attribute<Long> MAX_QUEUE_TIME_WINDOW = new TimeUnitAttribute("MAX_QUEUE_TIME_WINDOW", TimeUnit.MINUTES, 3 * 60L);
	
	// Don't perform evaluations at a rate of more than one per minute.
	public static final Attribute<Long> MIN_EVALUATION_GAP = new TimeUnitAttribute("MIN_EVALUATION_GAP", TimeUnit.MINUTES, 1L);              
	
	// Age off any strategies that have had no activity in 24 hours.
	public static final Attribute<Long> MAX_STRATEGY_IDLE_TIME = new TimeUnitAttribute("MAX_STRATEGY_IDLE_TIME", TimeUnit.MINUTES, 24 * 60L);              

	// The clock will issue notifications to the analyzer once every 5 minutes for cleanup tasks
	public static final Attribute<Long> CLOCK_TICK_INTERVAL = new TimeUnitAttribute("CLOCK_TICK_INTERVAL", TimeUnit.MINUTES, 5L);              
	
	private static final Attribute<?>[] values = {
			DEBUG, SAMPLE_SIZE_STRATEGY_THRESHOLD,
			DEFAULT_ANOMALY_THRESHOLD, DEFAULT_RETURN_TO_NORMAL_THRESHOLD,
			MIN_SAMPLE_SIZE, OUT_OF_ORDER_THRESHOLD, MAX_QUEUE_SIZE,
			MAX_QUEUE_TIME_WINDOW, MIN_EVALUATION_GAP, MAX_STRATEGY_IDLE_TIME,
			CLOCK_TICK_INTERVAL };
    private static final Set<Attribute<?>> valueSet;
    private static final Map<String, Attribute<?>> valueMap;
    
    public static final Attributes PROPERTIES = Attributes.newAttributes()
    		.withAttributes(values)
    		.build();
    static {
    	Set<Attribute<?>> tempSet = new LinkedHashSet<>();
    	Map<String, Attribute<?>> tempMap = new LinkedHashMap<>();
    	for (Attribute<?> Attribute : values) {
    		tempSet.add(Attribute);
    		tempMap.put(Attribute.getName(), Attribute);
    	}
    	valueSet = Collections.unmodifiableSet(tempSet);
    	valueMap = Collections.unmodifiableMap(tempMap);
    }
    
    public static Set<Attribute<?>> values () {
    	return valueSet;
    }
    
    public static Attribute<?> get (String name) {
    	return valueMap.get(name);
    }
	
}
