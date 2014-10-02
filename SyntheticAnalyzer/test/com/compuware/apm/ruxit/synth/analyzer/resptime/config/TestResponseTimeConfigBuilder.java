package com.compuware.apm.ruxit.synth.analyzer.resptime.config;

import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.DEFAULT_ANOMALY_THRESHOLD;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.DEFAULT_RETURN_TO_NORMAL_THRESHOLD;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.MAX_QUEUE_SIZE;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.MAX_QUEUE_TIME_WINDOW;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.MAX_STRATEGY_IDLE_TIME;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.MIN_EVALUATION_GAP;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.MIN_SAMPLE_SIZE;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.OUT_OF_ORDER_THRESHOLD;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties.SAMPLE_SIZE_STRATEGY_THRESHOLD;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigTuple.newResponseTimeConfigTuple;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.util.ParserUtil;

public class TestResponseTimeConfigBuilder {

	@Test
	public void testBuild() throws IOException {
		Properties props = new Properties();
		try (InputStream in = getClass().getResourceAsStream("config.properties")) {
		    props.load(in);
		}
		Tuple.Builder builder = newResponseTimeConfigTuple();
		for (Attribute<?> attribute : ResponseTimeConfigProperties.values()) {
			String propName = "com.compuware.apm.ruxit.synth.analyzer." + attribute.getName();
			String stringValue = props.getProperty(propName);
			Object value;
			if (stringValue == null) {
				value = attribute.getDefaultValue();
			} else {
				value = ParserUtil.parse(attribute, stringValue);
			}
			assertNotNull(value);
			builder.withUncheckedValue(attribute, value);
		}
		Tuple config = builder.build();

		assertThat(config.get(SAMPLE_SIZE_STRATEGY_THRESHOLD), equalTo(50));
		assertThat(config.get(DEFAULT_ANOMALY_THRESHOLD), equalTo(0.1));
		assertThat(config.get(DEFAULT_RETURN_TO_NORMAL_THRESHOLD), equalTo(0.05));
		assertThat(config.get(MIN_SAMPLE_SIZE), equalTo(3));
		assertThat(config.get(OUT_OF_ORDER_THRESHOLD), equalTo(TimeUnit.MILLISECONDS.convert(15, TimeUnit.MINUTES)));
		assertThat(config.get(MAX_QUEUE_SIZE), equalTo(50));
		assertThat(config.get(MAX_QUEUE_TIME_WINDOW), equalTo(TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS)));
		assertThat(config.get(MIN_EVALUATION_GAP), equalTo(TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)));
		assertThat(config.get(MAX_STRATEGY_IDLE_TIME), equalTo(TimeUnit.MILLISECONDS.convert(24, TimeUnit.HOURS)));
		
		
		
	}

}
