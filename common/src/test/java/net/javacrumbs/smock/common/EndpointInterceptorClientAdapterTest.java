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
package net.javacrumbs.smock.common;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.server.endpoint.interceptor.PayloadLoggingInterceptor;


public class EndpointInterceptorClientAdapterTest {

	@Test
	public void testWrapNull()
	{
		ClientInterceptor[] endpointInterceptors = EndpointInterceptorClientAdapter.wrapEndpointInterceptors(null);
		assertArrayEquals(new ClientInterceptor[0], endpointInterceptors);
	}
	@Test
	public void testWrapEmpty()
	{
		ClientInterceptor[] endpointInterceptors = EndpointInterceptorClientAdapter.wrapEndpointInterceptors(new EndpointInterceptor[0]);
		assertArrayEquals(new ClientInterceptor[0], endpointInterceptors);
	}
	@Test
	public void testWrapOne()
	{
		ClientInterceptor[] endpointInterceptors = EndpointInterceptorClientAdapter.wrapEndpointInterceptors(new EndpointInterceptor[]{new PayloadLoggingInterceptor()});
		assertEquals(1, endpointInterceptors.length);
		assertEquals(EndpointInterceptorClientAdapter.class, endpointInterceptors[0].getClass());
	}
	@Test
	public void testWrapperHandleRequest() throws Exception
	{
		MessageContext messageContext = createMock(MessageContext.class);
		EndpointInterceptor interceptor = createMock(EndpointInterceptor.class);
		expect(interceptor.handleRequest(messageContext, null)).andReturn(true);
		
		replay(interceptor);
		
		EndpointInterceptorClientAdapter adapter = new EndpointInterceptorClientAdapter(interceptor);
		adapter.handleRequest(messageContext);
		
		verify(interceptor);
	}
	@Test
	public void testWrapperHandleResponse() throws Exception
	{
		MessageContext messageContext = createMock(MessageContext.class);
		EndpointInterceptor interceptor = createMock(EndpointInterceptor.class);
		expect(interceptor.handleResponse(messageContext, null)).andReturn(true);
		
		replay(interceptor);
		
		EndpointInterceptorClientAdapter adapter = new EndpointInterceptorClientAdapter(interceptor);
		adapter.handleResponse(messageContext);
		
		verify(interceptor);
	}
	@Test
	public void testWrapperHandleFault() throws Exception
	{
		MessageContext messageContext = createMock(MessageContext.class);
		EndpointInterceptor interceptor = createMock(EndpointInterceptor.class);
		expect(interceptor.handleFault(messageContext, null)).andReturn(true);
		
		replay(interceptor);
		
		EndpointInterceptorClientAdapter adapter = new EndpointInterceptorClientAdapter(interceptor);
		adapter.handleFault(messageContext);
		
		verify(interceptor);
	}
}
