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
package net.javacrumbs.smock.http.metro.server.servlet;

import net.javacrumbs.smock.http.server.servlet.CommonServletBasedMockWebServiceClient;

import org.springframework.context.ApplicationContext;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

import com.sun.xml.ws.transport.http.servlet.WSSpringServlet;

/**
 * Creates connection to Metro servlet.
 * @author Lukas Krecan
 */
public class ServletBasedMockWebServiceClient extends CommonServletBasedMockWebServiceClient {

	public ServletBasedMockWebServiceClient(ApplicationContext applicationContext, ClientInterceptor[] clientInterceptors) {
		super(WSSpringServlet.class, applicationContext, clientInterceptors);
	}

	public ServletBasedMockWebServiceClient(ApplicationContext applicationContext) {
		this(applicationContext, null);
	}

}
