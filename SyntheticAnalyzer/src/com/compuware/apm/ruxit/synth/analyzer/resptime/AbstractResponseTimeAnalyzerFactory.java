package com.compuware.apm.ruxit.synth.analyzer.resptime;

import static com.compuware.apm.ruxit.synth.analyzer.model.Attributes.newAttributes;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.clock.SimulatedClockService.newSimulatedClock;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigTuple.newResponseTimeConfigTuple;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.input.TupleSourceReaderService.newTupleSourceReaderService;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.input.parser.SimpleResponseTimeTupleParser.newResponseTimeTupleParser;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.compuware.apm.ruxit.synth.analyzer.Analyzer;
import com.compuware.apm.ruxit.synth.analyzer.AnalyzerFactory;
import com.compuware.apm.ruxit.synth.analyzer.AnalyzerImpl;
import com.compuware.apm.ruxit.synth.analyzer.clock.ClockService;
import com.compuware.apm.ruxit.synth.analyzer.input.TupleSourceService;
import com.compuware.apm.ruxit.synth.analyzer.input.parser.TupleParser;
import com.compuware.apm.ruxit.synth.analyzer.model.Attribute;
import com.compuware.apm.ruxit.synth.analyzer.model.Attributes;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.analyzer.resptime.clock.SimulatedClockService;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeConfigProperties;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.ResponseTimeThresholdConfig;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.SimpleResponseTimeThresholdConfig;
import com.compuware.apm.ruxit.synth.analyzer.resptime.input.InputSource;
import com.compuware.apm.ruxit.synth.analyzer.resptime.input.TupleSourceReaderService;
import com.compuware.apm.ruxit.synth.analyzer.resptime.model.ResponseTimeAttributes;
import com.compuware.apm.ruxit.synth.analyzer.strategy.AnalysisStrategyFactory;
import com.compuware.apm.ruxit.synth.util.BuilderUtil;
import com.compuware.apm.ruxit.synth.util.ParserUtil;

public abstract class AbstractResponseTimeAnalyzerFactory implements AnalyzerFactory {
	protected static final String PROP_NAME_PREFIX = "com.compuware.apm.ruxit.synth.analyzer.";

	protected final InputSource inputSource;
	protected final Tuple config;
	protected final TupleParser tupleParser;
	protected final Attributes keyAttributes;
	protected final ResponseTimeThresholdConfig thresholds;
	
	protected AbstractResponseTimeAnalyzerFactory (Builder builder) {
		this.inputSource = builder.inputSource;
		this.config = builder.config;
		this.tupleParser = builder.tupleParser;
		this.keyAttributes = builder.keyAttributes;
		this.thresholds = builder.thresholds;
	}
	
	@Override
	public Analyzer newAnalyzer() {
		TupleSourceService tupleSourceService = createTupleSourceService();
		ClockService clockService = createClockService(tupleSourceService);
		AnalysisStrategyFactory strategyFactory = createAnalysisStrategyFactory();
		Analyzer analyzer = AnalyzerImpl.newAnalyzer()
				.withClockService(clockService)
				.withTupleSourceService(tupleSourceService)
				.withStrategyTimeout(this.config.get(ResponseTimeConfigProperties.MAX_STRATEGY_IDLE_TIME))
				.withKeyAttributes(this.keyAttributes)
				.withStrategyFactory(strategyFactory)
				.withConfig(this.config)
				.build();
		return analyzer;
	}


	protected abstract AnalysisStrategyFactory createAnalysisStrategyFactory ();

	protected TupleSourceService createTupleSourceService() {
		TupleSourceReaderService tupleSourceService = newTupleSourceReaderService()
				.withInputSource(this.inputSource)
				.withTupleParser(this.tupleParser)
				.build();
		return tupleSourceService;
	}

	protected ClockService createClockService(TupleSourceService tupleSourceService) {
		SimulatedClockService clockService = newSimulatedClock()
				.withTupleSource(tupleSourceService)
				.withTimeAttribute(ResponseTimeAttributes.TEST_TIME)
				.withPeriod(this.config.get(ResponseTimeConfigProperties.CLOCK_TICK_INTERVAL))
				.withTimeUnit(TimeUnit.MILLISECONDS)
				.build();
		return clockService;
	}

	public static abstract class Builder {
    	protected InputSource inputSource;
    	protected Properties configProps;
       	protected final Attributes tupleSchema = ResponseTimeAttributes.ATTRIBUTES;
   	    protected Attributes keyAttributes;
    	protected TupleParser tupleParser;
    	protected Tuple config;
    	protected ResponseTimeThresholdConfig thresholds;
    	
    	protected Builder () {}
    	
    	public Builder withInputSource (InputSource inputSource) {
    		this.inputSource = inputSource;
    		this.keyAttributes = createKeyAttributes();
    		this.tupleParser = createTupleParser();
    		return this;
    	}
    	
    	public Builder withConfigProperties (Properties configProps) {
    		this.configProps = configProps;
    		return this;
    	}
    	
    	public Builder withThresholds (ResponseTimeThresholdConfig thresholds) {
    		this.thresholds = thresholds;
    		return this;
    	}
    	
    	public AbstractResponseTimeAnalyzerFactory build () {
    		BuilderUtil.validateNotNull("inputSource", inputSource);
    		BuilderUtil.validateNotNull("configProps", configProps);
    		this.config = createConfig(this.configProps);
    		if (this.thresholds == null) {
    		    this.thresholds = createThresholds(this.keyAttributes);
    		}
    		return doBuild();
    	}
    	
    	protected abstract AbstractResponseTimeAnalyzerFactory doBuild();
    	
        protected TupleParser createTupleParser() {
    		TupleParser parser =  newResponseTimeTupleParser()
    				.withSchema(this.tupleSchema)
    				.build();
    		return parser;
    	}

    	protected Attributes createKeyAttributes() {
    		Attributes keyAttributes = newAttributes()
    				.withAttribute(ResponseTimeAttributes.TEST_DEF_ID)
    				.withAttribute(ResponseTimeAttributes.STEP_ID)
    				.build();
    		return keyAttributes;
    	}
   	

		protected ResponseTimeThresholdConfig createThresholds(Attributes keyAttributes) {
			return SimpleResponseTimeThresholdConfig.newSimpleResponseTimeThresholdConfig(keyAttributes)
					.build();
		}

		protected Tuple createConfig(Properties configProps) {
			Tuple.Builder builder = newResponseTimeConfigTuple();
    		for (Attribute<?> attribute : ResponseTimeConfigProperties.values()) {
    			String propName = PROP_NAME_PREFIX + attribute.getName();
    			String stringValue = configProps.getProperty(propName);
    			Object value;
    			if (stringValue == null) {
    				value = attribute.getDefaultValue();
    			} else {
    				value = ParserUtil.parse(attribute, stringValue);
    			}
    			builder.withUncheckedValue(attribute, value);
    		}
    		return builder.build();
		}
    }
}
