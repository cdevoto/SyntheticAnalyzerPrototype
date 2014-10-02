package com.compuware.apm.ruxit.synth.analyzer.resptime.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class FileInputSource implements InputSource {

	private File file;
	
	public FileInputSource(String filePath) {
		this.file = new File(filePath);
	}

	public FileInputSource(File file) {
		this.file = file;
	}

	@Override
	public BufferedReader toBufferedReader() throws IOException {
		return new BufferedReader(new FileReader(this.file));
	}

	@Override
	public InputStream toInputStream() throws IOException {
		return new FileInputStream(this.file);
	}

}
