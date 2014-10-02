package com.compuware.apm.ruxit.synth.analyzer.resptime;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.util.TupleGenerationConfig.newTupleGenerationConfig;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

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

public class Util {
	
    public static List<Tuple> generateDenseTuples1 () {
		Attributes keyAttributes = Attributes.newAttributes()
				.withAttribute(ResponseTimeAttributes.TEST_DEF_ID)
				.withAttribute(ResponseTimeAttributes.STEP_ID)
				.build();
		long now = 1412182929622L;

    	TupleGenerationConfig genConfig1 = newTupleGenerationConfig()
		.withKey(TupleImpl.newTuple(keyAttributes)
				.withValue(ResponseTimeAttributes.TEST_DEF_ID, "1")
				.withValue(ResponseTimeAttributes.STEP_ID, "1")
				.build())
		.withStartTime(now)
		.withMinResponseTime(0.5)
		.withResponseTimeIncrement(0.1)
		.withMaxResponseTime(1.5)
		.withInterval(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))
		.withNumCycles(1)
		.withTaperTuples(50)
		.build();
        
    	TupleGenerationConfig genConfig2 = newTupleGenerationConfig()
		.withKey(TupleImpl.newTuple(keyAttributes)
				.withValue(ResponseTimeAttributes.TEST_DEF_ID, "1")
				.withValue(ResponseTimeAttributes.STEP_ID, "2")
				.build())
		.withStartTime(now)
		.withMinResponseTime(0.5)
		.withResponseTimeIncrement(0.2)
		.withMaxResponseTime(1.5)
		.withInterval(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))
		.withNumCycles(2)
		.withTaperTuples(50)
		.build();

		List<Tuple> tuples = TupleGenerationUtil.generateTuples(genConfig1, genConfig2);
    	
		return tuples;
    }
	
    public static List<Tuple> generateDenseTuples2 () {
 		Attributes keyAttributes = Attributes.newAttributes()
 				.withAttribute(ResponseTimeAttributes.TEST_DEF_ID)
 				.withAttribute(ResponseTimeAttributes.STEP_ID)
 				.build();
 		long now = 1412182929622L;

     	TupleGenerationConfig genConfig1 = newTupleGenerationConfig()
 		.withKey(TupleImpl.newTuple(keyAttributes)
 				.withValue(ResponseTimeAttributes.TEST_DEF_ID, "1")
 				.withValue(ResponseTimeAttributes.STEP_ID, "1")
 				.build())
 		.withStartTime(now)
 		.withMinResponseTime(0.5)
 		.withResponseTimeIncrement(0.01)
 		.withMaxResponseTime(1.5)
 		.withInterval(TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS))
 		.withNumCycles(1)
 		.withTaperTuples(50)
 		.build();
         
 		List<Tuple> tuples = TupleGenerationUtil.generateTuples(genConfig1);
     	
 		return tuples;
     }

    public static List<Tuple> generateSparseTuples1 () {
		Attributes keyAttributes = Attributes.newAttributes()
				.withAttribute(ResponseTimeAttributes.TEST_DEF_ID)
				.withAttribute(ResponseTimeAttributes.STEP_ID)
				.build();
		long now = 1412182929622L;

    	TupleGenerationConfig genConfig1 = newTupleGenerationConfig()
		.withKey(TupleImpl.newTuple(keyAttributes)
				.withValue(ResponseTimeAttributes.TEST_DEF_ID, "1")
				.withValue(ResponseTimeAttributes.STEP_ID, "1")
				.build())
		.withStartTime(now)
		.withMinResponseTime(0.9)
		.withResponseTimeIncrement(0.1)
		.withMaxResponseTime(1.1)
		.withInterval(TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS))
		.withNumCycles(2)
		.withTaperTuples(50)
		.build();
        
    	TupleGenerationConfig genConfig2 = newTupleGenerationConfig()
		.withKey(TupleImpl.newTuple(keyAttributes)
				.withValue(ResponseTimeAttributes.TEST_DEF_ID, "1")
				.withValue(ResponseTimeAttributes.STEP_ID, "2")
				.build())
		.withStartTime(now)
		.withMinResponseTime(0.8)
		.withResponseTimeIncrement(0.1)
		.withMaxResponseTime(0.9)
		.withInterval(TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS))
		.withNumCycles(2)
		.withTaperTuples(50)
		.build();

		List<Tuple> tuples = TupleGenerationUtil.generateTuples(genConfig1, genConfig2);
    	
		return tuples;
    }

    public static Properties createConfigProps() throws IOException {
		Properties configProps = new Properties();
		try (InputStream in = Util.class.getResourceAsStream("config.properties")) {
			configProps.load(in);
		}
		return configProps;
	}
	
	public static void assertAnalyzerOutputs(List<Tuple> tuples,
			String[] expectedStrings, AnalyzerFactoryBuilder factoryBuilder) throws IOException {
		assertAnalyzerOutputs(tuples, expectedStrings, factoryBuilder, false);
	}
	
	public static void assertAnalyzerOutputs(List<Tuple> tuples,
		String[] expectedStrings, AnalyzerFactoryBuilder factoryBuilder, boolean verbose) throws IOException {
		
		String inputString = SimpleParserUtil.toString(tuples);
		InputSource inputSource = new StringInputSource(inputString);

		Properties configProps = Util.createConfigProps();
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
	
	public static interface AnalyzerFactoryBuilder {
		public AnalyzerFactory build(InputSource inputSource, Properties configProps);
	}
	
	
	private Util () {}
}
