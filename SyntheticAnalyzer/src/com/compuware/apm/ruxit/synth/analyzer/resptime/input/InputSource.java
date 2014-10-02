package com.compuware.apm.ruxit.synth.analyzer.resptime.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public interface InputSource {
	
	public InputStream toInputStream () throws IOException;
	
	public BufferedReader toBufferedReader () throws IOException;

}
