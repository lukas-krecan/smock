package net.javacrumbs.smock.common.client.connection;

import static net.javacrumbs.smock.common.XmlUtil.doTransform;
import static net.javacrumbs.smock.common.client.CommonSmockClient.message;
import static net.javacrumbs.smock.common.client.CommonSmockClient.withMessage;
import static org.junit.Assert.*;
import static org.springframework.ws.test.client.RequestMatchers.connectionTo;

import java.io.IOException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

public class ThreadLocalMockWebServiceServerTest {

	private static final String ADDRESS = "http://localhost:8080";

	@Test
	public void testOk() throws IOException
	{
		System.setProperty("java.protocol.handler.pkgs", "net.javacrumbs.smock.common.client.connection.http.protocol");
		ThreadLocalMockWebServiceServer server = new ThreadLocalMockWebServiceServer(getMessageFactory());
		server.expect(connectionTo(ADDRESS)).andRespond(withMessage("response.xml"));

		WebServiceMessage response = sendMessage(ADDRESS, "request.xml");
		
		message("response.xml").match(null, response);
		
	}
	@Test(expected=AssertionError.class)
	public void testDifferentUri() throws IOException
	{
		System.setProperty("java.protocol.handler.pkgs", "net.javacrumbs.smock.common.client.connection.http.protocol");
		ThreadLocalMockWebServiceServer server = new ThreadLocalMockWebServiceServer(getMessageFactory());
		server.expect(connectionTo("http://different")).andRespond(withMessage("response.xml"));
		sendMessage(ADDRESS, "request.xml");
	}
	@Test(expected=AssertionError.class)
	public void testMoreMatchers() throws IOException
	{
		System.setProperty("java.protocol.handler.pkgs", "net.javacrumbs.smock.common.client.connection.http.protocol");
		ThreadLocalMockWebServiceServer server = new ThreadLocalMockWebServiceServer(getMessageFactory());
		server.expect(message("request.xml")).andExpect(connectionTo("http://different")).andRespond(withMessage("response.xml"));
		sendMessage(ADDRESS, "request.xml");
	}

	private WebServiceMessage sendMessage(String uri, final String request) {
		WebServiceTemplate template = new WebServiceTemplate();
		template.afterPropertiesSet();
				
		WebServiceMessage response = template.sendAndReceive(uri, new WebServiceMessageCallback() {
			public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
				doTransform(loadMessage(request), message.getPayloadResult());			
			}
		}, new WebServiceMessageExtractor<WebServiceMessage>() {
			public WebServiceMessage extractData(WebServiceMessage message) throws IOException, TransformerException {
				return message;
			}
		});
		return response;
	}

	private WebServiceMessageFactory getMessageFactory() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
		return messageFactory;
	}

	private StreamSource loadMessage(String request) {
		return new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(request));
	}
}
