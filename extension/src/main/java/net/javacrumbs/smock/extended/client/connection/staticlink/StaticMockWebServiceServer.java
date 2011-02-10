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
package net.javacrumbs.smock.extended.client.connection.staticlink;

import net.javacrumbs.smock.extended.client.connection.MockConnection;
import net.javacrumbs.smock.extended.client.connection.MockConversation;
import net.javacrumbs.smock.extended.client.connection.MockWebServiceServer;

import org.springframework.util.Assert;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.ResponseActions;

/**
 * MockWebServiceServer that stores {@link MockConversation} in a static variable.
 * @author Lukas Krecan
 */
public class StaticMockWebServiceServer implements MockWebServiceServer{
	private static MockConversation mockConversation;
	
	public StaticMockWebServiceServer(WebServiceMessageFactory messageFactory, EndpointInterceptor[] interceptors) {
		Assert.notNull(messageFactory, "messageFactory can not be null");
		mockConversation = new MockConversation(messageFactory, interceptors);
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
		return mockConversation;
	}

	public void verify() {
		getMockConversation().verifyConnections();
	}
}
