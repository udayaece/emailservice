package com.siteminder.emailservice.exceptions;

public class UnSuccessfulException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public UnSuccessfulException() {
		super();
	}

	public UnSuccessfulException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UnSuccessfulException(String message) {
		super(message);
	}

	public UnSuccessfulException(Throwable cause) {
		super(cause);
	}

}
