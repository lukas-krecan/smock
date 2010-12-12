/*
 * Copyright 2005-2010 the original author or authors.
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

package net.javacrumbs.smock.common.server;

import static net.javacrumbs.smock.common.XmlUtil.getEnvelopeSource;
import static net.javacrumbs.smock.common.XmlUtil.serialize;
import static org.springframework.ws.test.support.AssertionErrors.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.WeakHashMap;

import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.ResponseActions;
import org.springframework.ws.test.server.ResponseMatcher;
import org.springframework.ws.test.support.MockStrategiesHelper;
import org.springframework.ws.transport.WebServiceMessageReceiver;


public class ServletBasedMockWebServiceClient {

	private static final String CONTENT_TYPE = "text/xml;charset=UTF-8";

	private HttpServlet servlet;
	
	private final WebServiceMessageFactory messageFactory;

	private final InterceptingTemplate interceptingTemplate;
	
	private static final WeakHashMap<ApplicationContext, HttpServlet> servletCache = new WeakHashMap<ApplicationContext, HttpServlet>(); 
	
	private static final Charset UTF8 = (Charset)Charset.availableCharsets().get("UTF-8");  
	
	private static final Log LOG = LogFactory.getLog(ServletBasedMockWebServiceClient.class);
	
	public ServletBasedMockWebServiceClient(Class<?> servletClass, ApplicationContext applicationContext) {
		this(servletClass, applicationContext, null);
	}
	public ServletBasedMockWebServiceClient(Class<?> servletClass, ApplicationContext applicationContext, ClientInterceptor[] clientInterceptors) {
		Assert.notNull(applicationContext, "ApplicationContext has to be set");
        messageFactory =  new MockStrategiesHelper(applicationContext).getStrategy(WebServiceMessageFactory.class, SaajSoapMessageFactory.class);
        interceptingTemplate = new InterceptingTemplate(clientInterceptors);
        createServlet(servletClass, applicationContext);
	}

	private void createServlet(Class<?> servletClass, ApplicationContext applicationContext) {
		if (servletCache.containsKey(applicationContext))
		{
			servlet = servletCache.get(applicationContext);
			return;
		}
		MockServletConfig config = new MockServletConfig();
        config.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, new ApplicationContextWrapper(applicationContext, config.getServletContext()));
		try {
			servlet = (HttpServlet) BeanUtils.instantiate(servletClass);
			servlet.init(config);
			servletCache.put(applicationContext, servlet);
		} catch (Exception e) {
			throw new IllegalArgumentException("Error when creating servlet "+servletClass.getName(),e);
		}
	}
	
	public ResponseActions sendRequestTo(String path, RequestCreator requestCreator) {
		try {
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
		request.setContent(requestXml.getBytes(UTF8));
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
	
	
	// ResponseActions

    private static class MockWebServiceClientResponseActions implements ResponseActions {

        private final MessageContext messageContext;

        private MockWebServiceClientResponseActions(MessageContext messageContext) {
            Assert.notNull(messageContext, "'messageContext' must not be null");
            this.messageContext = messageContext;
        }

        public ResponseActions andExpect(ResponseMatcher responseMatcher) {
            WebServiceMessage request = messageContext.getRequest();
            WebServiceMessage response = messageContext.getResponse();
            if (response == null) {
                fail("No response received");
                return null;
            }
            try {
                responseMatcher.match(request, response);
                return this;
            }
            catch (IOException ex) {
                fail(ex.getMessage());
                return null;
            }
        }
    }
}
