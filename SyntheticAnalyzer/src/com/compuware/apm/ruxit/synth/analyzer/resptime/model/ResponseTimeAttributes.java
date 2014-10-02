package com.compuware.apm.ruxit.synth.analyzer.resptime.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;

public class ResponseTimeAttributes {

    public static final Attribute<Long> TEST_TIME = new Attribute<>(Long.class, "TEST_TIME");
    public static final Attribute<String> TEST_DEF_ID = new Attribute<>(String.class, "TEST_DEF_ID");
    public static final Attribute<String> STEP_ID = new Attribute<>(String.class, "STEP_ID");
    public static final Attribute<Double> RESPONSE_TIME = new Attribute<>(Double.class, "RESPONSE_TIME");
    
    private static final Attribute<?> [] values = {TEST_TIME, TEST_DEF_ID, STEP_ID, RESPONSE_TIME};
    private static final Set<Attribute<?>> valueSet;
    private static final Map<String, Attribute<?>> valueMap;
    
    public static final Attributes ATTRIBUTES = Attributes.newAttributes()
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
	
	
	private ResponseTimeAttributes() {
	}

}
