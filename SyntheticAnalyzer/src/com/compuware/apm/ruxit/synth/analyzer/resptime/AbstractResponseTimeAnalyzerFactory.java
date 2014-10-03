package com.compuware.apm.ruxit.synth.analyzer.resptime;

import static com.compuware.apm.ruxit.synth.analyzer.model.Attributes.newAttributes;
import static com.compuware.apm.ruxit.synth.analyzer.resptime.clock.TupleDrivenClockService.newSimulatedClock;
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
import com.compuware.apm.ruxit.synth.analyzer.resptime.clock.TupleDrivenClockService;
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

	private final InputSource inputSource;
	private final Tuple config;
	private final TupleParser tupleParser;
	private final Attributes keyAttributes;
	private final ResponseTimeThresholdConfig thresholds;
	private AnalysisStrategyFactoryBuilder strategyFactoryBuilder;
	private ClockServiceBuilder clockServiceBuilder; 
	private TupleSourceServiceBuilder tupleSourceServiceBuilder; 
	
	
	protected AbstractResponseTimeAnalyzerFactory (Builder builder) {
		this.inputSource = builder.inputSource;
		this.config = builder.config;
		this.tupleParser = builder.tupleParser;
		this.keyAttributes = builder.keyAttributes;
		this.thresholds = builder.thresholds;
		this.strategyFactoryBuilder = builder.strategyFactoryBuilder;
		this.clockServiceBuilder = builder.clockServiceBuilder;
		this.tupleSourceServiceBuilder = builder.tupleSourceServiceBuilder;
	}
	
	@Override
	public final Analyzer newAnalyzer() {

		TupleSourceService tupleSourceService = this.tupleSourceServiceBuilder != null ?
				this.tupleSourceServiceBuilder.build() :
				createTupleSourceService();
				
		ClockService clockService = this.clockServiceBuilder != null ?
				this.clockServiceBuilder.build(tupleSourceService) :
				createClockService(tupleSourceService);
		
		AnalysisStrategyFactory strategyFactory = this.strategyFactoryBuilder != null ?
				this.strategyFactoryBuilder.build(this.keyAttributes, this.config, this.thresholds) :
				createAnalysisStrategyFactory(this.keyAttributes, this.config, this.thresholds);
		
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


	protected abstract AnalysisStrategyFactory createAnalysisStrategyFactory (Attributes keyAttributes, Tuple config, ResponseTimeThresholdConfig thresholds);

	private TupleSourceService createTupleSourceService() {
		TupleSourceReaderService tupleSourceService = newTupleSourceReaderService()
				.withInputSource(this.inputSource)
				.withTupleParser(this.tupleParser)
				.build();
		return tupleSourceService;
	}

	private ClockService createClockService(TupleSourceService tupleSourceService) {
		TupleDrivenClockService clockService = newSimulatedClock()
				.withTupleSource(tupleSourceService)
				.withTimeAttribute(ResponseTimeAttributes.TEST_TIME)
				.withPeriod(this.config.get(ResponseTimeConfigProperties.CLOCK_TICK_INTERVAL))
				.withTimeUnit(TimeUnit.MILLISECONDS)
				.build();
		return clockService;
	}

	public static abstract class Builder {
      	private static final Attributes TUPLE_SCHEMA = ResponseTimeAttributes.ATTRIBUTES;
      	
      	protected InputSource inputSource; // mandatory
    	protected Properties configProps; // mandatory
    	protected Tuple config; // derived
    	
   	    protected Attributes keyAttributes; // optional; default is TEST_DEF, STEP_ID
    	protected TupleParser tupleParser; // optional; default is simple tuple parser
    	protected ResponseTimeThresholdConfig thresholds; // optional; default uses apdex-based thresholds
        
    	protected AnalysisStrategyFactoryBuilder strategyFactoryBuilder; // optional; default determined by subclass
    	protected ClockServiceBuilder clockServiceBuilder; // optional; default builds simulated clock service
    	protected TupleSourceServiceBuilder tupleSourceServiceBuilder; // optional; default reads input records from input source
    	
    	protected Builder () {}
    	
    	public Builder withInputSource (InputSource inputSource) {
    		this.inputSource = inputSource;
    		return this;
    	}
    	
    	public Builder withConfigProperties (Properties configProps) {
    		this.configProps = configProps;
    		return this;
    	}
    	
    	public Builder withKeyAttributes (Attributes keyAttributes) {
    		if (!TUPLE_SCHEMA.containsAll(keyAttributes)) {
    			throw new IllegalArgumentException("The specified key contains attributes which are not supported by the schema for response time tuples");
    		}
    		this.keyAttributes = keyAttributes;
    		return this;
    	}
    	
    	public Builder withTupleParser (TupleParser tupleParser) {
    		this.tupleParser = tupleParser;
    		return this;
    	}
    	
    	public Builder withThresholds (ResponseTimeThresholdConfig thresholds) {
    		this.thresholds = thresholds;
    		return this;
    	}
    	
    	public Builder withStrategyFactoryBuilder (AnalysisStrategyFactoryBuilder strategyFactoryBuilder) {
    		this.strategyFactoryBuilder = strategyFactoryBuilder;
    		return this;
    	}
    	
    	public Builder withClockServiceBuilder (ClockServiceBuilder clockServiceBuilder) {
    		this.clockServiceBuilder = clockServiceBuilder;
    		return this;
    	}
    	
    	public Builder withTupleSourceServiceBuilder (TupleSourceServiceBuilder tupleSourceServiceBuilder) {
    		this.tupleSourceServiceBuilder = tupleSourceServiceBuilder;
    		return this;
    	}
    	
    	public AbstractResponseTimeAnalyzerFactory build () {
    		BuilderUtil.validateNotNull("inputSource", inputSource);
    		BuilderUtil.validateNotNull("configProps", configProps);
    		this.config = createConfig(this.configProps);
    		if (this.keyAttributes == null) {
    		    this.keyAttributes = createKeyAttributes();
    		}
    		if (this.thresholds == null) {
    		    this.thresholds = createThresholds(this.keyAttributes);
    		}
    		if (this.tupleParser == null) {
    		    this.tupleParser = createTupleParser();
    		}
    		return doBuild();
    	}
    	
    	protected abstract AbstractResponseTimeAnalyzerFactory doBuild();
    	
        protected TupleParser createTupleParser() {
    		TupleParser parser =  newResponseTimeTupleParser()
    				.withSchema(TUPLE_SCHEMA)
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
