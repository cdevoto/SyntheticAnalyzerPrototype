package com.compuware.apm.ruxit.synth.analyzer;

public class AnalyzerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AnalyzerException(String message) {
		super(message);
	}

	public AnalyzerException(Throwable cause) {
		super(cause);
	}

	public AnalyzerException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnalyzerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
