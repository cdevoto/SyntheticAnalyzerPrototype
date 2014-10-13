package com.compuware.apm.ruxit.synth.analyzer.resptime.strategy;

import static com.compuware.apm.ruxit.synth.analyzer.model.Attributes.newAttributes;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigTuple.newResponseTimeConfigTuple;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.SimpleResponseTimeThresholdConfig.newSimpleResponseTimeThresholdConfig;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.STEP_ID;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes.TEST_DEF_ID;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.strategy.SimpleResponseTimeStrategyFactory.newResponseTimeStrategyFactory;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.model.TupleImpl;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeThresholdConfig;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategy;

public class TestSimpleResponseTimeStrategy {

	@Test
	public void testBuild() {
		Tuple config = createConfig();
		Attributes keyAttributes = createKeyAttributes();
		ResponseTimeThresholdConfig thresholds = createThresholds(keyAttributes);
		AbstractResponseTimeStrategyFactory strategyFactory = newResponseTimeStrategyFactory()
				.withConfig(config)
				.withKeyAttributes(keyAttributes)
				.withResponseTimeThresholdConfig(thresholds)
				.build();

		Tuple key = TupleImpl.newTuple(keyAttributes)
				.withValue(TEST_DEF_ID, "1")
				.withValue(STEP_ID, "2")
			    .build();
		
		AnalysisStrategy strategy = strategyFactory.newAnalysisStrategy(key);
		assertNotNull(strategy);
	}

	private ResponseTimeThresholdConfig createThresholds(Attributes keyAttributes) {
		return newSimpleResponseTimeThresholdConfig(keyAttributes).build();
	}

	private Attributes createKeyAttributes() {
		Attributes keyAttributes = newAttributes()
				.withAttribute(TEST_DEF_ID)
				.withAttribute(STEP_ID)
				.build();
		return keyAttributes;
	}

	private Tuple createConfig() {
		Tuple.Builder builder = newResponseTimeConfigTuple();
		for (Attribute<?> attribute : ResponseTimeConfigProperties.values()) {
			Object value = attribute.getDefaultValue();
			builder.withUncheckedValue(attribute, value);
		}
		Tuple config = builder.build();
		return config;
	}

}
