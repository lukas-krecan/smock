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

package net.javacrumbs.smock.springws.server;

import net.javacrumbs.smock.common.server.AbstractCommonWebServiceServerTest;

import org.springframework.context.ApplicationContext;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.server.SoapMessageDispatcher;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseActions;
import org.springframework.ws.test.server.ResponseMatchers;
import org.springframework.ws.transport.WebServiceMessageReceiver;

/**
 * Simplifies usage of Spring WS testing framework. Wraps static methods of {@link RequestCreators}, {@link ResponseMatchers} and {@link MockWebServiceClient}. 
 * Moreover it automatically creates {@link MockWebServiceClient} instance. 
 * @author Lukas Krecan
 *
 */
public abstract class AbstractWebServiceServerTest extends AbstractCommonWebServiceServerTest{
	protected  MockWebServiceClient mockWebServiceClient;
	

	/**
	 * Sends a request message by using the given {@link RequestCreator}. Typically called by using the default request
	 * creators provided by {@link RequestCreators}.
	 *
	 * @param requestCreator the request creator
	 * @return the response actions
	 * @see RequestCreators
	 */
	public ResponseActions sendRequest(RequestCreator requestCreator) {
		return mockWebServiceClient.sendRequest(requestCreator);
	}
	/**
	 * Creates a {@code MockWebServiceClient} instance based on the given {@link ApplicationContext}.
	 *
	 * This factory method works in a similar fashion as the standard
	 * {@link org.springframework.ws.transport.http.MessageDispatcherServlet MessageDispatcherServlet}. That is:
	 * <ul>
	 * <li>If a {@link WebServiceMessageReceiver} is configured in the given application context, it will use that.
	 * If no message receiver is configured, it will create a default {@link SoapMessageDispatcher}.</li>
	 * <li>If a {@link WebServiceMessageFactory} is configured in the given application context, it will use that.
	 * If no message factory is configured, it will create a default {@link SaajSoapMessageFactory}.</li>
	 * </ul>
	 *
	 * @param applicationContext the application context to base the client on
	 * @return the created client
	 */
	public MockWebServiceClient createClient(ApplicationContext applicationContext) {
		return MockWebServiceClient.createClient(applicationContext);
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		super.setApplicationContext(applicationContext);
		this.mockWebServiceClient = createClient(applicationContext);
	}


	protected MockWebServiceClient getMockWebServiceClient() {
		return mockWebServiceClient;
	}
	protected void setMockWebServiceClient(MockWebServiceClient mockWebServiceClient) {
		this.mockWebServiceClient = mockWebServiceClient;
	} 
}
