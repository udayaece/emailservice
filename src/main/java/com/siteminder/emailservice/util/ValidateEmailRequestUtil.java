package com.siteminder.emailservice.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import com.siteminder.emailservice.model.EmailRequest;

public class ValidateEmailRequestUtil {
	
	private ValidateEmailRequestUtil() {
		throw new IllegalStateException("Utility class");
	}
	
	public static List<String> validate(EmailRequest request) {
		List<String> errors = new ArrayList<>();

		// Mandatory check - the from and to email need to exist
		if (request.getFrom() == null || "".equals(request.getFrom())) {
			errors.add("From email is missing");

			return errors;
		} else if ((request.getTo().length == 0) && (request.getCc().length == 0) && (request.getBcc().length == 0)) {
			errors.add("No no, cannot send an email without any recipients");

			return errors;
		}

		// Email address format check
		EmailValidator emailValidator = EmailValidator.getInstance();
		checkEmailFormat(errors, emailValidator, new String[]{
				request.getFrom()
		}, "from");

		checkEmailFormat(errors, emailValidator, request.getTo(), "to");
		checkEmailFormat(errors, emailValidator, request.getCc(), "cc");
		checkEmailFormat(errors, emailValidator, request.getBcc(), "bcc");

		// Check duplicate recipients because some providers will reject duplicates
		checkDuplicateRecipients(errors, request);

		return errors;
	}

	public static void checkEmailFormat(List<String> errors, EmailValidator validator, String[] emails, String type) {
		for (String email : emails) {
			if (!validator.isValid(email)) {
				errors.add(String.format("'%s' email is invalid - %s", type, email));
			}
		}
	}

	public static void checkDuplicateRecipients(List<String> errors, EmailRequest request) {
		Set<String> toSet = new HashSet<>();
		Set<String> ccSet = new HashSet<>();
		Set<String> bccSet = new HashSet<>();

		Set<String> duplicates = new HashSet<>();
		for (String to : request.getTo()) {
			if (toSet.contains(to)) {
				duplicates.add(to);
			}
			toSet.add(to);
		}

		for (String cc : request.getCc()) {
			if (toSet.contains(cc) || ccSet.contains(cc)) {
				duplicates.add(cc);
			}
			ccSet.add(cc);
		}

		for (String bcc : request.getBcc()) {
			if (toSet.contains(bcc) || ccSet.contains(bcc) || bccSet.contains(bcc)) {
				duplicates.add(bcc);
			}
			bccSet.add(bcc);
		}
		if (!duplicates.isEmpty()) {
			errors.add(String.format("Email address in to, cc and bcc should be unique - %s", StringUtils.join(duplicates, ",")));
		}
	}
}