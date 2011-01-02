package net.javacrumbs.smock.http.client.connection;

import java.util.LinkedList;
import java.util.List;

import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.ResponseActions;

public class MockConversation {
	private final List<MockConnection> expectedConnections = new LinkedList<MockConnection>();
	
	private int activeConnection = 0;

	private final WebServiceMessageFactory messageFactory;

	private final EndpointInterceptor[] interceptors;
		
	public MockConversation(WebServiceMessageFactory messageFactory, EndpointInterceptor[] interceptors) {
		this.messageFactory = messageFactory;
		this.interceptors = interceptors; 
	}

	public ResponseActions expect(RequestMatcher requestMatcher) {
		MockConnection mockConnection = new MockConnection(requestMatcher, messageFactory, interceptors);
		expectedConnections.add(mockConnection);
		return mockConnection;
	}


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
	
	public void verifyConnections() {
		 if (activeConnection<expectedConnections.size())
		 {
			 throw new AssertionError("Further connection(s) expected");
		 }
	}
}
