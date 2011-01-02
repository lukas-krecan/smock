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

import static org.easymock.EasyMock.createControl;
import static org.easymock.EasyMock.expect;

import java.util.Locale;

import net.javacrumbs.smock.springws.common.AbstractSmockTest;

import org.easymock.IMocksControl;
import org.junit.Test;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.transport.WebServiceMessageReceiver;


public class InterceptingMessageReceiverTest extends AbstractSmockTest{

	@Test
	public void testInterceptOne() throws Exception
	{
		MessageContext messageContext = new DefaultMessageContext(getMessageFactory());

		IMocksControl control = createControl();
		
		WebServiceMessageReceiver wrappedMessageReceiver = new WebServiceMessageReceiver(){
			public void receive(MessageContext messageContext) throws Exception {
				messageContext.setResponse(getMessageFactory().createWebServiceMessage());				
			}
		};
		ClientInterceptor interceptor1 = control.createMock(ClientInterceptor.class);
		ClientInterceptor[] interceptors = new ClientInterceptor[]{interceptor1};
		
		expect(interceptor1.handleRequest(messageContext)).andReturn(true);
		expect(interceptor1.handleResponse(messageContext)).andReturn(true);
		wrappedMessageReceiver.receive(messageContext);
		
		control.replay();
		InterceptingMessageReceiver receiver = new InterceptingMessageReceiver(wrappedMessageReceiver, interceptors);
		receiver.receive(messageContext);
		
		control.verify();
		 
	}
	@Test
	public void testInterceptOneFault() throws Exception
	{
		MessageContext messageContext = new DefaultMessageContext(getMessageFactory());
		
		IMocksControl control = createControl();
		
		WebServiceMessageReceiver wrappedMessageReceiver = new WebServiceMessageReceiver(){
			public void receive(MessageContext messageContext) throws Exception {
				SaajSoapMessage message = getMessageFactory().createWebServiceMessage();
				message.getSoapBody().addServerOrReceiverFault("TestFault", Locale.ENGLISH);
				messageContext.setResponse(message);				
			}
		};
		ClientInterceptor interceptor1 = control.createMock(ClientInterceptor.class);
		ClientInterceptor[] interceptors = new ClientInterceptor[]{interceptor1};
		
		expect(interceptor1.handleRequest(messageContext)).andReturn(true);
		expect(interceptor1.handleFault(messageContext)).andReturn(true);
		wrappedMessageReceiver.receive(messageContext);
		
		control.replay();
		InterceptingMessageReceiver receiver = new InterceptingMessageReceiver(wrappedMessageReceiver, interceptors);
		receiver.receive(messageContext);
		
		control.verify();
		
	}
	@Test
	public void testInterceptTwo() throws Exception
	{
		MessageContext messageContext = new DefaultMessageContext(getMessageFactory());
		
		IMocksControl control = createControl();
		
		WebServiceMessageReceiver wrappedMessageReceiver = new WebServiceMessageReceiver(){
			public void receive(MessageContext messageContext) throws Exception {
				messageContext.setResponse(getMessageFactory().createWebServiceMessage());				
			}
		};
		ClientInterceptor interceptor1 = control.createMock(ClientInterceptor.class);
		ClientInterceptor interceptor2 = control.createMock(ClientInterceptor.class);
		ClientInterceptor[] interceptors = new ClientInterceptor[]{interceptor1, interceptor2};
		
		expect(interceptor1.handleRequest(messageContext)).andReturn(true);
		expect(interceptor1.handleResponse(messageContext)).andReturn(true);
		expect(interceptor2.handleRequest(messageContext)).andReturn(true);
		expect(interceptor2.handleResponse(messageContext)).andReturn(true);
		wrappedMessageReceiver.receive(messageContext);
		
		control.replay();
		InterceptingMessageReceiver receiver = new InterceptingMessageReceiver(wrappedMessageReceiver, interceptors);
		receiver.receive(messageContext);
		
		control.verify();
		
	}
	@Test
	public void testInterceptTwoInterrupt() throws Exception
	{
		MessageContext messageContext = new DefaultMessageContext(getMessageFactory());
		
		IMocksControl control = createControl();
		
		WebServiceMessageReceiver wrappedMessageReceiver = new WebServiceMessageReceiver(){
			public void receive(MessageContext messageContext) throws Exception {
				messageContext.setResponse(getMessageFactory().createWebServiceMessage());				
			}
		};
		ClientInterceptor interceptor1 = control.createMock(ClientInterceptor.class);
		ClientInterceptor interceptor2 = control.createMock(ClientInterceptor.class);
		ClientInterceptor[] interceptors = new ClientInterceptor[]{interceptor1, interceptor2};
		
		expect(interceptor1.handleRequest(messageContext)).andReturn(false);
		expect(interceptor1.handleResponse(messageContext)).andReturn(true);
		wrappedMessageReceiver.receive(messageContext);
		
		control.replay();
		InterceptingMessageReceiver receiver = new InterceptingMessageReceiver(wrappedMessageReceiver, interceptors);
		receiver.receive(messageContext);
		
		control.verify();
		
	}

}
