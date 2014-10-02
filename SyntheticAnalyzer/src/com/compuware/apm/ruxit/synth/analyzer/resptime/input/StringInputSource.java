package com.compuware.apm.ruxit.synth.analyzer.resptime.input;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class StringInputSource implements InputSource {

	private String string;
	
	public StringInputSource(String s) {
		this.string = s;
	}

	@Override
	public BufferedReader toBufferedReader() throws IOException {
		return new BufferedReader(new StringReader(this.string));
	}

	@Override
	public InputStream toInputStream() throws IOException {
		return new ByteArrayInputStream(this.string.getBytes(StandardCharsets.UTF_8));
	}

}
