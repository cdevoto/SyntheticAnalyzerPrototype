package com.compuware.apm.ruxit.synth.analyzer.resptime;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.compuware.apm.ruxit.synth.analyzer.resptime.clock.TestRealtimeClockService;
import com.compuware.apm.ruxit.synth.analyzer.resptime.clock.TestTupleDrivenClockService;
import com.compuware.apm.ruxit.synth.analyzer.resptime.config.TestResponseTimeConfigBuilder;
import com.compuware.apm.ruxit.synth.analyzer.resptime.input.TestInputSource;
import com.compuware.apm.ruxit.synth.analyzer.resptime.input.TestTupleSourceInputService;
import com.compuware.apm.ruxit.synth.analyzer.resptime.input.TestTupleSourceReaderService;
import com.compuware.apm.ruxit.synth.analyzer.resptime.input.parser.TestSimpleResponseTimeTupleParser;
import com.compuware.apm.ruxit.synth.analyzer.resptime.strategy.TestSimpleResponseTimeStrategy;

@RunWith(Suite.class)
@SuiteClasses({ TestSimpleResponseTimeAnalyzer.class,
		TestRealtimeClockService.class, TestTupleDrivenClockService.class,
		TestResponseTimeConfigBuilder.class, TestInputSource.class,
		TestTupleSourceInputService.class, TestTupleSourceReaderService.class,
		TestSimpleResponseTimeTupleParser.class,
		TestSimpleResponseTimeStrategy.class,
		TestSimpleResponseTimeAnalyzer.class,
		TestBinomialTestResponseTimeAnalyzer.class })
public class AllTests {

}
