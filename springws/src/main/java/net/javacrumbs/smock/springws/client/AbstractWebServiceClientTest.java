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

package net.javacrumbs.smock.springws.client;

import net.javacrumbs.smock.common.client.AbstractCommonWebServiceClientTest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.RequestMatchers;
import org.springframework.ws.test.client.ResponseActions;
import org.springframework.ws.test.client.ResponseCreators;

/**
 * Simplifies Spring WS test usage. Can be extended by Spring WS client test. It wraps static methods from  {@link ResponseCreators}, {@link RequestMatchers} and
 * {@link MockWebServiceServer} and exposes them for the subclass. Moreover it automatically creates {@link MockWebServiceServer} so it can be automatically used by the subclass.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractWebServiceClientTest extends AbstractCommonWebServiceClientTest implements ApplicationContextAware {

	protected  MockWebServiceServer mockWebServiceServer; 

	/**
	 * Records an expectation specified by the given {@link RequestMatcher}. Returns a {@link ResponseActions} object
	 * that allows for creating the response, or to set up more expectations.
	 *
	 * @param requestMatcher the request matcher expected
	 * @return the response actions
	 */
	public ResponseActions expect(RequestMatcher requestMatcher) {
		return mockWebServiceServer.expect(requestMatcher);
	}

	/**
	 * Verifies that all expectations were met.
	 *
	 * @throws AssertionError in case of unmet expectations
	 */
	public void verify() {
		mockWebServiceServer.verify();
	}


	/**
	 * Creates a {@code MockWebServiceServer} instance based on the given {@link ApplicationContext}.
	 * <p/>
	 * This factory method will try and find a configured {@link WebServiceTemplate} in the given application context.
	 * If no template can be found, it will try and find a {@link WebServiceGatewaySupport}, and use its configured
	 * template. If neither can be found, an exception is thrown.
	 *
	 * @param applicationContext the application context to base the client on
	 * @return the created server
	 * @throws IllegalArgumentException if the given application context contains neither a {@link WebServiceTemplate}
	 *                                  nor a {@link WebServiceGatewaySupport}.
	 */
	public MockWebServiceServer createServer(ApplicationContext applicationContext) {
		return MockWebServiceServer.createServer(applicationContext);
	}

	public final void setApplicationContext(ApplicationContext applicationContext) {
		super.setApplicationContext(applicationContext);
		this.mockWebServiceServer = createServer(applicationContext);
	}


	protected MockWebServiceServer getMockWebServiceServer() {
		return mockWebServiceServer;
	}

	protected void setMockWebServiceServer(MockWebServiceServer mocWebServiceServer) {
		this.mockWebServiceServer = mocWebServiceServer;
	}

}
