package net.javacrumbs.smock.common.client.connection;

import static net.javacrumbs.smock.common.XmlUtil.getEnvelopeSource;
import static net.javacrumbs.smock.common.XmlUtil.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.ResponseActions;
import org.springframework.ws.test.client.ResponseCreator;

public class MockConnection implements ResponseActions{
	
	private ResponseCreator responseCreator;
	
	private final List<RequestMatcher> requestMatchers = new LinkedList<RequestMatcher>();
	
	private final WebServiceMessageFactory messageFactory;
	
	private static final Charset UTF8 = (Charset)Charset.availableCharsets().get("UTF-8"); 
	
	public MockConnection(RequestMatcher requestMatcher, WebServiceMessageFactory messageFactory)
	{
		requestMatchers.add(requestMatcher);
		this.messageFactory = messageFactory;
	}
	
	public ResponseActions andExpect(RequestMatcher requestMatcher) {
		// TODO Auto-generated method stub
		return null;
	}

	public void andRespond(ResponseCreator responseCreator) {
		this.responseCreator = responseCreator;
	}

	public InputStream getInputStream() throws IOException {
		WebServiceMessage message = responseCreator.createResponse(null, null, messageFactory);
		return new ByteArrayInputStream(serialize(getEnvelopeSource(message)).getBytes(UTF8));
	}

	public OutputStream getOutputStream() {
		return new ByteArrayOutputStream();
	}

	public int getResponseCode() {
		return 200;
	}
	
}
