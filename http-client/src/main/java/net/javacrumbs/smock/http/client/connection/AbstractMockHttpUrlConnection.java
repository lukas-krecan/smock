/**
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.smock.http.client.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import net.javacrumbs.smock.extended.client.connection.MockConnection;
import net.javacrumbs.smock.extended.client.connection.threadlocal.ThreadLocalMockWebServiceServer;

/**
 * Mock implementation of HttpURLConnection. It's an entry point for mock client test. It's necessary
 * to register this connection using "java.protocol.handler.pkgs" system property. It's done automatically
 * when using {@link ThreadLocalMockWebServiceServer}. 
 * @author Lukas Krecan
 */
public abstract class AbstractMockHttpUrlConnection extends HttpURLConnection {

	protected final MockConnection activeConnection;

	public AbstractMockHttpUrlConnection(URL u, MockConnection activConnection) {
		super(u);
		this.activeConnection = activConnection;
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
	
	@Override
	public String getContentType() {
		return "text/xml";
	}

}