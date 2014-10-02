package com.compuware.apm.ruxit.synth.analyzer.resptime.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClassPathInputSource implements InputSource {

	private String classPath;
	
	public ClassPathInputSource(String classPath) {
		this.classPath = classPath;
	}

	public ClassPathInputSource(Package pkg, String resourceName) {
		this.classPath = pkg.getName().replace(".", "/") + "/" + resourceName;
	}

	@Override
	public BufferedReader toBufferedReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(this.classPath)));
	}

	@Override
	public InputStream toInputStream() throws IOException {
		return this.getClass().getClassLoader().getResourceAsStream(this.classPath);
	}

}
