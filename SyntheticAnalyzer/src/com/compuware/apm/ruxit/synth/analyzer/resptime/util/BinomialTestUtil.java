package com.compuware.apm.ruxit.synth.analyzer.resptime.util;

import org.apache.commons.math3.distribution.BinomialDistribution;

public class BinomialTestUtil {
	
	public static double getCumulativeProbability (int sampleSize, int errors, double normalErrorRate) {
		BinomialDistribution dist = new BinomialDistribution(sampleSize, normalErrorRate);
		double p = 0.0;
		for (int i = errors; i <= sampleSize; i++) {
			p += dist.probability(i);
		}
		return p;
	}

	private BinomialTestUtil () {}
}
