package net.javacrumbs.smock.common.client.connection;

import static net.javacrumbs.smock.common.XmlUtil.getEnvelopeSource;
import static net.javacrumbs.smock.common.XmlUtil.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.ResponseActions;
import org.springframework.ws.test.client.ResponseCreator;

public class MockConnection implements ResponseActions{
	
	private static final String CONTENT_TYPE = "text/xml;charset=UTF-8";
	
	private ResponseCreator responseCreator;
	
	private final List<RequestMatcher> requestMatchers = new LinkedList<RequestMatcher>();
	
	private final WebServiceMessageFactory messageFactory;
	
	private final ByteArrayOutputStream requestStream = new ByteArrayOutputStream();
	
	private URI uri;
	
	private static final Charset UTF8 = (Charset)Charset.availableCharsets().get("UTF-8");

	private WebServiceMessage request; 
	
	public MockConnection(RequestMatcher requestMatcher, WebServiceMessageFactory messageFactory)
	{
		requestMatchers.add(requestMatcher);
		this.messageFactory = messageFactory;
	}
	
	public ResponseActions andExpect(RequestMatcher requestMatcher) {
		requestMatchers.add(requestMatcher);
		return this;
	}

	public void andRespond(ResponseCreator responseCreator) {
		this.responseCreator = responseCreator;
	}

	public InputStream getInputStream() throws IOException {
		validate(requestStream.toByteArray());
		WebServiceMessage message = responseCreator.createResponse(uri, request, messageFactory);
		
		return new ByteArrayInputStream(serialize(getEnvelopeSource(message)).getBytes(UTF8));
	}

	public OutputStream getOutputStream() {
		return requestStream;
	}

	public int getResponseCode() {
		return 200;
	}
	
	public String getHeaderField(String key) {
		if ("content-type".equals(key))
		{
			return CONTENT_TYPE;
		}
		return null;
	}
	
	protected void validate(byte[] requestData) throws IOException {
		request = messageFactory.createWebServiceMessage(new ByteArrayInputStream(requestData));
		for (RequestMatcher requestMatcher: requestMatchers)
		{
			requestMatcher.match(uri, request);
		}
		
	}
		
	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}




	
	
}
