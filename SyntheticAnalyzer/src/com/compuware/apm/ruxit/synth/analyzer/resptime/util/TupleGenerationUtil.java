package com.compuware.apm.ruxit.synth.analyzer.resptime.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;
import com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes;

public class TupleGenerationUtil {

	public static List<Tuple> generateTuples(TupleGenerationConfig ... genConfigs) {
		Set<Tuple> tuples = new TreeSet<>(new Comparator<Tuple> () {

			@Override
			public int compare(Tuple t1, Tuple t2) {
				long testTime1 = t1.get(ResponseTimeAttributes.TEST_TIME);
				long testTime2 = t2.get(ResponseTimeAttributes.TEST_TIME);
				if (testTime1 < testTime2) {
					return -1;
				} else if (testTime1 > testTime2) {
					return 1;
				} 
				return t1.hashCode() - t2.hashCode();
			}
		});
		
		for (TupleGenerationConfig genConfig : genConfigs) {
			double currentResponseTime = genConfig.getMinResponseTime();
			List<Double> responseTimeList = new ArrayList<>();
			for (int i = 0; i < genConfig.getNumCycles(); i++) {
				if (genConfig.getMinResponseTime() == genConfig.getMaxResponseTime() || genConfig.getResponseTimeIncrement() == 0) {
					responseTimeList.add(currentResponseTime);
					break;
				}
				int direction = 1;
				while (true) {
					responseTimeList.add(currentResponseTime);
					currentResponseTime += direction * genConfig.getResponseTimeIncrement();
					currentResponseTime = ((double) Math.round(currentResponseTime * 10000)) / 10000;
					if (currentResponseTime >= genConfig.getMaxResponseTime()) {
						direction *= -1;
					} else if (currentResponseTime < genConfig.getMinResponseTime()) {
						break;
					}
				}
			}
		
			int numTuples = responseTimeList.size();
			
			
			long [] testTimes = new long [numTuples];
			long currentTime = genConfig.getStartTime();
			for (int i = 0; i < testTimes.length; i++, currentTime += genConfig.getInterval()) {
				testTimes[i] = currentTime;
			}
			
			double [] responseTimes = new double [numTuples];
			for (int i = 0; i < numTuples; i++) {
				responseTimes[i] = responseTimeList.get(i);
			}
			
			for (int i = 0; i < numTuples; i++) {
				Tuple tuple = TupleImpl.newTuple(ResponseTimeAttributes.ATTRIBUTES)
						.withValue(ResponseTimeAttributes.TEST_TIME, testTimes[i])
						.withValue(ResponseTimeAttributes.TEST_DEF_ID, genConfig.getKey().get(ResponseTimeAttributes.TEST_DEF_ID))
						.withValue(ResponseTimeAttributes.STEP_ID, genConfig.getKey().get(ResponseTimeAttributes.STEP_ID))
						.withValue(ResponseTimeAttributes.RESPONSE_TIME, responseTimes[i])
						.build();
				
				tuples.add(tuple);
			}
			
			currentTime = testTimes[testTimes.length - 1] + genConfig.getInterval();
			for (int i = 0; i < genConfig.getTaperTuples(); i++, currentTime += genConfig.getInterval()) {
				Tuple tuple = TupleImpl.newTuple(ResponseTimeAttributes.ATTRIBUTES)
						.withValue(ResponseTimeAttributes.TEST_TIME, currentTime)
						.withValue(ResponseTimeAttributes.TEST_DEF_ID, genConfig.getKey().get(ResponseTimeAttributes.TEST_DEF_ID))
						.withValue(ResponseTimeAttributes.STEP_ID, genConfig.getKey().get(ResponseTimeAttributes.STEP_ID))
						.withValue(ResponseTimeAttributes.RESPONSE_TIME, genConfig.getMinResponseTime())
						.build();
				
				tuples.add(tuple);
				
			}
		}
        return new ArrayList<Tuple>(tuples);		
	}

	private TupleGenerationUtil () {}
}
