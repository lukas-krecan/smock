package net.javacrumbs.smock.http.client.connection.staticlink.http;

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

public class StaticMockWebServiceServer implements MockWebServiceServer{
	private static MockConversation mockConversation;
	
	public StaticMockWebServiceServer(ApplicationContext applicationContext, EndpointInterceptor[] interceptors) {
		this(new MockStrategiesHelper(applicationContext).getStrategy(WebServiceMessageFactory.class, SaajSoapMessageFactory.class), interceptors);
	}
	
	public StaticMockWebServiceServer(WebServiceMessageFactory messageFactory, EndpointInterceptor[] interceptors) {
		System.setProperty("java.protocol.handler.pkgs", "net.javacrumbs.smock.http.client.connection.staticlink");
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
