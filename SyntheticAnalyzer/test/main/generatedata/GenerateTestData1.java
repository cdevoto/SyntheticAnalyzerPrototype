package main.generatedata;


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

public class GenerateTestData1 {
	
	public static void main(String[] args) throws IOException {
		String outputFile = "test-data-1.dat";
		
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
	    	 		.withStartTime(startTime)
	    	 		.withMinResponseTime(0.7)
	    	 		.withResponseTimeIncrement(0.5)
	    	 		.withMaxResponseTime(1.2)
	    	 		.withInterval(TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS))
	    	 		.withNumCycles(20)
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
