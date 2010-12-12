package net.javacrumbs.smock.common.client.connection.staticlink.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import net.javacrumbs.smock.common.client.connection.MockConnection;

public class StaticMockHttpUrlConnection extends HttpURLConnection {
	private final MockConnection activeConnection; 
	
	public StaticMockHttpUrlConnection(URL url) {
		super(url);
		activeConnection = StaticMockWebServiceServer.getActiveConnection();
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
