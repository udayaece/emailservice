package com.siteminder.emailservice.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private final List<String> messages = new ArrayList<>();

    public BadRequestException() {
        super();
    }

   
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

   
    public BadRequestException(String message) {
        super(message);
        messages.add(message);
    }

    public BadRequestException(List<String> messages) {
        super(StringUtils.join(messages, ","));
        this.messages.addAll(messages);
    }

   
    public BadRequestException(Throwable cause) {
        super(cause);
    }

   
    public List<String> getMessages() {
        return messages;
    }

}