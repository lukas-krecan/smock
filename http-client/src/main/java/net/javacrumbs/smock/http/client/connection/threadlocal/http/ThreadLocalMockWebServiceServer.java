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
package net.javacrumbs.smock.http.client.connection.threadlocal.http;

import net.javacrumbs.smock.http.client.connection.MockConnection;
import net.javacrumbs.smock.http.client.connection.MockConversation;
import net.javacrumbs.smock.http.client.connection.MockWebServiceServer;

import org.springframework.context.ApplicationContext;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.ResponseActions;
import org.springframework.ws.test.support.MockStrategiesHelper;

/**
 * Stores mock conversation in {@link ThreadLocal}.
 * @author Lukas Krecan
 */
public class ThreadLocalMockWebServiceServer implements MockWebServiceServer{
	private static final ThreadLocal<MockConversation> mockConversation = new ThreadLocal<MockConversation>();
	
	public ThreadLocalMockWebServiceServer(ApplicationContext applicationContext, EndpointInterceptor[] interceptors) {
		this(new MockStrategiesHelper(applicationContext).getStrategy(WebServiceMessageFactory.class, SaajSoapMessageFactory.class), interceptors);
	}
	
	public ThreadLocalMockWebServiceServer(WebServiceMessageFactory messageFactory, EndpointInterceptor[] interceptors) {
		System.setProperty("java.protocol.handler.pkgs", "net.javacrumbs.smock.http.client.connection.threadlocal");
		mockConversation.set(new MockConversation(messageFactory, interceptors));
	}
	public ThreadLocalMockWebServiceServer(EndpointInterceptor[] interceptors) {
		this(createDefaultMessageFactory(), interceptors);
	}

	private static WebServiceMessageFactory createDefaultMessageFactory() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.afterPropertiesSet();
		return messageFactory;
	}

	public ResponseActions expect(RequestMatcher requestMatcher)
	{
		return getMockConversation().expect(requestMatcher);
	}
	
	public static MockConnection getActiveConnection()
	{
		return getMockConversation().getActiveConnection();
	}
	
	public static MockConversation getMockConversation() {
		return mockConversation.get();
	}
	public void verify() {
		getMockConversation().verifyConnections();
	}
}
