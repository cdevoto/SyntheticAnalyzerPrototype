package com.compuware.apm.ruxit.synth.analyzer.resptime.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public interface InputSource {
	
	public static final InputSource NULL = new InputSource () {

		@Override
		public InputStream toInputStream() throws IOException {
			return null;
		}

		@Override
		public BufferedReader toBufferedReader() throws IOException {
			return null;
		}
		
	};
	
	public InputStream toInputStream () throws IOException;
	
	public BufferedReader toBufferedReader () throws IOException;

}
