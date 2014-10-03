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

	@Test
	public void testDenseTuples3() throws IOException {
		// Testing with tuples that arrive every 5 minutes with a lot of sharp spikes and dips
		// Compare these results to the same test within TestBinomialTestResponseTimeAnalyzer.
		
		List<Tuple> tuples = Util.generateDenseTuples3();
		String [] expectedStrings = {
		        "[time=1412182939622, type=ALERT, key=1|1, tuple=1412182939622|1|1|1.2000]",
				"[time=1412182949622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412182949622|1|1|0.7000]",
				"[time=1412182979622, type=ALERT, key=1|1, tuple=1412182979622|1|1|1.2000]",
				"[time=1412182989622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412182989622|1|1|0.7000]",
				"[time=1412183019622, type=ALERT, key=1|1, tuple=1412183019622|1|1|1.2000]",
				"[time=1412183029622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183029622|1|1|0.7000]",
				"[time=1412183059622, type=ALERT, key=1|1, tuple=1412183059622|1|1|1.2000]",
				"[time=1412183069622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183069622|1|1|0.7000]",
				"[time=1412183099622, type=ALERT, key=1|1, tuple=1412183099622|1|1|1.2000]",
				"[time=1412183109622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183109622|1|1|0.7000]",
				"[time=1412183139622, type=ALERT, key=1|1, tuple=1412183139622|1|1|1.2000]",
				"[time=1412183149622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183149622|1|1|0.7000]",
				"[time=1412183179622, type=ALERT, key=1|1, tuple=1412183179622|1|1|1.2000]",
				"[time=1412183189622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183189622|1|1|0.7000]",
				"[time=1412183219622, type=ALERT, key=1|1, tuple=1412183219622|1|1|1.2000]",
				"[time=1412183229622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183229622|1|1|0.7000]",
				"[time=1412183259622, type=ALERT, key=1|1, tuple=1412183259622|1|1|1.2000]",
				"[time=1412183269622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183269622|1|1|0.7000]",
				"[time=1412183299622, type=ALERT, key=1|1, tuple=1412183299622|1|1|1.2000]",
				"[time=1412183309622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183309622|1|1|0.7000]",
				"[time=1412183339622, type=ALERT, key=1|1, tuple=1412183339622|1|1|1.2000]",
				"[time=1412183349622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183349622|1|1|0.7000]",
				"[time=1412183379622, type=ALERT, key=1|1, tuple=1412183379622|1|1|1.2000]",
				"[time=1412183389622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183389622|1|1|0.7000]",
				"[time=1412183419622, type=ALERT, key=1|1, tuple=1412183419622|1|1|1.2000]",
				"[time=1412183429622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183429622|1|1|0.7000]",
				"[time=1412183459622, type=ALERT, key=1|1, tuple=1412183459622|1|1|1.2000]",
				"[time=1412183469622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183469622|1|1|0.7000]",
				"[time=1412183499622, type=ALERT, key=1|1, tuple=1412183499622|1|1|1.2000]",
				"[time=1412183509622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183509622|1|1|0.7000]",
				"[time=1412183539622, type=ALERT, key=1|1, tuple=1412183539622|1|1|1.2000]",
				"[time=1412183549622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183549622|1|1|0.7000]",
				"[time=1412183579622, type=ALERT, key=1|1, tuple=1412183579622|1|1|1.2000]",
				"[time=1412183589622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183589622|1|1|0.7000]",
				"[time=1412183619622, type=ALERT, key=1|1, tuple=1412183619622|1|1|1.2000]",
				"[time=1412183629622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183629622|1|1|0.7000]",
				"[time=1412183659622, type=ALERT, key=1|1, tuple=1412183659622|1|1|1.2000]",
				"[time=1412183669622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183669622|1|1|0.7000]",
				"[time=1412183699622, type=ALERT, key=1|1, tuple=1412183699622|1|1|1.2000]",
				"[time=1412183709622, type=RETURN_TO_NORMAL, key=1|1, tuple=1412183709622|1|1|0.7000]",
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
