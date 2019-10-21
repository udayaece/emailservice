package com.siteminder.emailservice.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.siteminder.emailservice.exceptions.BadRequestException;
import com.siteminder.emailservice.exceptions.ServiceUnavailableException;
import com.siteminder.emailservice.exceptions.UnSuccessfulException;
import com.siteminder.emailservice.model.ErrorResponse;

@ControllerAdvice("com.siteminder.emailservice.controller")
public class SendEmailControllerExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(SendEmailControllerExceptionHandler.class);
	
	@ExceptionHandler(value = BadRequestException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException error) {
		logger.error("In BadRequestException exception handler:{}", error.getMessage());
		return ResponseEntity.status(400)
		        .body(new ErrorResponse("Bad request exception occurred", new String[] {error.getMessage()}, "400", "BAD_REQUEST"));
	}
	
	@ExceptionHandler(value = ServiceUnavailableException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleServiceUnavailbaleException(final ServiceUnavailableException error) {
		logger.error("In ServiceUnavailableException exception handler:{}", error.getMessage());
		return ResponseEntity.status(500)
		        .body(new ErrorResponse("Service Unavailable", new String[] {error.getMessage()}, "500", "EMAIL_SERVICE_PROVIDERS_UNAVAILABLE"));
	}
	
	@ExceptionHandler(value = UnSuccessfulException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleUnSuccessfulException(final UnSuccessfulException error) {
		logger.error("In UnSuccessfulException exception handler:{}", error.getMessage());
		return ResponseEntity.status(500)
		        .body(new ErrorResponse(error.getMessage(), new String[] {error.getMessage()}, "500", error.getMessage()));
	}

}
