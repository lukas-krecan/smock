package net.javacrumbs.smock.common.client.connection.threadlocal.http;

import net.javacrumbs.smock.common.client.connection.AbstractMockWebServiceServerTest;
import net.javacrumbs.smock.common.client.connection.MockWebServiceServer;

public class ThreadLocalMockWebServiceServerTest extends AbstractMockWebServiceServerTest{

	@Override
	protected MockWebServiceServer createServer() {
		return new ThreadLocalMockWebServiceServer(getMessageFactory());
	}

	
}
