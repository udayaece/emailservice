package com.siteminder.emailservice.model;

public class ErrorResponse {
	
	
	private String errorName;
	private String[] errorMessage;
	private String errorCode;
	private String errorDescription;
	
	
	public ErrorResponse(String errorName, String[] errorMessage, String errorCode, String errorDescription) {
		super();
		this.errorName = errorName;
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}
	
	public String getErrorName() {
		return errorName;
	}
	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}
	public String[] getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String[] errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}	
	
}
