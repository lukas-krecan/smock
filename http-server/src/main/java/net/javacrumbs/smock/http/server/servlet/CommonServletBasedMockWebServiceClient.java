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
package net.javacrumbs.smock.http.server.servlet;

import static net.javacrumbs.smock.common.XmlUtil.getEnvelopeSource;
import static net.javacrumbs.smock.common.XmlUtil.serialize;
import static net.javacrumbs.smock.common.XmlUtil.stringToBytes;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServlet;

import net.javacrumbs.smock.common.InterceptingTemplate;
import net.javacrumbs.smock.common.server.MockWebServiceClientResponseActions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.ResponseActions;
import org.springframework.ws.transport.WebServiceMessageReceiver;

/**
 * Common WebService client that calls instance of provided servlet class.
 * @author Lukas Krecan
 */
public class CommonServletBasedMockWebServiceClient {

	private static final String CONTENT_TYPE = "text/xml;charset=UTF-8";

	private final HttpServlet servlet;
	
	private final WebServiceMessageFactory messageFactory;

	private final InterceptingTemplate interceptingTemplate;
	
	private static final Log LOG = LogFactory.getLog(CommonServletBasedMockWebServiceClient.class);
	
	public CommonServletBasedMockWebServiceClient(HttpServlet servlet, WebServiceMessageFactory messageFactory, ClientInterceptor[] clientInterceptors) {
		Assert.notNull(servlet, "servlet has to be set");
		Assert.notNull(messageFactory, "messageFactory has to be set");
        this.messageFactory =  messageFactory;
        this.servlet = servlet;
        interceptingTemplate = new InterceptingTemplate(clientInterceptors);
	}
	
	public ResponseActions sendRequestTo(String path, RequestCreator requestCreator) {
		try {
			Assert.notNull(requestCreator, "'requestCreator' must not be null");
			WebServiceMessage requestMessage = requestCreator.createRequest(messageFactory);
			final MockHttpServletRequest   request = createRequest(path, requestMessage);
			MessageContext messageContext = new DefaultMessageContext(requestMessage, messageFactory);
			interceptingTemplate.interceptRequest(messageContext, new WebServiceMessageReceiver() {
				public void receive(MessageContext messageContext) throws Exception {
					MockHttpServletResponse response = new ExtendedMockHttpServletResponse();
					servlet.service(request, response);
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Received response:"+response.getContentAsString());
					}
					messageContext.setResponse(messageFactory.createWebServiceMessage(new ByteArrayInputStream(response.getContentAsByteArray())));
				}
			});
			return new MockWebServiceClientResponseActions(messageContext);
		} catch (Exception e) {
			throw new IllegalStateException("Error when sending request",e);
		}
	}

	protected MockHttpServletRequest createRequest(String path, WebServiceMessage message) {
		String requestXml = serialize(getEnvelopeSource(message));
		MockHttpServletRequest request = new ExtendedMockHttpServletRequest();
		request.setMethod("POST");
		request.setPathInfo(path);
		request.setRequestURI(path);
		request.setContent(stringToBytes(requestXml));
		request.setContentType(CONTENT_TYPE);
		request.addHeader("Content-Type", CONTENT_TYPE);
		return request;
	}
	
	private static class ExtendedMockHttpServletRequest extends MockHttpServletRequest
	{
		@Override
		public String getRealPath(String path) {
			return super.getPathInfo();
		}
	}
	private static class ExtendedMockHttpServletResponse extends MockHttpServletResponse
	{
		public String getContentAsString() throws UnsupportedEncodingException {
			if (getCharacterEncoding()!=null)
			{
				return new String(getContentAsByteArray(), getCharacterEncoding().toUpperCase().replaceAll("\"", ""));
			}
			else
			{
				return new String(getContentAsByteArray());
			}
		}
	}
}
