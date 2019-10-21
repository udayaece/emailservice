package com.siteminder.emailservice.services;

import com.siteminder.emailservice.model.EmailRequest;
import com.siteminder.emailservice.model.EmailResponse;

public interface EmailService {

	public EmailResponse sendEmail(EmailRequest request);
}
