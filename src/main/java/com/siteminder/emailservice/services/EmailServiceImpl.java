package com.siteminder.emailservice.services;

import static com.siteminder.emailservice.util.Constants.BEARER;
import static com.siteminder.emailservice.util.Constants.CONNECT_TIMEOUT;
import static com.siteminder.emailservice.util.Constants.HEADER_AUTHORIZATION;
import static com.siteminder.emailservice.util.Constants.HEADER_CONTENT_LENGTH;
import static com.siteminder.emailservice.util.Constants.HEADER_CONTENT_TYPE;
import static com.siteminder.emailservice.util.Constants.HEADER_RESPONSE_ACCEPT;
import static com.siteminder.emailservice.util.Constants.HTTP_OPTIONS;
import static com.siteminder.emailservice.util.Constants.MAIL_GUN_USERNAME;
import static com.siteminder.emailservice.util.Constants.READ_TIMEOUT;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siteminder.emailservice.config.ApplicationConfiguration;
import com.siteminder.emailservice.email.model.MailGunRequest;
import com.siteminder.emailservice.email.model.MailRequest;
import com.siteminder.emailservice.email.model.SendGridRequest;
import com.siteminder.emailservice.exceptions.BadRequestException;
import com.siteminder.emailservice.exceptions.ServiceUnavailableException;
import com.siteminder.emailservice.exceptions.UnSuccessfulException;
import com.siteminder.emailservice.model.EmailRequest;
import com.siteminder.emailservice.model.EmailResponse;
import com.siteminder.emailservice.util.ValidateEmailRequestUtil;

@Service
public class EmailServiceImpl implements EmailService{

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);	
	private final ApplicationConfiguration applicationConfig;

	private boolean useOtherEmailProvider = false;


	@Autowired
	public EmailServiceImpl(ApplicationConfiguration applicationConfig) {		
		this.applicationConfig = applicationConfig;
	}

	@Override
	public EmailResponse sendEmail(EmailRequest emailRequest) {
		List<String> errors = ValidateEmailRequestUtil.validate(emailRequest);
		String response = null;		
		if (!errors.isEmpty()) {
			throw new BadRequestException(errors);
		}
		if (healthCheck(applicationConfig.getSendgridUrl())) {
			useOtherEmailProvider = false;
		} else if (healthCheck(applicationConfig.getMailgunUrl())) {
			useOtherEmailProvider = true;
		}else {			
			response = "Both the email service providers are not available";			
			throw new ServiceUnavailableException(response);			
		}

		HttpURLConnection conn;
		int responseCode;
		try {
			conn = connectAndSendData(emailRequest);
			responseCode = conn.getResponseCode();
			if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
				response = "Your email sent to the provider successfully";
			}else {
				response = "Error in sending email";				
				throw new UnSuccessfulException(response);
			}
		} catch (Exception e) {
			logger.error("Exception occurred when sending email {}", e.getMessage());
			throw new UnSuccessfulException(e.getMessage());
		}
		

		return new EmailResponse(response);
	}

	private boolean healthCheck(String targetUrl) {
		try {
			URL url = new URL(targetUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod(HTTP_OPTIONS);

			return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
		} catch (IOException e) {			
			logger.error("Couldn't establish a connection to {}", e.getMessage());
			return false;
		}
	}

	private byte[] buildMailData(EmailRequest emailRequest) throws Exception {
		byte[] data = null;

		MailRequest mailRequest;
		if (!useOtherEmailProvider) {
			mailRequest = new SendGridRequest.Builder(emailRequest.getFrom(), emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText())
					.cc(emailRequest.getCc())
					.bcc(emailRequest.getBcc())
					.type(emailRequest.getType())
					.build();
		} else {
			mailRequest = new MailGunRequest.Builder(emailRequest.getFrom(), emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText())
					.cc(emailRequest.getCc())
					.bcc(emailRequest.getBcc())
					.type(emailRequest.getType())
					.build();
		}
		try {
			data = mailRequest.getData().getBytes();
		} catch (Exception e) {
			logger.error("Error occurred when building mail data, {}", e);
			throw e;
		}		
		return data;
	}

	private HttpURLConnection buildSendGridConn(String dataLength) throws IOException {
		HttpURLConnection conn = null;

		try {			
			URL url = new URL(applicationConfig.getSendgridUrl());
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod(applicationConfig.getSendgridRequestMethod());

			conn.setRequestProperty(HEADER_CONTENT_TYPE, applicationConfig.getSendgridContentType());
			conn.setRequestProperty(HEADER_CONTENT_LENGTH, dataLength);
			conn.setRequestProperty(HEADER_RESPONSE_ACCEPT, applicationConfig.getSendgridAcceptType());
			conn.setRequestProperty(HEADER_AUTHORIZATION, BEARER + applicationConfig.getSendgridKey());

			conn.setDoOutput(true);
			conn.setDoInput(true);
		} catch (IOException e) {
			logger.error("Could not connect to the sendgrid email provider, {}", e);
			throw e;
		}

		return conn;
	}

	private HttpURLConnection buildMailGunConn(String dataLength) throws IOException {
		HttpURLConnection conn = null;

		try {			
			URL url = new URL(applicationConfig.getMailgunUrl());
			conn = (HttpURLConnection) url.openConnection();


			// Set the user and password			
			Authenticator.setDefault(new Authenticator() { 
				@Override
				protected PasswordAuthentication getPasswordAuthentication() { return new
						PasswordAuthentication(MAIL_GUN_USERNAME, applicationConfig.getMailgunKey().toCharArray()); }
			});

			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod(applicationConfig.getMailgunRequestMethod());
			
			conn.setRequestProperty(HEADER_CONTENT_TYPE, applicationConfig.getMailgunContentType());
			conn.setRequestProperty(HEADER_CONTENT_LENGTH, dataLength);				

			conn.setDoOutput(true);
			conn.setDoInput(true);
		} catch (IOException e) {
			logger.error("Could not connect to the mailgun email provider, {}", e);
			throw e;
		}

		return conn;
	}

	private HttpURLConnection buildConn(String dataLength) throws IOException {
		HttpURLConnection conn;
		try {
			if (!useOtherEmailProvider) {
				conn = buildSendGridConn(dataLength);
			} else {
				conn = buildMailGunConn(dataLength);
			}
		} catch (IOException e) {            
			throw new IOException("Could not connect to the email provider, {}", e);
		}

		return conn;
	}

	private void writeToOutputStream(HttpURLConnection conn, byte[] data) throws IOException {
		OutputStream os = conn.getOutputStream();
		os.write(data);
		os.close();
	}

	private HttpURLConnection connectAndSendData(EmailRequest emailRequest) throws Exception {		
		byte[] data = buildMailData(emailRequest);	
		HttpURLConnection conn = buildConn(String.valueOf(data.length));		
		writeToOutputStream(conn, data);
		return conn;
	}

}
