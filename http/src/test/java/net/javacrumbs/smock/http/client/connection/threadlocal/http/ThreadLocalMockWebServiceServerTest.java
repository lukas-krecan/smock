package net.javacrumbs.smock.http.client.connection.threadlocal.http;

import net.javacrumbs.smock.http.client.connection.AbstractMockWebServiceServerTest;
import net.javacrumbs.smock.http.client.connection.MockWebServiceServer;
import net.javacrumbs.smock.http.client.connection.threadlocal.http.ThreadLocalMockWebServiceServer;

public class ThreadLocalMockWebServiceServerTest extends AbstractMockWebServiceServerTest{

	@Override
	protected MockWebServiceServer createServer() {
		return new ThreadLocalMockWebServiceServer(getMessageFactory());
	}

	
}
