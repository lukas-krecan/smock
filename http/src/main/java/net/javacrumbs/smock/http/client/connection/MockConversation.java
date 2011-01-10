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

package net.javacrumbs.smock.http.client.connection;

import java.util.LinkedList;
import java.util.List;

import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.ResponseActions;

/**
 * Stores information about mock server communication.
 * @author Lukas Krecan
 */
public class MockConversation {
	
	/**
	 * Connections expected to be called.
	 */
	private final List<MockConnection> expectedConnections = new LinkedList<MockConnection>();
	
	/**
	 * Index of active connection
	 */
	private int activeConnection = 0;
	
	private final WebServiceMessageFactory messageFactory;

	private final EndpointInterceptor[] interceptors;
		
	public MockConversation(WebServiceMessageFactory messageFactory, EndpointInterceptor[] interceptors) {
		this.messageFactory = messageFactory;
		this.interceptors = interceptors; 
	}

	/**
	 * Adds ne expected connection.
	 * @param requestMatcher
	 * @return
	 */
	public ResponseActions expect(RequestMatcher requestMatcher) {
		MockConnection mockConnection = new MockConnection(requestMatcher, messageFactory, interceptors);
		expectedConnections.add(mockConnection);
		return mockConnection;
	}

	/**
	 * Returns connection to process a request.
	 * @return
	 */
	public MockConnection getActiveConnection()
	{
		if (activeConnection>=expectedConnections.size())
		{
			throw new AssertionError("No further connections expected");
		}
		MockConnection connection = expectedConnections.get(activeConnection);
		activeConnection++;
		return connection;
	}
	
	/**
	 * Throws {@link AssertionError} if more calls were expected.
	 */
	public void verifyConnections() {
		 if (activeConnection<expectedConnections.size())
		 {
			 throw new AssertionError("Further connection(s) expected");
		 }
	}
}
