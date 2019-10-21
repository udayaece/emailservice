package com.siteminder.emailservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {
	@Value("${sendgrid.http-api.provider}")
	private String sendgridProvider;
	@Value("${sendgrid.http-api.url}")
	private String sendgridUrl;
	@Value("${sendgrid.http-api.request-method}")
	private String sendgridRequestMethod;
	@Value("${sendgrid.http-api.key}")
	private String sendgridKey;
	@Value("${sendgrid.http-api.content-type}")
	private String sendgridContentType;
	@Value("${sendgrid.http-api.accept-type}")
	private String sendgridAcceptType;

	@Value("${mailgun.http-api.provider}")
	private String mailgunProvider;	
	@Value("${mailgun.http-api.url}")
	private String mailgunUrl;
	@Value("${mailgun.http-api.request-method}")
	private String mailgunRequestMethod;
	@Value("${mailgun.http-api.key}")
	private String mailgunKey;
	@Value("${mailgun.http-api.content-type}")
	private String mailgunContentType;
	@Value("${mailgun.http-api.accept-type}")
	private String mailgunAcceptType;
	public String getSendgridProvider() {
		return sendgridProvider;
	}
	public void setSendgridProvider(String sendgridProvider) {
		this.sendgridProvider = sendgridProvider;
	}
	public String getSendgridUrl() {
		return sendgridUrl;
	}
	public void setSendgridUrl(String sendgridUrl) {
		this.sendgridUrl = sendgridUrl;
	}
	public String getSendgridRequestMethod() {
		return sendgridRequestMethod;
	}
	public void setSendgridRequestMethod(String sendgridRequestMethod) {
		this.sendgridRequestMethod = sendgridRequestMethod;
	}
	public String getSendgridKey() {
		return sendgridKey;
	}
	public void setSendgridKey(String sendgridKey) {
		this.sendgridKey = sendgridKey;
	}
	public String getSendgridContentType() {
		return sendgridContentType;
	}
	public void setSendgridContentType(String sendgridContentType) {
		this.sendgridContentType = sendgridContentType;
	}
	public String getSendgridAcceptType() {
		return sendgridAcceptType;
	}
	public void setSendgridAcceptType(String sendgridAcceptType) {
		this.sendgridAcceptType = sendgridAcceptType;
	}
	public String getMailgunProvider() {
		return mailgunProvider;
	}
	public void setMailgunProvider(String mailgunProvider) {
		this.mailgunProvider = mailgunProvider;
	}
	public String getMailgunUrl() {
		return mailgunUrl;
	}
	public void setMailgunUrl(String mailgunUrl) {
		this.mailgunUrl = mailgunUrl;
	}
	public String getMailgunRequestMethod() {
		return mailgunRequestMethod;
	}
	public void setMailgunRequestMethod(String mailgunRequestMethod) {
		this.mailgunRequestMethod = mailgunRequestMethod;
	}
	public String getMailgunKey() {
		return mailgunKey;
	}
	public void setMailgunKey(String mailgunKey) {
		this.mailgunKey = mailgunKey;
	}
	public String getMailgunContentType() {
		return mailgunContentType;
	}
	public void setMailgunContentType(String mailgunContentType) {
		this.mailgunContentType = mailgunContentType;
	}
	public String getMailgunAcceptType() {
		return mailgunAcceptType;
	}
	public void setMailgunAcceptType(String mailgunAcceptType) {
		this.mailgunAcceptType = mailgunAcceptType;
	}
	
}
