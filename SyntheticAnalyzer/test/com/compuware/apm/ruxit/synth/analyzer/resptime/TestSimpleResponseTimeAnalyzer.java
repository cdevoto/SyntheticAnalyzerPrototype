package com.compuware.apm.ruxit.synth.analyzer.resptime;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import com.compuware.apm.ruxit.synth.analyzer.AnalyzerFactory;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.resptime.input.InputSource;

public class TestSimpleResponseTimeAnalyzer {


	@Test
	public void testDenseTuples() throws IOException {
		// Testing with tuples that arrive every 5 minutes
		
		List<Tuple> tuples = Util.generateDenseTuples1();
		String [] expectedStrings = {
				"[time=1412183829622, type=ALERT, key=1|2, tuple=1412183829622|1|2|1.1000]",
				"[time=1412184429622, type=ALERT, key=1|1, tuple=1412184429622|1|1|1.0000]",
				"[time=1412185329622, type=RETURN_TO_NORMAL, key=1|2, tuple=1412185329622|1|2|0.9000]",
				"[time=1412187429622, type=ALERT, key=1|2, tuple=1412187429622|1|2|1.1000]",
				"[time=1412187729622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412187729622|1|1|0.9000]",
				"[time=1412188929622, type=RETURN_TO_NORMAL, key=1|2, tuple=1412188929622|1|2|0.9000]"
		};
		Util.AnalyzerFactoryBuilder builder = getBuilder();
		Util.assertAnalyzerOutputs(tuples, expectedStrings, builder, true);
	}

	private Util.AnalyzerFactoryBuilder getBuilder() {
		Util.AnalyzerFactoryBuilder builder = new Util.AnalyzerFactoryBuilder() {
			
			@Override
			public AnalyzerFactory build(InputSource inputSource,
					Properties configProps) {
				AnalyzerFactory factory = SimpleResponseTimeAnalyzerFactory.newResponseTimeAnalyzerFactory()
						.withConfigProperties(configProps)
						.withInputSource(inputSource)
						.build();
				return factory;
			}
		};
		return builder;
	}
	

}
