/**
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.smock.http.client.connection;

import static net.javacrumbs.smock.common.XmlUtil.transform;
import static net.javacrumbs.smock.common.client.CommonSmockClient.message;
import static net.javacrumbs.smock.common.client.CommonSmockClient.withMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.ws.test.client.RequestMatchers.connectionTo;

import java.io.IOException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import net.javacrumbs.smock.extended.client.connection.MockWebServiceServer;

import org.junit.Test;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

public abstract class AbstractMockWebServiceServerTest {

	private static final String ADDRESS = "http://localhost:8080";
	private static final String ADDRESS2 = "https://localhost:8080";

	protected abstract MockWebServiceServer createServer();
	
	@Test
	public void testOk() throws IOException
	{
		MockWebServiceServer server = createServer();
		server.expect(connectionTo(ADDRESS)).andRespond(withMessage("response.xml"));

		WebServiceMessage response = sendMessage(ADDRESS, "request.xml");
		
		message("response.xml").match(null, response);
		server.verify();
		
	}
	@Test
	public void testOkHttps() throws IOException
	{
		MockWebServiceServer server = createServer();
		server.expect(connectionTo(ADDRESS2)).andRespond(withMessage("response.xml"));
		
		WebServiceMessage response = sendMessage(ADDRESS2, "request.xml");
		
		message("response.xml").match(null, response);
		server.verify();
		
	}
	@Test(expected=AssertionError.class)
	public void testVerify() throws IOException
	{
		MockWebServiceServer server = createServer();
		server.expect(connectionTo(ADDRESS)).andRespond(withMessage("response.xml"));
		server.verify();
	}
	@Test
	public void testVerifyOnEmpty() throws IOException
	{
		MockWebServiceServer server = createServer();
		server.verify();
	}
	
	@Test
	public void testTwo() throws IOException
	{
		MockWebServiceServer server = createServer();
		server.expect(connectionTo(ADDRESS)).andExpect(message("request.xml")).andRespond(withMessage("response.xml"));
		server.expect(connectionTo(ADDRESS)).andExpect(message("request2.xml")).andRespond(withMessage("response2.xml"));
		
		WebServiceMessage response1 = sendMessage(ADDRESS, "request.xml");
		message("response.xml").match(null, response1);
		
		WebServiceMessage response2 = sendMessage(ADDRESS, "request2.xml");
		message("response2.xml").match(null, response2);
		
		server.verify();
		
	}
	@Test
	public void testUnexpected() throws IOException
	{
		MockWebServiceServer server = createServer();
		server.expect(connectionTo(ADDRESS)).andExpect(message("request.xml")).andRespond(withMessage("response.xml"));
		
		WebServiceMessage response1 = sendMessage(ADDRESS, "request.xml");
		message("response.xml").match(null, response1);
		
		try
		{
			sendMessage(ADDRESS, "request2.xml");
			fail("Unexpected exeption");
		}
		catch(AssertionError e)
		{
			assertEquals("No further connections expected",e.getMessage());
		}
	}
	@Test
	public void testUnexpectedFirst() throws IOException
	{
		try
		{
			sendMessage(ADDRESS, "request1.xml");
			fail("Unexpected exeption");
		}
		catch(AssertionError e)
		{
			assertEquals("No further connections expected",e.getMessage());
		}
	}
	
	


	@Test(expected=AssertionError.class)
	public void testDifferentUri() throws IOException
	{
		MockWebServiceServer server = createServer();
		server.expect(connectionTo("http://different")).andRespond(withMessage("response.xml"));
		sendMessage(ADDRESS, "request.xml");
		server.verify();
	}
	@Test(expected=AssertionError.class)
	public void testMoreMatchersError() throws IOException
	{
		MockWebServiceServer server = createServer();
		server.expect(message("request.xml")).andExpect(connectionTo("http://different")).andRespond(withMessage("response.xml"));
		sendMessage(ADDRESS, "request.xml");
		server.verify();
	}

	protected WebServiceMessage sendMessage(String uri, final String request) {
		WebServiceTemplate template = new WebServiceTemplate();
		template.afterPropertiesSet();
				
		WebServiceMessage response = template.sendAndReceive(uri, new WebServiceMessageCallback() {
			public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
				transform(loadMessage(request), message.getPayloadResult());			
			}
		}, new WebServiceMessageExtractor<WebServiceMessage>() {
			public WebServiceMessage extractData(WebServiceMessage message) throws IOException, TransformerException {
				return message;
			}
		});
		return response;
	}

	protected WebServiceMessageFactory getMessageFactory() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
		return messageFactory;
	}

	protected StreamSource loadMessage(String request) {
		return new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(request));
	}
}
