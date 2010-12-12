package net.javacrumbs.smock.common.client.connection.staticlink.http;

import net.javacrumbs.smock.common.client.connection.MockWebServiceServer;
import net.javacrumbs.smock.common.client.connection.threadlocal.http.ThreadLocalMockWebServiceServer;
import net.javacrumbs.smock.common.client.connection.threadlocal.http.ThreadLocalMockWebServiceServerTest;

public class StaticMockWebServiceServerTest extends ThreadLocalMockWebServiceServerTest
{
	protected MockWebServiceServer createServer() {
		return new ThreadLocalMockWebServiceServer(getMessageFactory());
	}
}
