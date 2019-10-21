package com.siteminder.emailservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siteminder.emailservice.model.EmailRequest;
import com.siteminder.emailservice.model.EmailResponse;
import com.siteminder.emailservice.services.EmailService;

@RestController
public class SendEmailController {
	
	 private final EmailService emailService;
	 
	 @Autowired
	    public SendEmailController(EmailService emailService) {
	        this.emailService = emailService;
	    }
	 
	 @RequestMapping(value = "/send/email", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<EmailResponse> sendEmail(@Valid @RequestBody EmailRequest emailRequest){
	        return new ResponseEntity<>(emailService.sendEmail(emailRequest), HttpStatus.CREATED);
	    }
}
