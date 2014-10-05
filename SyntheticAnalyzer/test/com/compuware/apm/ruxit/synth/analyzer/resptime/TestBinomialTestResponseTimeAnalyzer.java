package com.compuware.apm.ruxit.synth.analyzer.resptime;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.util.TupleGenerationConfig.newTupleGenerationConfig;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.compuware.apm.ruxit.synth.analyzer.Analyzer;
import com.compuware.apm.ruxit.synth.analyzer.AnalyzerFactory;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;
import com.compuware.apm.ruxit.synth.analyzer.output.AnalyzerEvent;
import com.compuware.apm.ruxit.synth.analyzer.output.EventListener;
import com.compuware.apm.ruxit.synth.analyzer.resptime.input.InputSource;
import com.compuware.apm.ruxit.synth.analyzer.resptime.input.StringInputSource;
import com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes;
import com.compuware.apm.ruxit.synth.analyzer.resptime.util.ResponseTimeStrategyUtil;
import com.compuware.apm.ruxit.synth.analyzer.resptime.util.SimpleParserUtil;
import com.compuware.apm.ruxit.synth.analyzer.resptime.util.TupleGenerationConfig;
import com.compuware.apm.ruxit.synth.analyzer.resptime.util.TupleGenerationUtil;

public class TestBinomialTestResponseTimeAnalyzer {

	@Test
	public void testDenseData1() throws IOException {
		// Testing with tuples that arrive every 5 minutes
		
		List<Tuple> tuples = Util.generateDenseTuples1();
		
		String [] expectedStrings = {
				"[time=1412184429622, type=ALERT, key=1|2, tuple=1412184429622|1|2|1.5000]",
				"[time=1412185029622, type=ALERT, key=1|1, tuple=1412185029622|1|1|1.2000]",
				"[time=1412195229622, type=RETURN_TO_NORMAL, key=1|2, tuple=1412195229622|1|2|0.5000]",
				"[time=1412196429622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412196429622|1|1|0.5000]"
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
	public void testDenseData3() throws IOException {
		// Testing with tuples that arrive every 5 minutes with a lot of sharp spikes and dips
		// Compare these results to the same test within TestSimpleTestResponseTimeAnalyzer.
		
		List<Tuple> tuples = Util.generateDenseTuples3();
		String [] expectedStrings = {
				"[time=1412183189622, type=ALERT, key=1|1, tuple=1412183179622|1|1|1.2000]",
				"[time=1412183909622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183909622|1|1|0.7000]"
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
    
	@Test
	public void testTimeout() throws IOException {
		// Here we test the condition in which an entity goes into
		// an alert status, but then we stop receiving data.  All records
		// should eventually age off the queue once they are older than
		// the max queue time window (3 hours), and at that point, a
		// TIMEOUT event should be generated.
		
    	TupleGenerationConfig genConfig1 = newTupleGenerationConfig()
		.withKey(TupleImpl.newTuple(Attributes.newAttributes()
				.withAttribute(ResponseTimeAttributes.TEST_DEF_ID)
				.withAttribute(ResponseTimeAttributes.STEP_ID)
				.build())
				.withValue(ResponseTimeAttributes.TEST_DEF_ID, "1")
				.withValue(ResponseTimeAttributes.STEP_ID, "1")
				.build())
		.withStartTime(1412182929622L)
		.withMinResponseTime(0.5)
		.withResponseTimeIncrement(0.1)
		.withMaxResponseTime(1.5)
		.withInterval(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))
		.withNumCycles(1)
		.build();
		
		List<Tuple> tuples = TupleGenerationUtil.generateTuples(genConfig1);
		
		// The tuple generation algorithm generated an entire cycle of tuples
		// ranging from the min response time to the max response time and back.
		// we only want half a cycle, because we want an alert without a return to normal
		for (int i = tuples.size() - 1; i >= 0; i--) {
			Tuple tuple = tuples.get(i);
			if (tuple.get(ResponseTimeAttributes.RESPONSE_TIME) < genConfig1.getMaxResponseTime()) {
				tuples.remove(i);
			} else {
				break;
			}
		}
		
		String [] expectedStrings = {
				"[time=1412185029622, type=ALERT, key=1|1, tuple=1412185029622|1|1|1.2000]",
				"[time=1412196729622, type=TIME_OUT, key=1|1, tuple=null]"
		};

		
		boolean verbose = true;
	
		String inputString = SimpleParserUtil.toString(tuples);
		InputSource inputSource = new StringInputSource(inputString);

		Properties configProps = Util.createConfigProps();
		Util.AnalyzerFactoryBuilder factoryBuilder = getBuilder();
		AnalyzerFactory factory = factoryBuilder.build(inputSource, configProps);
		
		Analyzer analyzer = factory.newAnalyzer();
		
		final List<String> actual = new ArrayList<>();
		
		analyzer.addEventListener(new EventListener() {
			
			@Override
			public void onEvent(AnalyzerEvent event) {
				String eventString = ResponseTimeStrategyUtil.toString(event);
				actual.add(eventString);
			}
		});
		
		analyzer.start();
		analyzer.stop();
		
		if (verbose) {
			for (String event : actual) {
				System.out.println(event);
			}
		}
		List<String> expected = Arrays.asList(expectedStrings);
        assertThat(actual, equalTo(expected));

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
