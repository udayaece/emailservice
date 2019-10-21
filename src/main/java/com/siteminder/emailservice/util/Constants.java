package com.siteminder.emailservice.util;

public final class Constants {

	private Constants() {
		throw new IllegalStateException("Utility class");
	}

	public static final int CONNECT_TIMEOUT = 3000;
	public static final int READ_TIMEOUT = 3000;
	public static final String HTTP_OPTIONS = "OPTIONS";
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_CONTENT_LENGTH = "Content-Length";
	public static final String HEADER_RESPONSE_ACCEPT = "Accept";
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer ";
	public static final String MAIL_GUN_USERNAME = "api";
}
