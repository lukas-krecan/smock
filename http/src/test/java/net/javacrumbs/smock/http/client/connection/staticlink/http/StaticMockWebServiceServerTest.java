package net.javacrumbs.smock.http.client.connection.staticlink.http;

import net.javacrumbs.smock.http.client.connection.MockWebServiceServer;
import net.javacrumbs.smock.http.client.connection.threadlocal.http.ThreadLocalMockWebServiceServer;
import net.javacrumbs.smock.http.client.connection.threadlocal.http.ThreadLocalMockWebServiceServerTest;

public class StaticMockWebServiceServerTest extends ThreadLocalMockWebServiceServerTest
{
	protected MockWebServiceServer createServer() {
		return new ThreadLocalMockWebServiceServer(getMessageFactory(), null);
	}
}
