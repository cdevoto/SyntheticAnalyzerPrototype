

import static com.compuware.apm.ruxit.synth.analyzer.resptime.util.TupleGenerationConfig.newTupleGenerationConfig;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;
import com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes;
import com.compuware.apm.ruxit.synth.analyzer.resptime.util.SimpleParserUtil;
import com.compuware.apm.ruxit.synth.analyzer.resptime.util.TupleGenerationConfig;
import com.compuware.apm.ruxit.synth.analyzer.resptime.util.TupleGenerationUtil;

public class GenerateTestData {
	
	public static void main(String[] args) throws IOException {
		String outputFile = "test-binomial-strategy.dat";
		
		Attributes keyAttributes = Attributes.newAttributes()
				.withAttribute(ResponseTimeAttributes.TEST_DEF_ID)
				.withAttribute(ResponseTimeAttributes.STEP_ID)
				.build();
		
		long startTime = 1412182929622L;
		
    	TupleGenerationConfig genConfigs []  = new TupleGenerationConfig [] {
    			
    			newTupleGenerationConfig()
					.withKey(TupleImpl.newTuple(keyAttributes)
							.withValue(ResponseTimeAttributes.TEST_DEF_ID, "1")
							.withValue(ResponseTimeAttributes.STEP_ID, "1")
							.build())
					.withStartTime(1412182929622L)
					.withMinResponseTime(0.5)
					.withResponseTimeIncrement(0.1)
					.withMaxResponseTime(1.5)
					.withInterval(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))
					.withNumCycles(1)
					.withTaperTuples(50)
					.build(),
		
				newTupleGenerationConfig()
					.withKey(TupleImpl.newTuple(keyAttributes)
							.withValue(ResponseTimeAttributes.TEST_DEF_ID, "1")
							.withValue(ResponseTimeAttributes.STEP_ID, "2")
							.build())
					.withStartTime(startTime)
					.withMinResponseTime(0.5)
					.withResponseTimeIncrement(0.2)
					.withMaxResponseTime(1.5)
					.withInterval(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES))
					.withNumCycles(2)
					.withTaperTuples(50)
					.build(),
  	
		     	newTupleGenerationConfig()
			 		.withKey(TupleImpl.newTuple(keyAttributes)
			 				.withValue(ResponseTimeAttributes.TEST_DEF_ID, "2")
			 				.withValue(ResponseTimeAttributes.STEP_ID, "1")
			 				.build())
			 		.withStartTime(startTime)
			 		.withMinResponseTime(0.5)
			 		.withResponseTimeIncrement(0.01)
			 		.withMaxResponseTime(1.5)
			 		.withInterval(TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS))
			 		.withNumCycles(1)
			 		.withTaperTuples(50)
			 		.build(),
    	
		    	newTupleGenerationConfig()
					.withKey(TupleImpl.newTuple(keyAttributes)
							.withValue(ResponseTimeAttributes.TEST_DEF_ID, "2")
							.withValue(ResponseTimeAttributes.STEP_ID, "2")
							.build())
					.withStartTime(startTime)
					.withMinResponseTime(0.9)
					.withResponseTimeIncrement(0.1)
					.withMaxResponseTime(1.1)
					.withInterval(TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS))
					.withNumCycles(2)
					.withTaperTuples(50)
					.build(),
					
				newTupleGenerationConfig()
					.withKey(TupleImpl.newTuple(keyAttributes)
							.withValue(ResponseTimeAttributes.TEST_DEF_ID, "3")
							.withValue(ResponseTimeAttributes.STEP_ID, "1")
							.build())
					.withStartTime(startTime)
					.withMinResponseTime(0.8)
					.withResponseTimeIncrement(0.1)
					.withMaxResponseTime(0.9)
					.withInterval(TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS))
					.withNumCycles(2)
					.withTaperTuples(50)
					.build()	
    	};
     	
		List<Tuple> tuples = TupleGenerationUtil.generateTuples(genConfigs);
		
		try (PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {
			for (Tuple tuple : tuples) {
				out.println(SimpleParserUtil.toString(tuple));
			}
		}
		
		System.out.printf("Data file %s was generated successfully.%n", outputFile);
	}

}
