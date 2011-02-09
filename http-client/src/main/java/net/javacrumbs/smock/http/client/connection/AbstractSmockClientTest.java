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

import net.javacrumbs.smock.common.client.AbstractCommonSmockClientTest;

import org.springframework.context.ApplicationContext;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.ResponseActions;

/**
 * Creates HTTP specific MockWebServiceServer.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractSmockClientTest extends AbstractCommonSmockClientTest{
	private MockWebServiceServer mockWebServiceServer;
    /**
     * Creates a {@code MockWebServiceServer} instance based on the given {@link WebServiceMessageFactory}.
     * Supports interceptors.
     * @param applicationContext
     * @param interceptors
     * @return
     */
	public void createServer(WebServiceMessageFactory messageFactory, EndpointInterceptor[] interceptors)
	{
		mockWebServiceServer = SmockClient.createServer(messageFactory, interceptors);
	}
	/**
	 * Creates a {@code MockWebServiceServer} instance.
	 * @param applicationContext
	 * @param interceptors
	 * @return
	 */
	public void createServer(ApplicationContext applicationContext, EndpointInterceptor[] interceptors)
	{
		createServer(SmockClient.createMessageFactory(applicationContext), interceptors);
	}
	/**
	 * Creates a {@code MockWebServiceServer} instance.
	 * @param applicationContext
	 * @return
	 */
	public void createServer(ApplicationContext applicationContext)
	{
		createServer(applicationContext, null);
	}

	/**
	 * Creates a {@code MockWebServiceServer} instance
	 * @return
	 */
	public void createServer()
	{
		createServer(SmockClient.createMessageFactory(), null);
	}

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

	protected MockWebServiceServer getMockWebServiceServer() {
		return mockWebServiceServer;
	}

	protected void setMockWebServiceServer(MockWebServiceServer mocWebServiceServer) {
		this.mockWebServiceServer = mocWebServiceServer;
	}

}
