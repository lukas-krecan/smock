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

import static net.javacrumbs.smock.axis2.server.SmockServer.createClient;
import static net.javacrumbs.smock.axis2.server.SmockServer.createConfigurationContextFromResource;
import static net.javacrumbs.smock.common.SmockCommon.resource;
import static net.javacrumbs.smock.common.server.CommonSmockServer.message;
import static net.javacrumbs.smock.common.server.CommonSmockServer.withMessage;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import org.apache.axis2.context.ConfigurationContext;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;


public class Axis2MockWebServiceClientTest{
	private ConfigurationContext configurationContext;
	
		
	@Before
	public void setUp() throws Exception
	{
		configurationContext = createConfigurationContextFromResource(resource(""));
	}
	
	@Test
	public void testNormal()
	{
		createClient(configurationContext).sendRequestTo("/axis2/services/TestService", withMessage("request.xml")).andExpect(message("response.xml"));
	}
	
	@Test
	public void testInterceptor()
	{
		ClientInterceptor interceptor = createMock(ClientInterceptor.class);
		expect(interceptor.handleRequest((MessageContext) anyObject())).andReturn(true);
		expect(interceptor.handleResponse((MessageContext) anyObject())).andReturn(true);
		
		replay(interceptor);
		
		Axis2MockWebServiceClient client = createClient(configurationContext, new ClientInterceptor[]{interceptor});
		client.sendRequestTo("/axis2/services/TestService", withMessage("request.xml")).andExpect(message("response.xml"));
		
		assertNotNull(TstWebService.getValue());
		
		verify(interceptor);
	}
	
	
	
}
