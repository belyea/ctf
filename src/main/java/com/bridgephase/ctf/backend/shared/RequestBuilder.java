package com.bridgephase.ctf.backend.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bridgephase.ctf.backend.domain.enumeration.DataNoun;
import com.bridgephase.ctf.backend.domain.enumeration.Protocol;

public class RequestBuilder {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestBuilder.class);
	
	protected String request;
	protected static final String PROTOCOL = "<protocol>";
	protected static final String HOST = "<host>";
	protected static final String NOUN = "<noun>";
	protected static final String EVENT = "event.json"; // FDA API assumption
	
	private static final Map<String, String> defaults;
	
	static {
		defaults = new HashMap<>();
		defaults.put(PROTOCOL, Protocol.HTTP.toString());
		defaults.put(HOST, Constants.FDA_HOST);
		defaults.put(NOUN, DataNoun.DRUG.toString());
	}
	
	protected RequestBuilder() {
		StringBuilder builder = new StringBuilder();
		builder.append(PROTOCOL)
		.append("://").append(HOST)
		.append("/").append(NOUN)
		.append("/").append(EVENT)
		.append("?").append("api_key=").append(KeyStore.API)
		.append("&search=");
		request = builder.toString();
	}
	
	public static RequestBuilder builder() {
		return new RequestBuilder();
	}
	
	public RequestBuilder withProtocol(Protocol protocol) {
		request = request.replace(PROTOCOL, protocol.toString());
		return this;
	}
	
	public String build() {
		defaults();
		logger.info("Request string gerated:" + request);
		return request;
	}
	
	private void defaults() {
		String regexp = String.format("%s|%s|%s", PROTOCOL, HOST, NOUN);
		StringBuffer buffer = new StringBuffer();
		Pattern pattern = Pattern.compile(regexp);
		Matcher m = pattern.matcher(request);
		
		while (m.find()) {
			m.appendReplacement(buffer, defaults.get(m.group()));
		}
		m.appendTail(buffer);
		
		request = buffer.toString();
	}
}
