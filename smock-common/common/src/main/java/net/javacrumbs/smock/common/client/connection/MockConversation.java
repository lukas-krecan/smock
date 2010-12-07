package net.javacrumbs.smock.common.client.connection;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.test.client.RequestMatcher;
import org.springframework.ws.test.client.ResponseActions;
import org.springframework.ws.test.client.ResponseCreator;

public class MockConversation {
	private final List<MockConnection> mockConnections = new ArrayList<MockConnection>();
	
	private int activeConnection = 0;

	private final WebServiceMessageFactory messageFactory;
		
	public MockConversation(WebServiceMessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}

	public ResponseActions expect(RequestMatcher requestMatcher) {
		MockConnection mockConnection = new MockConnection(requestMatcher, messageFactory);
		mockConnections.add(mockConnection);
		return mockConnection;
	}


	public MockConnection getActiveConnection()
	{
		MockConnection connection = mockConnections.get(activeConnection);
		activeConnection++;
		return connection;
	}
	

	public void andRespond(ResponseCreator responseCreator) {
	
	}

}
