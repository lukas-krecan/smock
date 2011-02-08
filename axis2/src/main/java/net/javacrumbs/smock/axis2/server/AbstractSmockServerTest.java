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

import net.javacrumbs.smock.common.server.AbstractCommonSmockServerTest;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseActions;

/**
 * Creates ServletBasedMockWebServiceClient automatically and provides method for simple use of Smock library.
 * @author Lukas Krecan
 *
 */
public abstract class AbstractSmockServerTest extends AbstractCommonSmockServerTest {

	protected  Axis2MockWebServiceClient mockWebServiceClient;
	
	
	/**
	 * Sends a request message by using the given {@link RequestCreator}. Typically called by using the default request
	 * creators provided by {@link RequestCreators}.
	 *
	 * @param requestCreator the request creator
	 * @return the response actions
	 * @see RequestCreators
	 */
	public ResponseActions sendRequestTo(String uri, RequestCreator requestCreator) {
		return mockWebServiceClient.sendRequestTo(uri, requestCreator);
	}
	/**
	 * Sends a request message by using the given {@link RequestCreator}. Typically called by using the default request
	 * creators provided by {@link RequestCreators}.
	 *
	 * @param to
	 * @param soapAction
	 * @param requestCreator the request creator
	 * @return the response actions
	 * @see RequestCreators
	 */
	public ResponseActions sendRequestTo(EndpointReference to, String soapAction,  RequestCreator requestCreator) {
		return mockWebServiceClient.sendRequestTo(to, soapAction, requestCreator);
	}
	
	protected void createClient(ConfigurationContext configurationContext, ClientInterceptor[] interceptors) {
		setMockWebServiceClient(SmockServer.createClient(configurationContext, interceptors));
	}
	
	protected void createClient(ConfigurationContext configurationContext) {
		createClient(configurationContext, null);
	}
	
	protected ConfigurationContext createConfigurationContextFromResource(Resource axis2Repository) {
		return SmockServer.createConfigurationContextFromResource(axis2Repository);
	}


	protected Axis2MockWebServiceClient getMockWebServiceClient() {
		return mockWebServiceClient;
	}

	protected void setMockWebServiceClient(Axis2MockWebServiceClient mockWebServiceClient) {
		this.mockWebServiceClient = mockWebServiceClient;
	}
}
