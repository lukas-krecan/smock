package net.javacrumbs.smock.common.client.connection;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import net.javacrumbs.smock.common.client.connection.MockHttpUrlConnection;

public class CommonHandler extends URLStreamHandler {
	
	
	
	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		return new MockHttpUrlConnection(u);
	}

}
