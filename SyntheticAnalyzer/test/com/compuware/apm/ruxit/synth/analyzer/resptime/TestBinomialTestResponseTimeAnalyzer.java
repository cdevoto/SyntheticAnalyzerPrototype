package com.compuware.apm.ruxit.synth.analyzer.resptime;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import com.compuware.apm.ruxit.synth.analyzer.AnalyzerFactory;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.resptime.input.InputSource;

public class TestBinomialTestResponseTimeAnalyzer {

	@Test
	public void testDenseData1() throws IOException {
		// Testing with tuples that arrive every 5 minutes
		
		List<Tuple> tuples = Util.generateDenseTuples1();
		
		String [] expectedStrings = {
				"[time=1412184429622, type=ALERT, key=1|2, tuple=1412184429622|1|2|1.5000]",
				"[time=1412185029622, type=ALERT, key=1|1, tuple=1412185029622|1|1|1.2000]",
				"[time=1412195229622, type=RETURN_TO_NORMAL, key=1|2, tuple=1412195229622|1|2|0.5000]",
				"[time=1412195829622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412195829622|1|1|0.5000]"
		};
		
		Util.AnalyzerFactoryBuilder builder = getBuilder();
		Util.assertAnalyzerOutputs(tuples, expectedStrings, builder, true);
	}
	
	@Test
	public void testDenseData2() throws IOException {
		// Testing with tuples that arrive every 10 seconds
		// This tests whether the MAX_QUEUE_SIZE is being enforced as well as whether
		// the MIN_EVALUATION_GAP is being enforced.
		
		List<Tuple> tuples = Util.generateDenseTuples2();

		String [] expectedStrings = {
				"[time=1412183549622, type=ALERT, key=1|1, tuple=1412183549622|1|1|1.1200]",
				"[time=1412184869622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412184869622|1|1|0.5600]"
		};
		
		Util.AnalyzerFactoryBuilder builder = getBuilder();
		Util.assertAnalyzerOutputs(tuples, expectedStrings, builder, true);
		
	}

	@Test
	public void testSparseData1() throws IOException {
		// Testing with tuples that arrive every 1 hour
		// This tests whether the MAX_QUEUE_TIME_WINDOW is being enforced as well as whether
		// the MIN_SAMPLE_SIZE is being enforced.

		List<Tuple> tuples = Util.generateSparseTuples1();
		
		String [] expectedStrings = {
				"[time=1412190129622, type=ALERT, key=1|1, tuple=1412190129622|1|1|1.1000]",
				"[time=1412200929622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412200929622|1|1|0.8000]",
				"[time=1412211729622, type=ALERT, key=1|1, tuple=1412211729622|1|1|1.1000]",
				"[time=1412222529622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412222529622|1|1|0.9000]",
		};
		
		Util.AnalyzerFactoryBuilder builder = getBuilder();
		Util.assertAnalyzerOutputs(tuples, expectedStrings, builder, true);
	}
    
	private Util.AnalyzerFactoryBuilder getBuilder() {
		Util.AnalyzerFactoryBuilder builder = new Util.AnalyzerFactoryBuilder() {
			
			@Override
			public AnalyzerFactory build(InputSource inputSource,
					Properties configProps) {
				AnalyzerFactory factory = BinomialTestResponseTimeAnalyzerFactory.newResponseTimeAnalyzerFactory()
						.withConfigProperties(configProps)
						.withInputSource(inputSource)
						.build();
				return factory;
			}
		};
		return builder;
	}

	

}
