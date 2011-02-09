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
package net.javacrumbs.smock.springws.client;

import net.javacrumbs.smock.common.client.AbstractCommonWebServiceClientTest;

import org.springframework.context.ApplicationContext;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.server.EndpointInterceptor;
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
public abstract class AbstractWebServiceClientTest extends AbstractCommonWebServiceClientTest {

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
     * Supports interceptors that will be applied on the incomming message. Please note that acctually the interceptoes will
     * be added to the {@link ClientInterceptor} set on the client side. it's an ugly hack, but that's the only way to do it 
     * without reimplementing the whole testing library. I hope it will change in next releases.
     * @param applicationContext
	 * @param interceptors 
     * @return
     */
    public void createServer(ApplicationContext applicationContext, EndpointInterceptor[] interceptors) {
    	mockWebServiceServer = SmockClient.createServer(applicationContext, interceptors);
    }
    /**
     * Creates a {@code MockWebServiceServer} instance based on the given {@link ApplicationContext}.
     * @param applicationContext
     * @param interceptors 
     * @return
     */
    public void createServer(ApplicationContext applicationContext) {
    	createServer(applicationContext, null);
    }

	protected MockWebServiceServer getMockWebServiceServer() {
		return mockWebServiceServer;
	}

	protected void setMockWebServiceServer(MockWebServiceServer mocWebServiceServer) {
		this.mockWebServiceServer = mocWebServiceServer;
	}

}
