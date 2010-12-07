package net.javacrumbs.smock.common.client.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MockHttpUrlConnection extends HttpURLConnection {
	private final MockConnection activeConnection; 
	
	public MockHttpUrlConnection(URL url) {
		super(url);
		activeConnection = ThreadLocalMockWebServiceServer.getActiveConnection();
		activeConnection.setUri(URI.create(url.toString()));
	}

	@Override
	public void connect() throws IOException {
	}
	
	@Override
	public void disconnect() {
		
	}

	@Override
	public boolean usingProxy() {
		return false;
	}
	@Override
	public int getResponseCode() throws IOException {
		return activeConnection.getResponseCode();
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return activeConnection.getInputStream();
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return activeConnection.getOutputStream();
	}
}
