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

package net.javacrumbs.smock.common;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Test;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.WebServiceMessageReceiver;


public class InterceptingTemplateTest {
		
	
	

	@Test
	public void testNoIntercptors() throws Exception
	{
		MessageContext messageContext = createMock(MessageContext.class);
		WebServiceMessageReceiver messageReceiver = createMock(WebServiceMessageReceiver.class);
		messageReceiver.receive(messageContext);
		expect(messageContext.hasResponse()).andReturn(false);
		expect(messageContext.hasResponse()).andReturn(true).atLeastOnce();
		expect(messageContext.getResponse()).andReturn(null);
		replay(messageReceiver, messageContext);
				
		
		InterceptingTemplate template = new InterceptingTemplate(null);
		
		template.interceptRequest(messageContext, messageReceiver);
		
		verify(messageReceiver, messageContext);
	}
	@Test
	public void testNormalIntercptor() throws Exception
	{
		MessageContext messageContext = createMock(MessageContext.class);

		WebServiceMessageReceiver messageReceiver = createMock(WebServiceMessageReceiver.class);
		ClientInterceptor interceptor = createMock(ClientInterceptor.class);
		
		messageReceiver.receive(messageContext);
		expect(interceptor.handleRequest(messageContext)).andReturn(true);
		expect(interceptor.handleResponse(messageContext)).andReturn(true);
		expect(messageContext.hasResponse()).andReturn(false);
		expect(messageContext.hasResponse()).andReturn(true).atLeastOnce();
		expect(messageContext.getResponse()).andReturn(null);
		
		replay(messageReceiver, interceptor, messageContext);
		
		
		InterceptingTemplate template = new InterceptingTemplate(new ClientInterceptor[]{interceptor});
		
		template.interceptRequest(messageContext, messageReceiver);
		
		verify(messageReceiver, interceptor, messageContext);
	}
	@Test
	public void testNormalTwoIntercptors() throws Exception
	{
		MessageContext messageContext = createMock(MessageContext.class);
		
		WebServiceMessageReceiver messageReceiver = createMock(WebServiceMessageReceiver.class);
		ClientInterceptor interceptor1 = createMock(ClientInterceptor.class);
		ClientInterceptor interceptor2 = createMock(ClientInterceptor.class);
		
		messageReceiver.receive(messageContext);
		expect(interceptor1.handleRequest(messageContext)).andReturn(true);
		expect(interceptor1.handleResponse(messageContext)).andReturn(true);
		expect(interceptor2.handleRequest(messageContext)).andReturn(true);
		expect(interceptor2.handleResponse(messageContext)).andReturn(true);
		expect(messageContext.hasResponse()).andReturn(false);
		expect(messageContext.hasResponse()).andReturn(true).atLeastOnce();
		expect(messageContext.getResponse()).andReturn(null);
		
		replay(messageReceiver, interceptor1, interceptor2, messageContext);
		
		
		InterceptingTemplate template = new InterceptingTemplate(new ClientInterceptor[]{interceptor1, interceptor2});
		
		template.interceptRequest(messageContext, messageReceiver);
		
		verify(messageReceiver, interceptor1, interceptor2, messageContext);
	}
	@Test
	public void testBreakAfterFirstIntercptor() throws Exception
	{
		MessageContext messageContext = createMock(MessageContext.class);
		
		WebServiceMessageReceiver messageReceiver = createMock(WebServiceMessageReceiver.class);
		ClientInterceptor interceptor1 = createMock(ClientInterceptor.class);
		ClientInterceptor interceptor2 = createMock(ClientInterceptor.class);
		
		messageReceiver.receive(messageContext);
		expect(interceptor1.handleRequest(messageContext)).andReturn(false);
		expect(interceptor1.handleResponse(messageContext)).andReturn(true);
		expect(messageContext.hasResponse()).andReturn(false);
		expect(messageContext.hasResponse()).andReturn(true).atLeastOnce();
		expect(messageContext.getResponse()).andReturn(null);
		
		replay(messageReceiver, interceptor1, interceptor2, messageContext);
		
		
		InterceptingTemplate template = new InterceptingTemplate(new ClientInterceptor[]{interceptor1, interceptor2});
		
		template.interceptRequest(messageContext, messageReceiver);
		
		verify(messageReceiver, interceptor1, interceptor2, messageContext);
	}
	
	protected SaajSoapMessageFactory getMessageFactory() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
		return messageFactory;
	}
}
