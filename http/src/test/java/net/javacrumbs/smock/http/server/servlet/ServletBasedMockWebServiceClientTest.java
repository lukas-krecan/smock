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

package net.javacrumbs.smock.http.server.servlet;

import static net.javacrumbs.smock.common.server.CommonSmockServer.message;
import static net.javacrumbs.smock.common.server.CommonSmockServer.withMessage;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import net.javacrumbs.smock.http.server.servlet.ServletBasedMockWebServiceClient;
import net.javacrumbs.smock.http.server.servlet.test.TstWebService;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

public class ServletBasedMockWebServiceClientTest {
	
	@Test
	public void testCxf()
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("cxf-servlet.xml");
		ServletBasedMockWebServiceClient client = new ServletBasedMockWebServiceClient(CXFServlet.class, context);
		client.sendRequestTo("/TestWebService", withMessage("request.xml")).andExpect(message("response.xml"));
		
		assertNotNull(TstWebService.getValue());
	}
	@Test
	public void testCxfInterceptor()
	{
		ClientInterceptor interceptor = createMock(ClientInterceptor.class);
		expect(interceptor.handleRequest((MessageContext) anyObject())).andReturn(true);
		expect(interceptor.handleResponse((MessageContext) anyObject())).andReturn(true);
		
		replay(interceptor);
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("cxf-servlet.xml");
		ServletBasedMockWebServiceClient client = new ServletBasedMockWebServiceClient(CXFServlet.class, context, new ClientInterceptor[]{interceptor});
		client.sendRequestTo("/TestWebService", withMessage("request.xml")).andExpect(message("response.xml"));
		
		assertNotNull(TstWebService.getValue());
		
		verify(interceptor);
	}
}
