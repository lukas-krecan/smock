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
package net.javacrumbs.smock.axis2.server;

import static net.javacrumbs.smock.common.XmlUtil.getEnvelopeSource;
import static net.javacrumbs.smock.common.XmlUtil.getSourceAsStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import net.javacrumbs.smock.common.InterceptingTemplate;
import net.javacrumbs.smock.extended.server.MockWebServiceClientResponseActions;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.local.LocalTransportReceiver;
import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseActions;
import org.springframework.ws.test.support.AssertionErrors;
import org.springframework.ws.transport.WebServiceMessageReceiver;

/**
 * Creates connection to Axis 2.
 * @author Lukas Krecan
 */
public class Axis2MockWebServiceClient {
	private final WebServiceMessageFactory messageFactory;
	private final InterceptingTemplate interceptingTemplate;
	private final LocalTransportReceiver receiver;
	
	public Axis2MockWebServiceClient(WebServiceMessageFactory messageFactory, ConfigurationContext configurationContext, ClientInterceptor[] interceptors) {
		this.messageFactory =  messageFactory;
		interceptingTemplate = new InterceptingTemplate(interceptors);
		receiver = new LocalTransportReceiver(configurationContext);
	}
	public Axis2MockWebServiceClient(ConfigurationContext configurationContext, ClientInterceptor[] interceptors) {
		this(createDefaultMessageFactory(), configurationContext, interceptors);
	}
	
    private static WebServiceMessageFactory createDefaultMessageFactory() {
    	SaajSoapMessageFactory factory = new SaajSoapMessageFactory();
    	factory.afterPropertiesSet();
		return factory;
	}
	/**
     * Sends a request message by using the given {@link RequestCreator}. Typically called by using the default request
     * creators provided by {@link RequestCreators}.
     *
     * @param requestCreator the request creator
     * @return the response actions
     * @see RequestCreators
     */
    public ResponseActions sendRequestTo(final EndpointReference to, final String action, RequestCreator requestCreator) {
		try {
			Assert.notNull(requestCreator, "'requestCreator' must not be null");
			WebServiceMessage requestMessage = requestCreator.createRequest(messageFactory);

			MessageContext messageContext = new DefaultMessageContext(requestMessage, messageFactory);
			interceptingTemplate.interceptRequest(messageContext, new WebServiceMessageReceiver() {
				public void receive(MessageContext messageContext) throws Exception {
					ByteArrayOutputStream response = new ByteArrayOutputStream();
					InputStream request = getSourceAsStream(getEnvelopeSource(messageContext.getRequest()));
					receiver.processMessage(request, to, action, response);
					messageContext.setResponse(messageFactory.createWebServiceMessage(new ByteArrayInputStream(response.toByteArray())));
				}
			});
			return new MockWebServiceClientResponseActions(messageContext);
		} catch (Exception e) {
			AssertionErrors.fail(e.getMessage());
			return null;
		}
    }
    
    /**
     * Sends a request message by using the given {@link RequestCreator}. Typically called by using the default request
     * creators provided by {@link RequestCreators}.
     *
     * @param requestCreator the request creator
     * @return the response actions
     * @see RequestCreators
     */
    public ResponseActions sendRequestTo(String serviceAddress, RequestCreator requestCreator)
    {
    	EndpointReference to = new EndpointReference(serviceAddress);
		return sendRequestTo(to , null, requestCreator);
    }

}
