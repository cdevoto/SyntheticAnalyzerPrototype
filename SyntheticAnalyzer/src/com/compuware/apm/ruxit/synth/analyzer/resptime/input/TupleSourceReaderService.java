package com.compuware.apm.ruxit.synth.analyzer.resptime.input;

import java.io.BufferedReader;
import java.io.IOException;

import com.compuware.apm.ruxit.synth.analyzer.AnalyzerException;
import com.compuware.apm.ruxit.synth.analyzer.input.AbstractTupleSourceService;
import com.compuware.apm.ruxit.synth.analyzer.input.parser.TupleParser;
import com.compuware.apm.ruxit.synth.analyzer.model.Tuple;
import com.compuware.apm.ruxit.synth.util.BuilderUtil;

public class TupleSourceReaderService extends AbstractTupleSourceService {
	
	private InputSource inputSource;
	private TupleParser parser;
	
	public static Builder newTupleSourceReaderService() {
		return new Builder();
	}

	private TupleSourceReaderService(Builder builder) {
		this.inputSource = builder.inputSource;
		this.parser = builder.parser;
	}

	@Override
	public void start() {
		try (BufferedReader reader = inputSource.toBufferedReader()) {
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				Tuple tuple = this.parser.parse(line);
				notifyTupleListeners(tuple);
			}
		} catch (IOException e) {
			throw new AnalyzerException(e);
		}
	}

	@Override
	public void stop() {
	}
	
	public static class Builder {
		private InputSource inputSource;
		private TupleParser parser;
		
		private Builder () {}
		
		public Builder withInputSource (InputSource inputSource) {
		    this.inputSource = inputSource;
			return this;
		}
		
		public Builder withTupleParser (TupleParser parser) {
			this.parser = parser;
			return this;
		}
		
		public TupleSourceReaderService build () {
			BuilderUtil.validateNotNull("inputSource", inputSource);
			BuilderUtil.validateNotNull("tupleParser", parser);
			return new TupleSourceReaderService(this);
		}
	}

}
