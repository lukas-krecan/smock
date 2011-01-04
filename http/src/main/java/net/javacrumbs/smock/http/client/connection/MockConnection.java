package net.javacrumbs.smock.http.client.connection;

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

import net.javacrumbs.smock.common.EndpointInterceptorClientAdapter;
import net.javacrumbs.smock.common.InterceptingTemplate;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.ResponseActions;
import org.springframework.ws.test.client.ResponseCreator;
import org.springframework.ws.transport.WebServiceMessageReceiver;

public class MockConnection implements ResponseActions{
	
	private static final String CONTENT_TYPE = "text/xml;charset=UTF-8";
	
	private ResponseCreator responseCreator;
	
	private final List<RequestMatcher> requestMatchers = new LinkedList<RequestMatcher>();
	
	private final WebServiceMessageFactory messageFactory;
	
	private final ByteArrayOutputStream requestStream = new ByteArrayOutputStream();
	
	private URI uri;
	
	private static final Charset UTF8 = (Charset)Charset.availableCharsets().get("UTF-8");

	private final EndpointInterceptor[] interceptors; 
	
	public MockConnection(RequestMatcher requestMatcher, WebServiceMessageFactory messageFactory, EndpointInterceptor[] interceptors)
	{
		requestMatchers.add(requestMatcher);
		this.messageFactory = messageFactory;
		this.interceptors = interceptors;
	}
	
	public ResponseActions andExpect(RequestMatcher requestMatcher) {
		requestMatchers.add(requestMatcher);
		return this;
	}

	public void andRespond(ResponseCreator responseCreator) {
		this.responseCreator = responseCreator;
	}

	public InputStream getInputStream() throws IOException {
		final WebServiceMessage request = crateRequest();
		MessageContext messageContext = new DefaultMessageContext(request, messageFactory);

		InterceptingTemplate interceptingTemplate = new InterceptingTemplate(EndpointInterceptorClientAdapter.wrapEndpointInterceptors(interceptors));
		try {
			interceptingTemplate.interceptRequest(messageContext, new WebServiceMessageReceiver() {
				public void receive(MessageContext context) throws Exception {
					validate(request);
					context.setResponse(responseCreator.createResponse(uri, request, messageFactory));
				}
			});
		} catch (Exception e) {
			throw new IOException("Error when processing request.",e);
		}
		return new ByteArrayInputStream(serialize(getEnvelopeSource(messageContext.getResponse())).getBytes(UTF8));
	}

	protected WebServiceMessage crateRequest() throws IOException {
		 return messageFactory.createWebServiceMessage(new ByteArrayInputStream(requestStream.toByteArray()));
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
	
	protected void validate(WebServiceMessage request) throws IOException {
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
