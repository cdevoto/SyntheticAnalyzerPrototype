package com.compuware.apm.ruxit.synth.analyzer.resptime.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class TestInputSource {

	@Test
	public void testFileInputSource() throws IOException {
		InputSource inputSource = new FileInputSource(new File("test-tuples-1.txt"));
		try (BufferedReader reader = inputSource.toBufferedReader()) {
			// If we got this far, everything is working
		} catch (IOException io) {
			throw io;
		}
	}

	@Test
	public void testClassPathInputSource() throws IOException {
		InputSource inputSource = new ClassPathInputSource(getClass().getPackage(), "test-tuples-1.txt");
		try (BufferedReader reader = inputSource.toBufferedReader()) {
			// If we got this far, everything is working
		} catch (IOException io) {
			throw io;
		}
	}
	
	@Test
	public void testStringInputSource() throws IOException {
		String s = "5|1|1|0.5\n" +
				   "10|1|2|0.8\n" +
                   "15|1|2|0.8\n";
		InputSource inputSource = new StringInputSource(s);
		try (BufferedReader reader = inputSource.toBufferedReader()) {
			// If we got this far, everything is working
		} catch (IOException io) {
			throw io;
		}
	}
}
